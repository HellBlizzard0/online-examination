package com.code.ui.backings.minisearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;

import com.code.dal.orm.securityanalysis.ConversationData;
import com.code.dal.orm.securityanalysis.ConversationPartyData;
import com.code.dal.orm.securityanalysis.FollowUpData;
import com.code.dal.orm.securityanalysis.NetworkData;
import com.code.enums.ConversationResultsEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.NoDataException;
import com.code.services.infosys.securityanalysis.ConversationService;
import com.code.services.infosys.securityanalysis.FollowUpService;
import com.code.services.log4j.Log4j;
import com.code.ui.backings.base.BaseBacking;

@SuppressWarnings("serial")
@ManagedBean(name = "commuincationNodes")
@ViewScoped
public class CommunicationNodes extends BaseBacking implements Serializable {
	private String screenTitle;
	private int mode;
	private DefaultDiagramModel model;
	private long width;
	private long height;
	private long x;
	private long y;
	private int space;
	private Long networkId;
	private NetworkData networkData;
	private List<FollowUpData> followUpDataList;
	private Map<Long, List<ConversationPartyData>> conversationPartyDataMap;
	private Map<Element, List<Element>> Tree;
	private List<String> nodeTitles;
	private List<String> nodeToolTip;
	private int countTitle = -1;
	private int countTooltip = -1;

	public CommunicationNodes() {
		super();
		init();

		if (!getRequest().getParameter("mode").equals("null") && !getRequest().getParameter("mode").isEmpty() && !getRequest().getParameter("mode").equals("undefined")) {
			mode = (Integer.parseInt(getRequest().getParameter("mode")));
		} else {
			this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		}
		try {
			if (mode == 1) {

				if (!getRequest().getParameter("followUpId").equals("null") && !getRequest().getParameter("followUpId").isEmpty() && !getRequest().getParameter("followUpId").equals("undefined")) {
					FollowUpData followUpData = new FollowUpData();
					followUpData.setId(Long.parseLong(getRequest().getParameter("followUpId")));
					followUpData = FollowUpService.getFollowUpDataById(followUpData.getId());
					followUpDataList.add(followUpData);
					List<ConversationPartyData> conversationPartyDataList = new ArrayList<>();
					List<ConversationData> conversationData = new ArrayList<>();
					conversationData = ConversationService.getConversationsByConversationResult(followUpData.getId(), ConversationResultsEnum.POSITIVE.getCode());
					for (ConversationData convData : conversationData) {
						conversationPartyDataList.addAll(ConversationService.getConversationsPartiesByConversationId(convData.getId()));
					}
					conversationPartyDataMap.put(followUpData.getId(), conversationPartyDataList);
					String ghostName = "";
					if (followUpData.getGhostType() == 1)
						ghostName = followUpData.getEmployeeName();
					else
						ghostName = followUpData.getNonEmployeeName();
					if (ghostName == null)
						ghostName = "";
					screenTitle = this.getParameterizedMessage("title_conversationsNodes_1", new Object[] { ghostName });

				}
			} else if (mode == 2) {

				if (!getRequest().getParameter("networkId").equals("null") && !getRequest().getParameter("networkId").isEmpty() && !getRequest().getParameter("networkId").equals("undefined")) {
					networkId = (Long.parseLong(getRequest().getParameter("networkId")));
					networkData = FollowUpService.getNetworkDataById(networkId);
					followUpDataList = FollowUpService.getFollowUpsDataByNetworkId(networkId);
					screenTitle = this.getParameterizedMessage("title_conversationsNodes_2", new Object[] { networkData.getNetworkNumber() });

					for (FollowUpData followUp : followUpDataList) {
						List<ConversationData> conversationData = new ArrayList<>();
						conversationData = ConversationService.getConversationsByConversationResult(followUp.getId(), ConversationResultsEnum.POSITIVE.getCode());
						List<ConversationPartyData> parties = new ArrayList<>();
						for (ConversationData convData : conversationData) {
							parties.addAll(ConversationService.getConversationsPartiesByConversationId(convData.getId()));
						}
						conversationPartyDataMap.put(followUp.getId(), parties);
					}
				}

			}
		} catch (BusinessException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		} catch (NoDataException e) {
			this.setServerSideErrorMessages(getParameterizedMessage(e.getMessage()));
		}catch (Exception e) {
			   Log4j.traceErrorException(CommunicationNodes.class, e, "CommunicationNodes");
			   this.setServerSideErrorMessages(getParameterizedMessage("error_general"));
		  }

		DrawDiagram();
	}

	/**
	 * Initialize/Reset Variables
	 */
	public void init() {
		width = 1000;
		height = 700;
		space = 10;
		followUpDataList = new ArrayList<>();
		conversationPartyDataMap = new HashMap<>();
		Tree = new HashMap<>();
		// toolTips = new HashMap<>();
		nodeTitles = new ArrayList<>();
		nodeToolTip = new ArrayList<>();
	}

	public void DrawDiagram() {
		x = (width + 200) / 2;
		y = (height - 200) / 2;

		model = new DefaultDiagramModel();
		model.setMaxConnections(-1);

		StraightConnector connector = new StraightConnector();
		connector.setPaintStyle("{strokeStyle:'#000000',lineWidth:0.5}");
		model.setDefaultConnector(connector);

		Element centerCase = new Element();
		int nL1 = 0;
		int nL2 = 0;
		int startAngle = 0;
		long min = Integer.MAX_VALUE;
		List<Element> elements = new ArrayList<Element>();

		int totalNL = 0;
		int tAngle = 0;
		if (mode == 1) {
			centerCase = new Element(followUpDataList.get(0).getContactNumber(), x + "px", y + "px");
			nL1 = conversationPartyDataMap.get(followUpDataList.get(0).getId()).size();
			centerCase.setStyleClass("start-node");
		} else if (mode == 2) {
			centerCase = new Element(networkData.getNetworkNumber(), x + "px", y + "px");
			nL1 = followUpDataList.size();
			int maxLength = Integer.MIN_VALUE;
			for (Long followUpId : conversationPartyDataMap.keySet()) {
				if (conversationPartyDataMap.get(followUpId).size() > maxLength)
					maxLength = conversationPartyDataMap.get(followUpId).size();
			}
			nL2 = maxLength;

			totalNL = nL1 * nL2;
			if (totalNL != 0)
				tAngle = 360 / totalNL;
			else
				tAngle = 0;
			centerCase.setStyleClass("start-node");

		}

		centerCase.setDraggable(true);
		centerCase.setId("centerCase");
		model.addElement(centerCase);
		Tree.put(centerCase, new ArrayList<>());
		nodeTitles.add(centerCase.getData().toString());
		nodeToolTip.add("");

		String[] level1 = new String[nL1];
		String[][] level2 = new String[nL1][nL2];
		int angle = 0;
		if (nL1 != 0) {
			angle = 360 / (nL1 % 2 == 0 ? nL1 : nL1 + 1);
		}
		int i = 0;

		if (mode == 1) {
			List<Element> childOfCenterCase = new ArrayList<>();
			List<ConversationPartyData> conversationPartyList = conversationPartyDataMap.get(followUpDataList.get(0).getId());
			for (ConversationPartyData cParty : conversationPartyList) {
				level1[i] = cParty.getContactNumber().toString();

				//////
				String registrationDate = cParty.getRegistraionDateString();
				String networkDesc = cParty.getDomainIdNetworkRolesDesc();
				if (registrationDate == null)
					registrationDate = "";
				if (networkDesc == null)
					networkDesc = "";
				String toolTip = getParameterizedMessage("label_nodeToolTip", new Object[] { registrationDate, networkDesc });
				if (angle < 20)
					angle += 30;
				long r1 = (i % 2 == 1 && nL1 >= 3) ? 10 * angle : 6 * angle;

				if (r1 < 70)
					r1 += 50;
				long xCenter = (stringToLong(centerCase.getX())) + (long) (r1 * Math.cos(Math.toRadians(startAngle)));

				if (xCenter > 1100 && nL1 < 5)
					while (xCenter > 1100)
						xCenter -= 150;
				if (xCenter < 0)
					while (xCenter < 0)
						xCenter += 100;
				long yCenter = (long) (stringToLong(centerCase.getY()) + (long) (r1 * Math.sin(Math.toRadians(startAngle))));
				if (yCenter > 600)
					yCenter -= 600;
				if (yCenter < 0)
					while (yCenter < 0)
						yCenter += 100;
				Element parent = addElement(centerCase, level1[i], xCenter, yCenter);
				parent.setId("centerCase" + cParty.getId().toString());
				elements.add(parent);
				childOfCenterCase.add(parent);
				// toolTips.put(parent, toolTip);
				nodeTitles.add(parent.getData().toString());
				nodeToolTip.add(toolTip);
				startAngle += angle;
				i++;
			}
			Tree.put(centerCase, childOfCenterCase);
		} else if (mode == 2) {
			int temp = 0;
			List<Element> childOfParent;
			for (FollowUpData followUpData : followUpDataList) {
				childOfParent = new ArrayList<>();
				level1[i] = followUpData.getContactNumber();
				////
				// long r1 = (i % 2 == 1 && nL1 >= 3) ? 6 * angle : 4 * angle; //old
				long r1 = 0;
				if (nL1 >= 3) {
					if (i % 2 == 1) {
						r1 = 6 * angle;
					} else {
						r1 = 4 * angle;
					}
				} else {
					if (nL1 % 2 == 1) {
						r1 = 6 * angle;
					} else {
						r1 = 4 * angle;
					}
				}

				long xParent = (stringToLong(centerCase.getX())) + (long) (r1 * Math.cos(Math.toRadians(startAngle)));
				if (xParent > 1100)
					while (xParent > 1100)
						xParent -= 100;
				if (xParent < 0)
					while (xParent < 0)
						xParent += 200;
				long yParent = stringToLong(centerCase.getY()) + (long) (r1 * Math.sin(Math.toRadians(startAngle)));
				if (yParent < 0)
					while (yParent < 0)
						yParent += 100;
				if (yParent > 600)
					yParent -= 250;
				Element parent = addElement(centerCase, level1[i], xParent, yParent);
				parent.setId("parent:" + followUpData.getId().toString());
				parent.setDraggable(true);
				boolean found = false;
				for (Element e : elements) {
					if (e.getData().equals(parent.getData())) {
						parent = e;
						found = true;
						break;
					}
				}
				if (!found) {
					elements.add(parent);
					Tree.put(parent, childOfParent);
					String registrationDate = followUpData.getFollowUpStartDateString();
					String networkDesc = followUpData.getNetworkRoleDesc();
					if (registrationDate == null)
						registrationDate = "";
					if (networkDesc == null)
						networkDesc = "";
					String toolTip = getParameterizedMessage("label_nodeToolTip", new Object[] { registrationDate, networkDesc });
					// toolTips.put(parent, toolTip);
					nodeTitles.add(parent.getData().toString());
					nodeToolTip.add(toolTip);

				} else {
					found = false;
				}
				// int weight = (angle * 3) / nL2 - space / nL2;
				// weight = weight * nL2 >= 360 ? 360 / nL2 : weight;
				///////

				startAngle += angle;
				int j = 0;
				List<ConversationPartyData> conversationPartyList = conversationPartyDataMap.get(followUpData.getId());
				for (ConversationPartyData cParty : conversationPartyList) {
					level2[i][j] = cParty.getContactNumber().toString();

					//////
					long r2 = (nL2 < 3 || j % 2 == 1) ? 130 : 180;

					long yChild = stringToLong(parent.getY()) + (long) (r2 * Math.sin(Math.toRadians(temp)));
					long xChild = stringToLong(parent.getX()) + (long) (r2 * Math.cos(Math.toRadians(temp)));
					temp += 2 * tAngle;
					if (xChild > 1200)
						while (xChild > 1200)
							xChild -= 100;
					if (xChild < 0)
						while (xChild < 0)
							xChild += 100;
					if (yChild < 0)
						while (yChild < 0)
							yChild += 100;
					if (yChild > 600)
						yChild -= 250;

					found = false;
					Element child = new Element();

					for (Element e : elements) {
						if (e.getData().equals(level2[i][j])) {
							List<EndPoint> endPoints = e.getEndPoints();
							if (endPoints.size() > 1) {
								if (parent.getEndPoints().size() == 0)
									parent.addEndPoint(new BlankEndPoint(EndPointAnchor.AUTO_DEFAULT));
								if (e.getEndPoints().size() == 0)
									e.addEndPoint(new BlankEndPoint(EndPointAnchor.AUTO_DEFAULT));
								model.connect(new Connection(e.getEndPoints().get(0), parent.getEndPoints().get(0)));
								e.setX(xChild + "px");
								e.setY(yChild + "px");

								List<Element> modifyChildren = Tree.get(e);
								Long mXChild = 0L;
								Long mYChild = 0L;
								int xAtemp = (360 / (nL1 * nL2)) + 20;
								int xtemp = 0;
								int tempR = 0;
								int count = 0;
								for (Element ch : modifyChildren) {
									tempR = (modifyChildren.size() < 3 && count % 2 == 1) ? 70 : 100;
									mYChild = stringToLong(e.getY()) + (long) (tempR * Math.sin(Math.toRadians(xtemp)));
									mXChild = stringToLong(e.getX()) + (long) (tempR * Math.cos(Math.toRadians(xtemp)));
									ch.setX(mXChild + "px");
									ch.setY(mYChild + "px");
									xtemp += xAtemp;

								}
								found = true;
								break;

							}
						}
					}
					if (!found) {
						if (parent.getY().equals(yChild))
							yChild += 30;
						child = addElement(parent, level2[i][j], xChild, yChild);
						child.setDraggable(true);
						child.setId("child:" + cParty.getId().toString());
						elements.add(child);
						childOfParent.add(child);
						String registrationDate = cParty.getRegistraionDateString();
						String networkDesc = cParty.getDomainIdNetworkRolesDesc();
						if (registrationDate == null)
							registrationDate = "";
						if (networkDesc == null)
							networkDesc = "";
						String toolTip = getParameterizedMessage("label_nodeToolTip", new Object[] { registrationDate, networkDesc });
						// toolTips.put(child, toolTip);
						nodeTitles.add(child.getData().toString());
						nodeToolTip.add(toolTip);

					} else {
						found = false;
					}
					// childAngle += weight;
					if (yChild < min) {
						min = yChild;
					}
					j++;
					//////
				}
				Tree.put(parent, childOfParent);
				i++;
			}
		}

	}

	private Element addElement(Element parent, String title, long x, long y) {
		Element element = new Element(title, x + "px", y + "px");
		element.setDraggable(true);
		model.addElement(element);
		model.connect(createConnection(createEndPoint(parent, EndPointAnchor.AUTO_DEFAULT), createEndPoint(element, EndPointAnchor.AUTO_DEFAULT), ""));
		return element;
	}

	private long stringToLong(String x) {
		return Long.parseLong(x.substring(0, x.length() - 2));
	}

	private EndPoint createEndPoint(Element element, EndPointAnchor left) {
		element.addEndPoint(new BlankEndPoint(left));
		int lastIndex = element.getEndPoints().size() - 1;
		return element.getEndPoints().get(lastIndex);
	}

	private Connection createConnection(EndPoint from, EndPoint to, String label) {
		return new Connection(from, to);
	}

	public DiagramModel getModel() {
		return model;
	}

	public String getNodeToolTips() {
		countTooltip++;
		if (countTooltip == nodeToolTip.size())
			countTooltip--; // to be fixed
		return nodeToolTip.get(countTooltip);
	}

	public String getNodeTitle() {

		countTitle++;

		if (countTitle == nodeTitles.size())
			countTitle--; // to be fixed
		return nodeTitles.get(countTitle);

	}

	/************* Getters && Setters ****************************/

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public long getWidth() {
		return width;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public long getHeight() {
		return height;
	}

	public void setHeight(long height) {
		this.height = height;
	}

	public long getX() {
		return x;
	}

	public void setX(long x) {
		this.x = x;
	}

	public long getY() {
		return y;
	}

	public void setY(long y) {
		this.y = y;
	}

	public int getSpace() {
		return space;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public Long getNetworkId() {
		return networkId;
	}

	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}

	public NetworkData getNetworkData() {
		return networkData;
	}

	public void setNetworkData(NetworkData networkData) {
		this.networkData = networkData;
	}

	public List<FollowUpData> getFollowUpDataList() {
		return followUpDataList;
	}

	public void setFollowUpDataList(List<FollowUpData> followUpDataList) {
		this.followUpDataList = followUpDataList;
	}

	public Map<Long, List<ConversationPartyData>> getConversationPartyDataMap() {
		return conversationPartyDataMap;
	}

	public void setConversationPartyDataMap(Map<Long, List<ConversationPartyData>> conversationPartyDataMap) {
		this.conversationPartyDataMap = conversationPartyDataMap;
	}

	public void setModel(DefaultDiagramModel model) {
		this.model = model;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public List<String> getNodeTitles() {
		return nodeTitles;
	}

	public void setNodeTitles(List<String> nodeTitles) {
		this.nodeTitles = nodeTitles;
	}

	public List<String> getNodeToolTip() {
		return nodeToolTip;
	}

	public void setNodeToolTip(List<String> nodeToolTip) {
		this.nodeToolTip = nodeToolTip;
	}

	// public class NetworkElement implements Serializable {
	//
	// private String name;
	// private String toolTip;
	//
	// public NetworkElement(String name, String toolTip) {
	// this.name = name;
	// this.toolTip = toolTip;
	// }
	//
	// public String getName() {
	// return name;
	// }
	//
	// public void setName(String name) {
	// this.name = name;
	// }
	//
	// public String getToolTip() {
	// return toolTip;
	// }
	//
	// public void setToolTip(String toolTip) {
	// this.toolTip = toolTip;
	// }
	//
	// @Override
	// public String toString() {
	// return name;
	// }
	//
	// }

}