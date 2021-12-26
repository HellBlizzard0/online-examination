package com.code.ui.backings.worklist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StateMachineConnector;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;

import com.code.dal.orm.setup.EmployeeData;
import com.code.dal.orm.workflow.WFInstanceData;
import com.code.dal.orm.workflow.WFTaskData;
import com.code.enums.WFInstanceStatusEnum;
import com.code.enums.WFProcessesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.setup.EmployeeService;
import com.code.services.workflow.BaseWorkFlow;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
// version 4
@ManagedBean(name = "wfRepresentation")
@ViewScoped
public class WFRepresentation extends BaseBacking implements Serializable {
	private DefaultDiagramModel model;
	private long instanceId;
	private double x, y, yStep = 20;
	private double startShift = 8;
	private int drawStep = 25;// em
	private String topX;
	private WFInstanceData instance;

	public WFRepresentation() {

		if (!getRequest().getParameter("id").equals("null") && !getRequest().getParameter("id").isEmpty() && !getRequest().getParameter("id").equals("undefined")) {
			instanceId = Long.parseLong(getRequest().getParameter("id"));
		}
	}

	@PostConstruct
	public void init() {

		model = new DefaultDiagramModel();
		model.setMaxConnections(-1);

		StateMachineConnector connector = new StateMachineConnector();
		connector.setPaintStyle("{strokeStyle:'#000000',lineWidth:1}");

		model.setDefaultConnector(connector);

		x = 29;
		y = 0.0;
		Element preStart = new Element("", "", "");
		preStart.setDraggable(false);
		preStart.setX(x + startShift + "em");
		preStart.setY(y + "em");
		preStart.setStyleClass("ui-diagram-start");
		model.addElement(preStart);

		List<WFTaskData> tasks;
		try {
			tasks = BaseWorkFlow.getWFInstanceTasksDataSorted(instanceId);
			instance = BaseWorkFlow.getWFInstanceDataById(instanceId);
			Element start = new Element(getStartTaskName(), "", "");
			start.setDraggable(false);
			start.setX(x + "em");
			y += yStep;
			start.setY(y + "em");
			start.setStyleClass("ui-diagram- initiate");
			model.addElement(start);

			model.connect(createConnection(createEndPoint(preStart, EndPointAnchor.BOTTOM), createEndPoint(start, EndPointAnchor.TOP), ""));

			drawFlowChart(tasks, start, 0.0);
			if (topX != null) {
				start.setX(topX);
				preStart.setX(topX);
			}
		} catch (Exception e1) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}

	}

	private void drawFlowChart(List<WFTaskData> tasks, Element start, double xShift) {
		Set<String> xyTrack = new HashSet<String>();
		x = value(start.getX()) + xShift;
		y = value(start.getY()) + yStep;
		try {
			int[] indexOfParent = doParentIndexing(tasks);// have parent index
			Node[] treeRef = new Node[indexOfParent.length];
			for (int i = 0; i < indexOfParent.length; i++) {
				Node tree = new Node(tasks.get(i));
				treeRef[i] = tree;
			}
			double parentChildX = 0.0;
			for (int i = 0; i < indexOfParent.length; i++) {
				int parentIndex = indexOfParent[i];

				if (parentIndex == -1) {
					treeRef[i].x = x + parentChildX;
					treeRef[i].y = y;
					// draw parents first
					Node firstNode = treeRef[i];
					model.addElement(firstNode.element);
					firstNode = TrackXY(firstNode, xyTrack);
					xyTrack.add(firstNode.x + "," + firstNode.y);
					firstNode.element.setX(firstNode.x + "em");
					firstNode.element.setY(firstNode.y + "em");
					model.connect(createConnection(createEndPoint(start, EndPointAnchor.BOTTOM), createEndPoint(firstNode.element, EndPointAnchor.TOP), ""));
					firstNode.drawn = true;
					parentChildX += drawStep;
				} else {
					treeRef[i].x = treeRef[parentIndex].x;
					treeRef[i].y = treeRef[parentIndex].y + yStep;
					treeRef[parentIndex].addChild(treeRef[i]);
				}
			}
			// /////////////////// end building tree ///////////////////////
			// start drawing after parents

			Node lastParentDrawn = null;
			if (treeRef.length > 1)
				for (int i = 0; i < treeRef.length; i++) {
					Node node = treeRef[i];
					if (tasks.get(i).getAction() == null)
						if (node.drawn == false) {
							// draw node
							model.addElement(node.element);
							node = TrackXY(node, xyTrack);
							xyTrack.add(node.x + "," + node.y);
							node.element.setX(node.x + "em");
							node.element.setY(node.y + "em");
							model.connect(createConnection(createEndPoint(treeRef[i - 1].element, EndPointAnchor.BOTTOM), createEndPoint(node.element, EndPointAnchor.TOP), ""));
							node.drawn = true;
						}
					if (node.children.size() > 0) {
						// draw children
						lastParentDrawn = node;
						boolean odd = false;

						int childrenCount = node.children.size();
						if (childrenCount % 2 != 0)
							odd = true;
						double xDrawChildIndex = (odd && childrenCount != 1) ? (node.x - drawStep * (childrenCount - 2)) : (node.x - drawStep * (childrenCount - 1));
						for (int j = 0; j < node.children.size(); j++) {
							Node chiNode = node.children.get(j);
							if (chiNode.drawn == false) {
								// draw node
								model.addElement(chiNode.element);
								chiNode.x = xDrawChildIndex;
								chiNode = TrackXY(chiNode, xyTrack);
								xyTrack.add(chiNode.x + "," + chiNode.y);
								chiNode.element.setX(chiNode.x + "em");
								if (xDrawChildIndex < xShift)
									xShift = xDrawChildIndex;
								chiNode.element.setY(chiNode.y + "em");
								model.connect(createConnection(createEndPoint(node.element, EndPointAnchor.BOTTOM), createEndPoint(chiNode.element, EndPointAnchor.TOP), ""));
								chiNode.drawn = true;
								xDrawChildIndex += drawStep;
								if (xDrawChildIndex == x && !odd)
									xDrawChildIndex += drawStep;
							}

						}

					}
				}

			if (xShift < 0) {
				xShift *= -1;
				for (int j = 0; j < treeRef.length; j++) {
					String elementX = treeRef[j].element.getX();
					double shift = Double.parseDouble(elementX.substring(0, elementX.indexOf("em")));
					treeRef[j].element.setX((shift + xShift) + "em");
				}

			}

			if (!instance.getStatus().equals(WFInstanceStatusEnum.RUNING.getCode()) || instance.getProcessId().equals(WFProcessesEnum.NOTIFICATIONS.getCode())) {
				boolean endDrawn = false;
				Element end = new Element();
				end.setDraggable(false);
				for (int j = 0; j < lastParentDrawn.children.size(); j++) {
					Node chiNode = lastParentDrawn.children.get(j);
					if (!endDrawn) {
						endDrawn = true;
						String elementX = chiNode.element.getX();
						double value = Double.parseDouble(elementX.substring(0, elementX.indexOf("em")));
						end.setX(value + startShift + "em");
						y = value(chiNode.element.getY()) + yStep;
						end.setY(y + "em");
						end.setStyleClass("ui-diagram-end");
						model.addElement(end);
					}
					model.connect(createConnection(createEndPoint(chiNode.element, EndPointAnchor.BOTTOM), createEndPoint(end, EndPointAnchor.TOP), ""));
				}
			}
			topX = treeRef[0].element.getX();

		} catch (Exception e1) {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
	}

	private Node TrackXY(Node firstNode, Set<String> xyTrack) {
		String xy = firstNode.x + "," + firstNode.y;
		while (xyTrack.contains(xy)) {
			firstNode.x += drawStep;
			// firstNode.y -= 3;// to avoid arrows overlapping ..
			xy = firstNode.x + "," + firstNode.y;
		}
		return firstNode;
	}

	private double value(String x) {
		// x on format 12.0em
		return Double.parseDouble(x.substring(0, x.indexOf("em")));
	}

	private String getStartTaskName() {
		String out = "";
		try {
			EmployeeData starterEmp = EmployeeService.getEmployee(instance.getRequesterId());
			out += starterEmp.getRank() + "/" + starterEmp.getFirstName();
			out = checkElementLength(out);
			out += "\n";
			String actualTitleDescription = starterEmp.getActualTitleDescription();
			out += " " + actualTitleDescription;
			out += "\n";
			String actualDepartmentName = starterEmp.getActualDepartmentName();
			String[] actualDepartmentNameSplitted = starterEmp.getActualDepartmentName().split("/");
			out += actualDepartmentName.contains("\u0642\u0637\u0627\u0639") ? " " + actualDepartmentNameSplitted[1] + "/" + actualDepartmentNameSplitted[actualDepartmentNameSplitted.length - 1] : " " + actualDepartmentNameSplitted[0] + "/" + actualDepartmentNameSplitted[actualDepartmentNameSplitted.length - 1];
			out += "\n";
			out += "\n";
			out += "\n";
			out += "\n";
			out += "\n";

			if (instance.getHijriRequestDateString() != null) {
				out += getParameterizedMessage("label_elementOrderIntreducingDate");
				out += instance.getHijriRequestDateString();
				out += " \n";

			}
		} catch (BusinessException e) {
		}
		return out;
	}

	private String checkElementLength(String name) {
		String ret = "";
		if (name.length() > 50) {
			int index = name.substring(0, 30).lastIndexOf(" ");
			ret += name.substring(0, index);
			ret += "\n";
			ret += name.substring(index);
			return ret;
		} else
			return name;
	}

	private String getTaskName(WFTaskData task) {
		String out = "";
		out += task.getOriginalRankDesc() + "/" + task.getOriginalName();
		out = checkElementLength(out);
		out += "\n";

		try {
			EmployeeData taskEmployee = EmployeeService.getEmployee(task.getOriginalId());
			String actualTitleDescription = taskEmployee.getActualTitleDescription();
			out += " " + actualTitleDescription;
			out += "\n";
			String actualDepartmentName = taskEmployee.getActualDepartmentName();
			String[] actualDepartmentNameSplitted = taskEmployee.getActualDepartmentName().split("/");
			out += actualDepartmentName.contains("\u0642\u0637\u0627\u0639") ? " " + actualDepartmentNameSplitted[1] + "/" + actualDepartmentNameSplitted[actualDepartmentNameSplitted.length - 1] : " " + actualDepartmentNameSplitted[0] + "/" + actualDepartmentNameSplitted[actualDepartmentNameSplitted.length - 1];
			out += "\n";
		} catch (BusinessException e) {
		}

		if (task.getAction() != null) {
			out += getParameterizedMessage("label_elementAction");
			out += task.getAction();
			out += "\n";

		}
		out += "\n";
		out += "\n";
		out += "\n";
		out += "\n";

		out += getParameterizedMessage("label_elementAssigneeDate");
		out += task.getHijriAssignDateString();
		out += " \n";

		if (task.getActionDate() != null) {
			out += getParameterizedMessage("label_elementActionDate");
			out += task.getHijriActionDateString();
			out += " \n";

		}

		return out;
	}

	private int[] doParentIndexing(List<WFTaskData> tasks) {
		int indexOfParent[] = new int[tasks.size()]; // parent index
		boolean parentFound[] = new boolean[9];
		int levelLength = 1;
		for (int i = 0; i < tasks.size(); i++) {
			String taskLevel = tasks.get(i).getLevel().trim();// 1.1.1 1.1.2
			if (taskLevel.length() > levelLength) {
				String prefex = getPrefex(taskLevel);
				int parentIndex = getParentIndex(tasks, i, prefex);
				indexOfParent[i] = parentIndex;
			} else // < or ==
			{ // ==
				// == and diff or == and equal 1 1 1.1 1.2
				if (taskLevel.length() == 1) {
					int startLevel = Integer.parseInt(taskLevel);
					if (!parentFound[startLevel]) {
						indexOfParent[i] = -1;
						parentFound[startLevel] = true;
					} else
						indexOfParent[i] = i - 1;
				} else { // 1.1 1.2
					if (taskLevel.equals(tasks.get(i - 1).getLevel().trim()))
						indexOfParent[i] = i - 1;
					else {
						String prefex = getPrefex(taskLevel);
						int parentIndex = getParentIndex(tasks, i, prefex);
						indexOfParent[i] = parentIndex;
					}
				}
			}
			levelLength = taskLevel.length();
		}

		return indexOfParent;
	}

	private int getParentIndex(List<WFTaskData> tasks, int to, String prefex) {
		for (int i = to - 1; i >= 0; i--) {
			if (tasks.get(i).getLevel().equals(prefex))
				return i;
		}
		return -1;
	}

	private String getPrefex(String taskLevel) {
		// 1.1.2
		int lastDot = taskLevel.lastIndexOf(".");
		return taskLevel.substring(0, lastDot);
	}

	private EndPoint createEndPoint(Element element, EndPointAnchor left) {
		element.addEndPoint(new BlankEndPoint(left));
		int lastIndex = element.getEndPoints().size() - 1;
		return element.getEndPoints().get(lastIndex);
	}

	public DiagramModel getModel() {
		return model;
	}

	private Connection createConnection(EndPoint from, EndPoint to, String label) {
		Connection conn = new Connection(from, to);
		conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));

		if (label != null) {
			conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
		}

		return conn;
	}

	public long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(long instanceId) {
		this.instanceId = instanceId;
	}

	public class Node {
		private List<Node> children = null;
		public WFTaskData task;
		public Element element;
		public boolean drawn;
		public double x, y;

		public Node(WFTaskData task) {
			this.children = new ArrayList<WFRepresentation.Node>();
			this.task = task;
			element = new Element(getTaskName(task));
			element.setDraggable(false);
			if (task.getAction() == null) {
				element.setStyleClass("ui-diagram-no-action");
			} else {
				element.setStyleClass("ui-diagram-with-action");
			}

			if (task.getAssigneeWfRole().equals("Notification")) {
				if (task.getAction() == null) {
					element.setStyleClass("ui-diagram-notify-no-action");
				} else {
					element.setStyleClass("ui-diagram-notify-with-action");
				}
			}

			drawn = false;
		}

		public void addChild(Node child) {
			children.add(child);
		}

	}
}