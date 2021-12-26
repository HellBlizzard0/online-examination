package com.code.ui.components.coordinates;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

@FacesComponent(value = "coordinates")
public class UICoordinates extends UINamingContainer {
	private String nDegrees;
	private String nMinutes;
	private String nSecs;
	private String eDegrees;
	private String eMinutes;
	private String eSecs;
	private final Integer SECONDS = 60;
	private final Integer MINUTES = 3600;
	private boolean emptyLatitude;
	private boolean emptyLongitude;

	/**
	 * Encode Begin
	 */
	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
		Double latitude = (Double) getAttributes().get("latitude");
		emptyLatitude = (boolean) getAttributes().get("emptyLatitude");

		Double longitude = (Double) getAttributes().get("longitude");
		emptyLongitude = (boolean) getAttributes().get("emptyLongitude");

		setECoordinate(latitude);
		setNCoordinate(longitude);
	}

	/**
	 * Submit N value to Bean
	 */
	public void submitNCoordinateValues(AjaxBehaviorEvent event) {
		ValueExpression longitude = getValueExpression("longitude");
		Double longitudeValue = evaluateCoordinates(nDegrees, nMinutes, nSecs);
		longitude.setValue(FacesContext.getCurrentInstance().getELContext(), longitudeValue);
		invokeEventListener();
	}

	/**
	 * Submit E value to Bean
	 */
	public void submitECoordinateValues(AjaxBehaviorEvent event) {
		ValueExpression latitude = getValueExpression("latitude");
		Double latitudeValue = evaluateCoordinates(eDegrees, eMinutes, eSecs);
		latitude.setValue(FacesContext.getCurrentInstance().getELContext(), latitudeValue);
		invokeEventListener();
	}

	/**
	 * Evaluate Coordinates
	 * 
	 * @param degree
	 * @param minutes
	 * @param secs
	 * @return
	 */
	public Double evaluateCoordinates(String degree, String minutes, String secs) {
		return Double.valueOf(degree) + (Double.valueOf(minutes) / SECONDS) + (Double.valueOf(secs) / MINUTES);
	}

	/**
	 * Set Latitude
	 * 
	 * @param latitude
	 */
	public void setNCoordinate(Double latitude) {
		if (emptyLatitude) {
			nDegrees = "";
			nMinutes = "";
			nSecs = "";
		} else {
			nDegrees = latitude.intValue() + "";
			Double nMinutesDouble = ((latitude - latitude.intValue()) * SECONDS);
			nMinutes = nMinutesDouble.intValue() + "";
			DecimalFormat df = new DecimalFormat("##.##");
			nSecs = df.format((nMinutesDouble - nMinutesDouble.intValue()) * SECONDS);
		}
	}

	/**
	 * Set Longitude
	 * 
	 * @param longitude
	 */
	public void setECoordinate(Double longitude) {
		if (emptyLongitude) {
			eDegrees = "";
			eMinutes = "";
			eSecs = "";
		} else {
			eDegrees = longitude.intValue() + "";
			Double eMinutesDouble = ((longitude - longitude.intValue()) * SECONDS);
			eMinutes = eMinutesDouble.intValue() + "";
			DecimalFormat df = new DecimalFormat("##.##");
			eSecs = df.format((eMinutesDouble - eMinutesDouble.intValue()) * SECONDS);
		}
	}

	private void invokeEventListener() {
		MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("listener");
		if (ajaxEventListener != null)
			ajaxEventListener.invoke(FacesContext.getCurrentInstance().getELContext(), new Object[] {});
	}

	public String getnDegrees() {
		return nDegrees;
	}

	public void setnDegrees(String nDegrees) {
		this.nDegrees = nDegrees;
	}

	public String getnMinutes() {
		return nMinutes;
	}

	public void setnMinutes(String nMinutes) {
		this.nMinutes = nMinutes;
	}

	public String getnSecs() {
		return nSecs;
	}

	public void setnSecs(String nSecs) {
		this.nSecs = nSecs;
	}

	public String geteDegrees() {
		return eDegrees;
	}

	public void seteDegrees(String eDegrees) {
		this.eDegrees = eDegrees;
	}

	public String geteMinutes() {
		return eMinutes;
	}

	public void seteMinutes(String eMinutes) {
		this.eMinutes = eMinutes;
	}

	public String geteSecs() {
		return eSecs;
	}

	public void seteSecs(String eSecs) {
		this.eSecs = eSecs;
	}
}