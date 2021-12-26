package com.code.ui.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;

import com.code.dal.orm.setup.HijriCalendar;
import com.code.exceptions.BusinessException;
import com.code.services.log4j.Log4j;
import com.code.services.util.HijriDateService;

@FacesComponent(value = "advancedHijriDateNew")
public class AdvancedHijriDate extends UINamingContainer {
	private boolean hijriDate;

	private int[] yearsItem;
	private List<SelectItem> monthsItem;

	private final static String MONTH_LIST = "monthList";
	private final static String YEAR_LIST = "yearList";
	private final static String MONTH_LABEL = "month";
	private final static String YEAR_LABEL = "year";
	private final static String DAY_LABEL = "day";
	private final static String SYS_DATE = "sysDate";
	private final static String DRAWING_DAYS = "drawingDays";
	private final static String CALENDAR_VISIBILITY = "calendarVisibility";
	private final static String DRAWING_STYLES = "styles";
	private final static String SELECTED_VALUE = "selectedValue";
	private final static String SELECTED_VALUE_DAY = "selectedValueDay";
	private final static String SELECTED_VALUE_MONTH = "selectedValueMonth";
	private final static String SELECTED_VALUE_YEAR = "selectedValueYear";
	private final static String OLD_SELECTED_VALUE = "oldSelectedValue";
	private final static String OLD_SELECTED_VALUE_DAY = "oldSelectedValueDay";
	private final static String OLD_SELECTED_VALUE_MONTH = "oldSelectedValueMonth";
	private final static String OLD_SELECTED_VALUE_YEAR = "oldSelectedValueYear";

	private final static String VALUE_DEFAULT_STYLE = "custom_calendar_date_value";
	private final static String VALUE_SELECTED_STYLE = "custom_calendar_date_selected_value";
	private final static String VALUE_VACATION_STYLE = "custom_calendar_date_vaction_value";
	private final static String VALUE_NOT_USED_STYLE = "custom_calendar_date_not_used";
	private final static String VALUE_CURRENT_DATE_STYLE = "custom_calendar_date_current";
	private ResourceBundle messages = ResourceBundle.getBundle("com.code.resources.messages", new Locale("ar"));
	private String hijriError;

	private String[] hijriMonths = new String[] { getMessage("label_moharm"), getMessage("label_safr"), getMessage("label_rabieAwl"), getMessage("label_rabieTani"), getMessage("label_gamadAwl"), getMessage("label_gamadTani"), getMessage("label_ragb"), getMessage("label_shaban"), getMessage("label_ramdan"), getMessage("label_shwal"), getMessage("label_zwAlqada"), getMessage("label_zwAlhaga") };

	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);

		hijriDate = true;
		setVisibleCalendar(false);

		String sysDate = null;
		try {
			sysDate = HijriDateService.getHijriSysDateString();
		} catch (Exception e) {
			sysDate = "01/01/" + getStartYear();
		}

		setSysDateValue(sysDate);//

		Date selectedDate = (Date) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{cc.attrs.value}", Object.class);
		SimpleDateFormat sdf = new SimpleDateFormat("mm/MM/yyyy");
		String selectedDateValue = null;
		String selectedDateValueDay = null;
		String selectedDateValueMonth = null;
		String selectedDateValueYear = null;

		if (selectedDate != null) {
			selectedDateValue = sdf.format(selectedDate);
			String[] parts = selectedDateValue.split("/");
			selectedDateValueDay = parts[0]; // day
			selectedDateValueMonth = parts[1]; // month
			selectedDateValueYear = parts[2]; // year

		} else if (selectedDate == null) {
			selectedDateValue = null;
			// selectedDateValue = sysDate;
			// String[] parts = sysDate.split("/");
			// selectedDateValueDay = parts[0]; // day
			selectedDateValueDay = null;
			// selectedDateValueMonth = parts[1]; // month
			selectedDateValueMonth = null;
			// selectedDateValueYear=parts[2]; //year
			selectedDateValueYear = null;
		}
		// selectedDateValue = selectedDate == null ? sysDate : sdf.format(selectedDate);

		setSelectedValue(selectedDateValue);
		setSelectedValueDay(selectedDateValueDay);
		setSelectedValueMonth(selectedDateValueMonth);
		setSelectedValueYear(selectedDateValueYear);
		// if(selectedDateValue!=null){
		this.drawCalendar(selectedDateValue);
		// }
		this.initMonthYears();
	}

	private void initMonthYears() {
		yearsItem = new int[getEndYear() - getStartYear() + 1];
		for (int i = 0; i < yearsItem.length; i++) {
			yearsItem[i] = getStartYear() + i;
		}

		setYearsItem(yearsItem);

		monthsItem = new ArrayList<SelectItem>();
		for (int i = 0; i < hijriMonths.length; i++) {
			monthsItem.add(new SelectItem("" + (i + 1), hijriMonths[i]));
		}

		setMonthsItem(monthsItem);
	}

	public void drawCalendar() {
		DecimalFormat df = new DecimalFormat("00");
		String selectedValue = this.getSelectedValue();
		if (selectedValue == null || selectedValue.equals("")) {

			String[] sysDateTemp = getSysDateValue().split("/");
			@SuppressWarnings("unused")
			int sysDayTemp = Integer.parseInt(sysDateTemp[0]);
			int sysMonthTemp = Integer.parseInt(sysDateTemp[1]);
			int sysYearTemp = Integer.parseInt(sysDateTemp[2]);
			// System.out.println(getCurrentMonth());
			// System.out.println(getCurrentYear());
			selectedValue = "01/" + df.format(sysMonthTemp) + "/" + sysYearTemp;
			setSelectedValue(selectedValue);
		}

		drawCalendar(selectedValue);
	}

	private void drawCalendar(int month, int year) {
		DecimalFormat df = new DecimalFormat("00");
		drawCalendar("01/" + df.format(month) + "/" + year);
	}

	public void drawCalendar(String date) {
		List<List<Integer>> drawingDays = new ArrayList<List<Integer>>();
		String[][] styles = new String[6][7];

		String sysDate = this.getSysDateValue();
		if (date == null || date.equals("")) {
			date = sysDate;
			// date =null;
			setSelectedValue(date);
		}

		String[] sysDateValues = sysDate.split("/");
		int sysDateDay = Integer.parseInt(sysDateValues[0]);
		int sysDateMonth = Integer.parseInt(sysDateValues[1]);
		int sysDateYear = Integer.parseInt(sysDateValues[2]);

		String[] dateValues = date.split("/");

		int currentMonth = Integer.parseInt(dateValues[1]);
		int currentYear = Integer.parseInt(dateValues[2]);

		setCurrentMonth(currentMonth);
		setCurrentYear(currentYear);

		HijriCalendar previousMonth = null;

		if (currentMonth == 1) {
			try {
				previousMonth = HijriDateService.getHijriCalendar(12, currentYear - 1);
			} catch (BusinessException e) {
				Log4j.traceErrorException(AdvancedHijriDate.class, e, "AdvancedHijriDate");
			}
		} else {
			try {
				previousMonth = HijriDateService.getHijriCalendar(currentMonth - 1, currentYear);
			} catch (BusinessException e) {
				Log4j.traceErrorException(AdvancedHijriDate.class, e, "AdvancedHijriDate");
			}
		}

		HijriCalendar currentMonthObject = null;
		try {
			currentMonthObject = HijriDateService.getHijriCalendar(currentMonth, currentYear);
		} catch (BusinessException e1) {
			Log4j.traceErrorException(AdvancedHijriDate.class, e1, "AdvancedHijriDate");
		}

		int previousMonthLength = (previousMonth != null ? previousMonth.getHijriMonthLength() : 30);
		int sysMonthLength = (currentMonthObject != null ? currentMonthObject.getHijriMonthLength() : 30);

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Calendar today = Calendar.getInstance();
			today.setTime(sdf.parse(HijriDateService.hijriToGregDateString(date)));

			today.add(Calendar.DAY_OF_MONTH, -1 * Integer.parseInt(dateValues[0]) + 1); // reset to 1 value in hijri

			int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);

			int value = dayOfWeek == 1 ? 1 : (previousMonthLength - dayOfWeek + 2);

			String style = VALUE_NOT_USED_STYLE;
			String[] selectedDateValue = getSelectedValue().split("/");// HERE
			int selectedDayValue = Integer.parseInt(selectedDateValue[0]);
			int selectedMonthValue = Integer.parseInt(selectedDateValue[1]);
			int selectedYearValue = Integer.parseInt(selectedDateValue[2]);
			for (int i = 0; i < 6; i++) {
				List<Integer> row = new ArrayList<Integer>();
				for (int j = 0; j < 7; j++) {
					row.add(value);

					if (i == 0 && j < dayOfWeek - 1)
						style = VALUE_NOT_USED_STYLE;
					else if (i * 7 + j > dayOfWeek + sysMonthLength - 2)
						style = VALUE_NOT_USED_STYLE;
					else if (value == sysDateDay && currentMonth == sysDateMonth && currentYear == sysDateYear)
						style = VALUE_CURRENT_DATE_STYLE;
					else if (value == selectedDayValue && currentMonth == selectedMonthValue && currentYear == selectedYearValue)
						style = VALUE_SELECTED_STYLE;
					else if (j >= 5)
						style = VALUE_VACATION_STYLE;
					else
						style = VALUE_DEFAULT_STYLE;

					styles[i][j] = style;

					if ((i == 0 && value == previousMonthLength) || (i > 0 && value == sysMonthLength))
						value = 1;
					else
						value++;
				}
				drawingDays.add(row);
			}
		} catch (Exception e) {
			Log4j.traceErrorException(AdvancedHijriDate.class, e, "AdvancedHijriDate");
		}

		setStyles(styles);
		setDrawingDays(drawingDays);
	}

	public void adjustSelectedStyles() {
		String newSelectedValue = this.getSelectedValue();// problem
		String oldSelectedValue = this.getOldSelectedValue();

		String[][] styles = getStyles();
		String[] sysDate = getSysDateValue().split("/");
		int sysDay = Integer.parseInt(sysDate[0]);
		int sysMonth = Integer.parseInt(sysDate[1]);
		int sysYear = Integer.parseInt(sysDate[2]);
		// System.out.println("oldSelectedValue: "+oldSelectedValue);
		// System.out.println("newSelectedValue :"+newSelectedValue);

		if (oldSelectedValue == null || oldSelectedValue.equals("") || oldSelectedValue.equals(newSelectedValue))
			return;

		String[] oldSelectedValues = oldSelectedValue.split("/");
		int oldDay = Integer.parseInt(oldSelectedValues[0]);
		// this.setDay(oldDay);
		int oldMonth = Integer.parseInt(oldSelectedValues[1]);
		// this.setMonth(oldMonth);
		int oldYear = Integer.parseInt(oldSelectedValues[2]);
		// this.setYear(oldYear);

		String[] newDateValues = newSelectedValue.split("/");
		int newDay = Integer.parseInt(newDateValues[0]);
		int newMonth = Integer.parseInt(newDateValues[1]);
		int newYear = Integer.parseInt(newDateValues[2]);

		if (oldMonth != newMonth && oldYear != newYear) {
			adjustNewSelectedValueStyle(newDay);
			// this.setDay(newDay);
			// this.setMonth(newMonth);
			// this.setYear(newYear);

			return;
		}

		boolean breakFlag = false;
		for (int i = 0; i < styles.length; i++) {
			for (int j = 0; j < styles[i].length; j++) {
				if (styles[i][j].equals(VALUE_SELECTED_STYLE)) {
					if (j == 5 || j == 6)
						styles[i][j] = VALUE_VACATION_STYLE;
					else if (oldDay == sysDay && oldMonth == sysMonth && oldYear == sysYear)
						styles[i][j] = VALUE_CURRENT_DATE_STYLE;
					else
						styles[i][j] = VALUE_DEFAULT_STYLE;

					breakFlag = true;
					break;
				}
			}
			if (breakFlag)
				break;
		}

		adjustNewSelectedValueStyle(newDay);
	}

	private void adjustNewSelectedValueStyle(int newDay) {
		try {
			List<List<Integer>> drawingDays = getDrawingDays();
			int beginningDay = drawingDays.get(0).get(0);
			int newDateSum = newDay;
			if (beginningDay != 1) {
				beginningDay = 30 - beginningDay;
				if (drawingDays.get(0).get(beginningDay) == 1)
					beginningDay--;

				newDateSum += beginningDay;
			} else
				newDateSum--;

			int i = (int) Math.floor(newDateSum / 7.0);
			int j = newDateSum % 7;

			String[][] styles = getStyles();
			styles[i][j] = VALUE_SELECTED_STYLE;

			setStyles(styles);
		} catch (Exception e) {
			Log4j.traceErrorException(AdvancedHijriDate.class, e, "AdvancedHijriDate");
		}
	}

	public void isValidDate() {
		String date = this.getSelectedValue();
		int currentMonth = getCurrentMonth();
		int currentYear = getCurrentYear();
		String[] dateValues = null;
		try {
			dateValues = date.split("/");
			if (date == null || date.equals("") || !date.contains("/") || dateValues[2].length() != 4)
				throw new Exception("Invalid Date");
			else {
				DecimalFormat df = new DecimalFormat("00");
				String day = df.format(Integer.parseInt(dateValues[0]));
				String month = df.format(Integer.parseInt(dateValues[1]));
				date = day + "/" + month + "/" + dateValues[2];

				if (day.equals("30") && !HijriDateService.isValidHijriDate(HijriDateService.getHijriDate(date)))
					throw new Exception("Invalid Date");
			}
		} catch (Exception e) {
			date = this.getSysDateValue();
		}

		setSelectedValueOnly(date);

		if (isVisibleCalendar()) {
			dateValues = date.split("/");
			if (Integer.parseInt(dateValues[1]) == currentMonth && Integer.parseInt(dateValues[2]) == currentYear) {
				adjustSelectedStyles();
			} else
				drawCalendar(date);
		}
	}

	public void changeYearOrMonth() {
		int currentMonth = this.getCurrentMonth();
		int currentYear = this.getCurrentYear();
		// this.setSelectedValueMonth(this.getCurrentMonth().toString());//To Change i/p month
		// this.setSelectedValueYear(this.getCurrentYear().toString());//To Change i/p year

		drawCalendar(currentMonth, currentYear);
	}

	public void previousYear() {
		int currentMonth = this.getCurrentMonth();
		int currentYear = this.getCurrentYear();
		currentYear--;

		drawCalendar(currentMonth, currentYear);
	}

	public void previousMonth() {
		int currentMonth = this.getCurrentMonth();
		int currentYear = this.getCurrentYear();
		if (currentMonth == 1) {
			currentMonth = 12;
			currentYear--;
		} else {
			currentMonth--;
		}

		drawCalendar(currentMonth, currentYear);
	}

	public void nextMonth() {
		int currentMonth = this.getCurrentMonth();
		int currentYear = this.getCurrentYear();
		if (currentMonth == 12) {
			currentMonth = 1;
			currentYear++;
		} else {
			currentMonth++;
		}

		drawCalendar(currentMonth, currentYear);
	}

	public void nextYear() {
		int currentMonth = this.getCurrentMonth();
		int currentYear = this.getCurrentYear();
		currentYear++;

		drawCalendar(currentMonth, currentYear);
	}

	public void gotoMonthYear() {
		drawCalendar(this.getCurrentMonth(), this.getCurrentYear());
	}

	public void today() {
		String sysDate = this.getSysDateValue();
		this.setSelectedValue(sysDate);
		this.setSelectedValue(sysDate);
		String[] parts = sysDate.split("/");
		this.setSelectedValueDay(parts[0]); // day
		this.setSelectedValueMonth(parts[1]); // month
		this.setSelectedValueYear(parts[2]); // year
		String[] sysDateValue = sysDate.split("/");
		drawCalendar(Integer.parseInt(sysDateValue[1]), Integer.parseInt(sysDateValue[2]));
	}

	public void selectDateValue() {
		if (((String) this.getSelectedValue()).equals("")) {
			this.updateSubmittedValue(false);
			this.setVisibleCalendar(false);
		} else {

			try {
				boolean isValid = HijriDateService.isValidHijriDate(HijriDateService.getHijriDate(this.getSelectedValue()));
				if (isValid) {
					this.adjustSelectedStyles();
					this.updateSubmittedValue(false);
					this.setVisibleCalendar(false);
					hijriError = null;
				} else {
					this.updateSubmittedValue(true);
					this.setVisibleCalendar(false);
					String error = getMessage("error_invalidHijriDate");
					hijriError = error;
					// FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, error, error));
				}// isValid
			} catch (BusinessException e) {
				Log4j.traceErrorException(AdvancedHijriDate.class, e, "AdvancedHijriDate");
			}

		}
	}

	public void updateSubmittedValue(Boolean hijriDateErrorFlag) {// the solution
		/*
		 * try{ Object dateValue = null; Calendar calendar = Calendar.getInstance(getFacesContext().getViewRoot().getLocale()); calendar.set(Calendar.DAY_OF_MONTH, 1); System.out.println(this.getDay()); calendar.set(Calendar.MINUTE, this.getDay()); System.out.println(this.getMonth()); calendar.set(Calendar.MONTH,this.getMonth() - 1); System.out.println(this.getYear()); calendar.set(Calendar.YEAR,this.getYear()); dateValue = calendar.getTime();
		 * 
		 * ValueExpression valueExpression = getValueExpression("value");// FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{cc.attrs.value}", Object.class); valueExpression.setValue(FacesContext.getCurrentInstance().getELContext(), dateValue);//tmpSelectedHijDate); } catch(Exception e){ e.printStackTrace(); }
		 */
		if (!hijriDateErrorFlag) {
			if (this.getSelectedValue() == null)
				return;
			Date tmpSelectedHijDate = HijriDateService.getHijriDate((String) this.getSelectedValue());
			try {

				ValueExpression valueExpression = getValueExpression("value");// FacesContext.getCurrentInstance().getApplication().getExpressionFactory().createValueExpression(FacesContext.getCurrentInstance().getELContext(), "#{cc.attrs.value}", Object.class);
				valueExpression.setValue(FacesContext.getCurrentInstance().getELContext(), tmpSelectedHijDate);
			} catch (Exception e) {
				Log4j.traceErrorException(AdvancedHijriDate.class, e, "AdvancedHijriDate");
			}
		} else if (hijriDateErrorFlag) {
			ValueExpression valueExpression = getValueExpression("value");
			valueExpression.setValue(FacesContext.getCurrentInstance().getELContext(), null);
		}

	}

	public void ajaxEventListener(AjaxBehaviorEvent event) {
		MethodExpression ajaxEventListener = (MethodExpression) getAttributes().get("listener");
		if (ajaxEventListener != null)
			ajaxEventListener.invoke(FacesContext.getCurrentInstance().getELContext(), new Object[] { event });
	}

	public String getHijriDateTitle() {
		try {
			return hijriMonths[getCurrentMonth() - 1] + " - " + getCurrentYear();
		} catch (Exception e) {
			return getCurrentYear() + "";
		}
	}

	public void setHijriDate(boolean hijriDate) {
		this.hijriDate = hijriDate;
	}

	public boolean isHijriDate() {
		return hijriDate;
	}

	public void setCurrentMonth(Integer currentMonth) {
		if (currentMonth != null) {
			this.getStateHelper().put(MONTH_LABEL, currentMonth);
		}
	}

	public Integer getCurrentMonth() {
		return (Integer) getStateHelper().get(MONTH_LABEL);
	}

	public void setCurrentYear(Integer currentYear) {
		if (currentYear != null) {
			this.getStateHelper().put(YEAR_LABEL, currentYear);
		}
	}

	public Integer getCurrentYear() {
		return (Integer) getStateHelper().get(YEAR_LABEL);
	}

	public void setStyles(String[][] styles) {
		this.getStateHelper().put(DRAWING_STYLES, styles);
	}

	public String[][] getStyles() {
		return (String[][]) getStateHelper().get(DRAWING_STYLES);
	}

	public void setDrawingDays(List<List<Integer>> drawingDays) {
		this.getStateHelper().put(DRAWING_DAYS, drawingDays);
	}

	@SuppressWarnings("unchecked")
	public List<List<Integer>> getDrawingDays() {
		return (List<List<Integer>>) getStateHelper().get(DRAWING_DAYS);
	}

	public void changeCalendarVisibility() {
		boolean visiblity = !this.isVisibleCalendar();
		if (visiblity == true)
			drawCalendar();

		this.setVisibleCalendar(visiblity);
	}

	public void setVisibleCalendar(boolean visibleCalendar) {
		this.getStateHelper().put(CALENDAR_VISIBILITY, visibleCalendar);
	}

	public boolean isVisibleCalendar() {
		return (Boolean) getStateHelper().get(CALENDAR_VISIBILITY);
	}

	public void setSelectedValue(String selectedValue) {
		this.getStateHelper().put(OLD_SELECTED_VALUE, getSelectedValue());

		this.getStateHelper().put(SELECTED_VALUE, selectedValue);
	}

	public void setSelectedValueDay(String selectedValueDay) {
		this.getStateHelper().put(OLD_SELECTED_VALUE_DAY, getSelectedValue());

		this.getStateHelper().put(SELECTED_VALUE_DAY, selectedValueDay);
	}

	public void setSelectedValueMonth(String selectedValueMonth) {
		this.getStateHelper().put(OLD_SELECTED_VALUE_MONTH, getSelectedValue());

		this.getStateHelper().put(SELECTED_VALUE_MONTH, selectedValueMonth);
	}

	public void setSelectedValueYear(String selectedValueYear) {
		this.getStateHelper().put(OLD_SELECTED_VALUE_YEAR, getSelectedValue());

		this.getStateHelper().put(SELECTED_VALUE_YEAR, selectedValueYear);
	}

	public void setSelectedValueOnly(String selectedValue) {
		this.getStateHelper().put(SELECTED_VALUE, selectedValue);
	}

	public String getSelectedValue() {
		return (String) getStateHelper().get(SELECTED_VALUE);
	}

	public String getSelectedValueDay() {
		return (String) getStateHelper().get(SELECTED_VALUE_DAY);
	}

	public String getSelectedValueMonth() {
		return (String) getStateHelper().get(SELECTED_VALUE_MONTH);
	}

	public String getSelectedValueYear() {
		return (String) getStateHelper().get(SELECTED_VALUE_YEAR);
	}

	public String getOldSelectedValue() {
		return (String) getStateHelper().get(OLD_SELECTED_VALUE);
	}

	public void setSysDateValue(String sysDate) {
		this.getStateHelper().put(SYS_DATE, sysDate);
	}

	public String getSysDateValue() {
		return (String) this.getStateHelper().get(SYS_DATE);
	}

	public int getStartYear() {
		// return EnvironmentUtilities.getStartHijriYear();
		return 1350;
	}

	public int getEndYear() {
		// return EnvironmentUtilities.getEndHijriYear();
		return 1443;
	}

	public void setYearsItem(int[] yearsItem) {
		this.getStateHelper().put(YEAR_LIST, yearsItem);
	}

	public int[] getYearsItem() {
		return (int[]) this.getStateHelper().get(YEAR_LIST);
	}

	public void setMonthsItem(List<SelectItem> monthsItem) {
		this.getStateHelper().put(MONTH_LIST, monthsItem);
	}

	@SuppressWarnings("unchecked")
	public List<SelectItem> getMonthsItem() {
		return (List<SelectItem>) this.getStateHelper().get(MONTH_LIST);
	}

	public void setDay(Integer day) {
		this.getStateHelper().put(DAY_LABEL, day);
	}

	public Integer getDay() {
		return (Integer) getStateHelper().get(DAY_LABEL);
	}

	public void setMonth(Integer month) {
		this.getStateHelper().put(MONTH_LABEL, month);
	}

	public Integer getMonth() {
		return (Integer) getStateHelper().get(MONTH_LABEL);
	}

	public void setYear(Integer year) {
		getStateHelper().put(YEAR_LABEL, year);
	}

	public Integer getYear() {
		return (Integer) getStateHelper().get(YEAR_LABEL);
	}

	public String getMessage(String key) {
		return messages.getString(key);
	}

	public void setHijriError(String hijriError) {
		this.hijriError = hijriError;
	}

	public String getHijriError() {
		return hijriError;
	}

}
