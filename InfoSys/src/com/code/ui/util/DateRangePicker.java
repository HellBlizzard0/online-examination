package com.code.ui.util;

import java.io.IOException;
import java.util.Date;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;

import net.sf.jasperreports.types.date.DateRange;

@FacesComponent(value = "dateRangePicker")
public class DateRangePicker extends UINamingContainer {

    private final static String SEARCH_DATE_FROM = "searchDateFrom";
    private final static String SEARCH_DATE_TO = "searchDateTo";
    private final static String RANGE_TYPE = "rangeType";
    private final static String SHOW_DATE_RANGE_PICKER = "showDateRangePicker";

    public void encodeBegin(FacesContext context) throws IOException {
	super.encodeBegin(context);

	ValueExpression dateFromValueExpression = getValueExpression("dateFromValue");
	setSearchDateFrom((Date) dateFromValueExpression.getValue(FacesContext.getCurrentInstance().getELContext()));

	ValueExpression dateToValueExpression = getValueExpression("dateToValue");
	setSearchDateTo((Date) dateToValueExpression.getValue(FacesContext.getCurrentInstance().getELContext()));

	setShowDateRangePicker(false);
	setRangeType("today");
    }

    public void toggleDateRangePickerDisplay() {
	setShowDateRangePicker(!getShowDateRangePicker());
    }

    public void changeSearchDateRange(String rangeType) {
	try {
	    int month, year, daysInLastMonth;
	    Date today = HijriDateService.getHijriSysDate();
	    String hijriDateString;
	    switch (rangeType) {
	    case "today":
		setSearchDateFrom(today);
		setSearchDateTo(today);
		break;

	    case "yesterday":
		Date yesterday = HijriDateService.addSubHijriDays(today, -1);
		setSearchDateFrom(yesterday);
		setSearchDateTo(yesterday);
		break;

	    case "last-7-days":
		setSearchDateFrom(HijriDateService.addSubHijriDays(today, -6));
		setSearchDateTo(today);
		break;

	    case "last-30-days":
		setSearchDateFrom(HijriDateService.addSubHijriDays(today, -29));
		setSearchDateTo(today);
		break;

	    case "this-month":
		Date nextMonthDay = HijriDateService.addSubHijriMonthsDays(today, 1, 0);
		daysInLastMonth = HijriDateService.hijriDateDiff(today, nextMonthDay);
		month = HijriDateService.getHijriDateMonth(today);
		year = HijriDateService.getHijriDateYear(today);

		hijriDateString = "01/" + (month < 10 ? "0" + month : month) + "/" + year;
		setSearchDateFrom(HijriDateService.getHijriDate(hijriDateString));

		hijriDateString = daysInLastMonth + "/" + (month < 10 ? "0" + month : month) + "/" + year;
		setSearchDateTo(HijriDateService.getHijriDate(hijriDateString));
		break;

	    case "last-month":
		Date lastMonthDay = HijriDateService.addSubHijriMonthsDays(today, -1, 0);
		daysInLastMonth = HijriDateService.hijriDateDiff(lastMonthDay, today);
		month = HijriDateService.getHijriDateMonth(lastMonthDay);
		year = HijriDateService.getHijriDateYear(lastMonthDay);

		hijriDateString = "01/" + (month < 10 ? "0" + month : month) + "/" + year;
		setSearchDateFrom(HijriDateService.getHijriDate(hijriDateString));

		hijriDateString = daysInLastMonth + "/" + (month < 10 ? "0" + month : month) + "/" + year;
		setSearchDateTo(HijriDateService.getHijriDate(hijriDateString));
		break;
	    }
	    setRangeType(rangeType);
	    if (!rangeType.equals("custom")) {
		submitChanges();
		close();
	    }
	} catch (BusinessException e) {
	    Log4j.traceLog(DateRange.class, e.getMessage());
	}
    }

    public void apply() {
	submitChanges();
	close();
    }

    public void cancel() {
	close();
    }

    private void submitChanges() {
	ValueExpression dateFromValueExpression = getValueExpression("dateFromValue");
	dateFromValueExpression.setValue(FacesContext.getCurrentInstance().getELContext(), getSearchDateFrom());
	ValueExpression dateToValueExpression = getValueExpression("dateToValue");
	dateToValueExpression.setValue(FacesContext.getCurrentInstance().getELContext(), getSearchDateTo());
	invokeEventListener();
    }

    private void close() {
	setShowDateRangePicker(false);
    }

    private void invokeEventListener() {
	MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("listener");
	if (ajaxEventListener != null)
	    ajaxEventListener.invoke(FacesContext.getCurrentInstance().getELContext(), new Object[] {});
    }

    public Date getSearchDateFrom() {
	return (Date) getStateHelper().get(SEARCH_DATE_FROM);
    }

    public void setSearchDateFrom(Date searchDateFrom) {
	this.getStateHelper().put(SEARCH_DATE_FROM, searchDateFrom);
    }

    public Date getSearchDateTo() {
	return (Date) getStateHelper().get(SEARCH_DATE_TO);
    }

    public void setSearchDateTo(Date searchDateTo) {
	this.getStateHelper().put(SEARCH_DATE_TO, searchDateTo);
    }

    public String getRangeType() {
	return (String) getStateHelper().get(RANGE_TYPE);
    }

    public void setRangeType(String rangeType) {
	this.getStateHelper().put(RANGE_TYPE, rangeType);
    }

    public boolean getShowDateRangePicker() {
	return (boolean) getStateHelper().get(SHOW_DATE_RANGE_PICKER);
    }

    public void setShowDateRangePicker(boolean showDateRangePicker) {
	this.getStateHelper().put(SHOW_DATE_RANGE_PICKER, showDateRangePicker);
    }

    public String getDateFromValueString() {
	ValueExpression dateFromValueExpression = getValueExpression("dateFromValue");
	return HijriDateService.getHijriDateString((Date) dateFromValueExpression.getValue(FacesContext.getCurrentInstance().getELContext()));
    }

    public String getDateToValueString() {
	ValueExpression dateToValueExpression = getValueExpression("dateToValue");
	return HijriDateService.getHijriDateString((Date) dateToValueExpression.getValue(FacesContext.getCurrentInstance().getELContext()));
    }
}