package com.code.services.util;

import com.code.dal.DataAccess;
import com.code.dal.orm.setup.HijriCalendar;
import com.code.enums.QueryNamesEnum;
import com.code.exceptions.BusinessException;
import com.code.services.BaseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class HijriDateService extends BaseService {
	private final static String DATE_FORMAT = "mm/MM/yyyy";
	private final static String DATE_FORMAT_LTR = "yyyy/MM/mm";
	
	private HijriDateService() {
	}

	public static String getDateFormat(){
		return DATE_FORMAT;
	}
	
	public static Date getHijriSysDate() throws BusinessException {
		try {
			return gregToHijriDate(new Date());
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static String getHijriSysDateString() throws BusinessException {
		try {
			return getHijriDateString(gregToHijriDate(new Date()));
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static String getHijriDateString(Date hijriDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return ((hijriDate == null) ? null : sdf.format(hijriDate));
	}
	
	public static String getLTRHijriDateString(Date hijriDate) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_LTR);
		return ((hijriDate == null) ? null : sdf.format(hijriDate));
	}

	public static Date getHijriDate(String hijriDateString) {
		if (hijriDateString == null || hijriDateString.equals(""))
			return null;
		else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				return sdf.parse(hijriDateString);
			} catch (ParseException e) {
				return null;
			}
		}
	}

	public static Date getExternalHijriDate(String date) {
		try {
			date = date.substring(0, 2) + "/" + date.substring(2, 4) + "/" + date.substring(4, 8);
			return getHijriDate(date);

		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isValidHijriDate(Date hijriDate) throws BusinessException {
		try {
			String[] splittedDate = getHijriDateString(hijriDate).split("/");

			if (Integer.parseInt(splittedDate[0]) <= 29)
				return true;

			HijriCalendar tempCal = getHijriCalendar(Integer.parseInt(splittedDate[1]), Integer.parseInt(splittedDate[2]));

			if (tempCal.getHijriMonthLength() >= Integer.parseInt(splittedDate[0]))
				return true;

			return false;
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static Date addSubHijriDays(Date inputHijriDate, Integer noOfDays, boolean... flags) throws BusinessException {
		return getHijriDate(addSubStringHijriDays(getHijriDateString(inputHijriDate), noOfDays, flags));
	}

	public static String addSubStringHijriDays(String inputHijriDate, Integer noOfDays, boolean... flags) throws BusinessException {
		boolean thirtyFlag = false;
		if (flags != null && flags.length > 0)
			thirtyFlag = flags[0];

		try {
			String[] splittedDate;
			int[] date = new int[3];
			try {
				splittedDate = inputHijriDate.split("/");
				for (int j = 0; j <= 2; j++) {
					date[j] = Integer.parseInt(splittedDate[j]);
				}
			} catch (Exception ex) {
				throw new BusinessException("error_general");
			}

			if (noOfDays >= 0) {
				while (noOfDays > 0) {
					HijriCalendar tempCal;
					if (thirtyFlag) {
						tempCal = new HijriCalendar();
						tempCal.setHijriMonthLength(30);
					} else {
						tempCal = getHijriCalendar(date[1], date[2]);
					}

					if (tempCal.getHijriMonthLength() >= date[0] + noOfDays) {
						date[0] += noOfDays;
						noOfDays = 0;
					} else {
						if (date[1] == 12) {
							date[2]++;
							date[1] = 0;
						}
						date[1]++;
						noOfDays = noOfDays - (tempCal.getHijriMonthLength() - date[0]) - 1;
						date[0] = 1;
					}
				}
			} else {
				while (noOfDays < 0) {
					if (date[0] > (-1 * noOfDays)) {
						date[0] += noOfDays;
						noOfDays = 0;
					} else {
						if (date[1] == 1) {
							date[2]--;
							date[1] = 13;
						}
						date[1]--;

						HijriCalendar tempCal;
						if (thirtyFlag) {
							tempCal = new HijriCalendar();
							tempCal.setHijriMonthLength(30);
						} else {
							tempCal = getHijriCalendar(date[1], date[2]);
						}

						date[0] += tempCal.getHijriMonthLength();
					}
				}
			}

			String newHijriDate = (date[0] < 10 ? "0" + date[0] : "" + date[0]) + "/" + (date[1] < 10 ? "0" + date[1] : "" + date[1]) + "/" + date[2];
			return newHijriDate;
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static Date addSubHijriMonthsDays(Date inputHijriDate, int noOfMonths, int noOfDays) throws BusinessException {
		return getHijriDate(addSubStringHijriMonthsDays(getHijriDateString(inputHijriDate), noOfMonths, noOfDays));
	}

	public static String addSubStringHijriMonthsDays(String inputHijriDate, int noOfMonths, int noOfDays) throws BusinessException {
		if (inputHijriDate != null && !inputHijriDate.isEmpty()) {
			String monthsDateString = addSubStringHijriDays(inputHijriDate, noOfMonths * 30, true);
			if (!isValidHijriDate(getHijriDate(monthsDateString))) {
				monthsDateString = addSubStringHijriDays(monthsDateString, -1, true);
			}
			return addSubStringHijriDays(monthsDateString, noOfDays);
		} else
			return null;
	}

	public static Date hijriToGregDate(Date date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String gregDate = hijriToGregDateString(getHijriDateString(date));
			return sdf.parse(gregDate);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String hijriToGregDateString(String date) {
		try {
			String[] splittedDate = date.split("/");
			HijriCalendar tempCal = getHijriCalendar(Integer.parseInt(splittedDate[1]), Integer.parseInt(splittedDate[2]));

			DateTime hijriMonthEndGregDate = new DateTime(tempCal.getHijriMonthEndGregDate().getTime());
			DateTime gregDate = hijriMonthEndGregDate.minusDays(tempCal.getHijriMonthLength() - Integer.parseInt(splittedDate[0]));

			Date result = new Date(gregDate.getMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return sdf.format(result);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date gregToHijriDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return getHijriDate(gregToHijriDateString(sdf.format(date)));
	}

	public static String gregToHijriDateString(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date gregDate = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(gregDate);
			cal.add(Calendar.DAY_OF_MONTH, 31);

			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_FROM_DATE", date);
			qParams.put("P_TO_DATE", sdf.format(cal.getTime()));
			HijriCalendar endCal = DataAccess.executeNamedQuery(HijriCalendar.class, QueryNamesEnum.HIJRI_CALENDER_GET_GREG_DATES.getCode(), qParams).get(0);

			DateTime endGregDate = new DateTime(endCal.getHijriMonthEndGregDate().getTime());
			DateTime startGregDate = new DateTime(gregDate.getTime());
			Days d = Days.daysBetween(startGregDate, endGregDate);
			int daysDiff = d.getDays();

			String monthYear = "/" + (endCal.getHijriMonth() < 10 ? "0" + endCal.getHijriMonth() : "" + endCal.getHijriMonth()) + "/" + endCal.getHijriYear();

			int variance = endCal.getHijriMonthLength() - daysDiff;
			if (variance <= 0) {
				variance--;
				return addSubStringHijriDays("01" + monthYear, variance);
			}

			return (variance < 10 ? "0" + variance : "" + variance) + monthYear;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the difference between two hijri dates formatted as java Date
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int hijriDateDiff(Date startDate, Date endDate) {
		return hijriDateStringDiff(getHijriDateString(startDate), getHijriDateString(endDate));
	}

	/**
	 * Get the difference between two hijri dates formatted as String
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int hijriDateStringDiff(String startDate, String endDate) {
		try {
			String gregStartDate = hijriToGregDateString(startDate);
			String gregEndDate = hijriToGregDateString(endDate);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			return gregDateDiff(sdf.parse(gregStartDate), sdf.parse(gregEndDate));
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Get the difference between two Greg dates formatted as java Date
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int gregDateDiff(Date startDate, Date endDate) {
		DateTime startGregDate = new DateTime(startDate.getTime());
		DateTime endGregDate = new DateTime(endDate.getTime());
		Days d = Days.daysBetween(startGregDate, endGregDate);
		return d.getDays();
	}

	public static HijriCalendar getHijriCalendar(int hijriMonth, int hijriYear) throws BusinessException {
		try {
			Map<String, Object> qParams = new HashMap<String, Object>();
			qParams.put("P_HIJRI_MONTH", hijriMonth);
			qParams.put("P_HIJRI_YEAR", hijriYear);
			return DataAccess.executeNamedQuery(HijriCalendar.class, QueryNamesEnum.HIJRI_CALENDER_GET_HIJRI_CALENDAR.getCode(), qParams).get(0);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	/**
	 * Check if date check is between Date start and end or not
	 * 
	 * @param start
	 * @param end
	 * @param check
	 * @return ture in case of between; false otherwise.
	 */
	public static boolean isDateBetween(Date start, Date end, Date check) {
		return check.compareTo(start) * end.compareTo(check) >= 0;
	}

	public static int getHijriMonthLength(int hijriMonth, int hijriYear) throws BusinessException {
		try {
			HijriCalendar hijriCalendar = getHijriCalendar(hijriMonth, hijriYear);

			return hijriCalendar.getHijriMonthLength();
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static int getHijriDateDay(Date date) throws BusinessException {
		try {
			String dateString = HijriDateService.getHijriDateString(date);
			String[] dateArray = dateString.split("/");
			return Integer.parseInt(dateArray[0]);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static int getHijriDateMonth(Date date) throws BusinessException {
		try {
			String dateString = HijriDateService.getHijriDateString(date);
			String[] dateArray = dateString.split("/");
			return Integer.parseInt(dateArray[1]);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static int getHijriDateYear(Date date) throws BusinessException {
		try {
			String dateString = HijriDateService.getHijriDateString(date);
			String[] dateArray = dateString.split("/");
			return Integer.parseInt(dateArray[2]);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static int getHijriStringDay(String date) throws BusinessException {
		try {
			String[] dateArray = date.split("/");
			return Integer.parseInt(dateArray[0]);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static int getHijriStringMonth(String date) throws BusinessException {
		try {
			String[] dateArray = date.split("/");
			return Integer.parseInt(dateArray[1]);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static int getHijriStringYear(String date) throws BusinessException {
		try {
			String[] dateArray = date.split("/");
			return Integer.parseInt(dateArray[2]);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	public static Date floorInvalidHijriDateToValidHijriDate(Date date) throws BusinessException {

		if (!HijriDateService.isValidHijriDate(date)) {
			return HijriDateService.addSubHijriDays(date, -1);
		} else
			return date;
	}
	
	public static String getHijriYearsString(int gregYear) throws BusinessException{
		Calendar calendar = Calendar.getInstance();
		calendar.set(gregYear, 0, 1);
		int year1 = getHijriDateYear(gregToHijriDate(calendar.getTime()));
		calendar.set(gregYear, 11, 31);
		int year2 = getHijriDateYear(gregToHijriDate(calendar.getTime()));
		return year2 + "/" + year1;
	}
}