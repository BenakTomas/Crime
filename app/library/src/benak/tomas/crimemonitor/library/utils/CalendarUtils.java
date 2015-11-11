package benak.tomas.crimemonitor.library.utils;

import java.util.Calendar;

/**
 * A helper class containing the methods used for the conversion of the year and month numbers of different notations.
 * <p>
 * There are three notations used for the numbering of years and months.
 * <ul>
 * <li>the SPARQL notation
 * <li>the Java {@code Calendar} notation
 * <li>the CrimeMonitor notation
 * </ul>
 * 
 * <p>
 * The year numbering is the same in all three notations.
 * There is a difference in the numbering of months.
 * <p>
 * In Java {@code Calendar} notation, the months are numbered starting with 0.
 * <br />
 * In SPARQL notation, the months are numbered starting with 1.
 * <br />
 * The CrimeMonitor notation is the same as the Java {@code Calendar} notation.
 * 
 * <p>
 * Every get method in this class returns the year or month number 
 * 
 * @author Tom
 *
 */
public final class CalendarUtils
{
	/**
	 * For a year number in the CrimeMonitor notation, it returns
	 * a year number in the SPARQL notation.
	 * 
	 * @param year the year number in the CrimeMonitor notation
	 * @return the year number in the SPARQL notation
	 */
	public static int getYearForSparql(int year)
	{
		return year;
	}
	
	/**
	 * For a month number in the CrimeMonitor notation, it returns
	 * a month number in the SPARQL notation.
	 * 
	 * @param month the month number in the CrimeMonitor notation
	 * @return the month number in the SPARQL notation
	 */
	public static int getMonthForSparql(int month)
	{
		return month + 1;
	}
	
	/**
	 * For a year number in the SPARQL notation, it returns
	 * a year number in the CrimeMonitor notation.
	 * 
	 * @param year the year number in the SPARQL notation
	 * @return the year number in the CrimeMonitor notation
	 */
	public static int getYearFromSparql(int year)
	{
		return year;
	}
	
	/**
	 * For a month number in the SPARQL notation, it returns
	 * a month number in the CrimeMonitor notation.
	 * 
	 * @param month the month number in the SPARQL notation
	 * @return the month number in the CrimeMonitor notation
	 */
	public static int getMonthFromSparql(int month)
	{
		return month - 1;
	}
	
	/**
	 * For a year number in the CrimeMonitor notation, it returns
	 * a year number in the Java Calendar notation.
	 * 
	 * @param year the year number in the CrimeMonitor notation
	 * @return the year number in the Java Calendar notation
	 */
	public static int getYearForJavaCalendar(int year)
	{
		return year;
	}
	
	/**
	 * For a month number in the CrimeMonitor notation, it returns
	 * a month number in the Java Calendar notation.
	 * 
	 * @param month the month number in the CrimeMonitor notation
	 * @return the month number in the Java Calendar notation
	 */
	public static int getMonthForJavaCalendar(int month)
	{
		return month;
	}
	
	/**
	 * For a year number in the Java Calendar notation, it returns
	 * a year number in the CrimeMonitor notation.
	 * 
	 * @param year the year number in the Java Calendar notation
	 * @return the year number in the CrimeMonitor notation
	 */
	public static int getYearFromJavaCalendar(int year)
	{
		return year;
	}
	
	/**
	 * For a month number in the Java Calendar notation, it returns
	 * a month number in the CrimeMonitor notation.
	 * 
	 * @param month the month number in the Java Calendar notation
	 * @return the month number in the CrimeMonitor notation
	 */
	public static int getMonthFromJavaCalendar(int month)
	{
		return month;
	}
	
	/**
	 * For a given {@code Calendar}, it returns a year number in the CrimeMonitor notation.
	 * 
	 * @param cal the Java {@code Calendar} to extract the year number from
	 * @return the extracted year number in the CrimeMonitor notation
	 */
	public static int getYearFromJavaCalendar(Calendar cal)
	{
		return CalendarUtils.getYearFromJavaCalendar(cal.get(Calendar.YEAR));
	}
	
	/**
	 * For a given Java {@code Calendar}, it returns a month number in the CrimeMonitor notation.
	 * 
	 * @param cal the Java {@code Calendar} to extract the month number from
	 * @return the extracted month number in the CrimeMonitor notation
	 */
	public static int getMonthFromJavaCalendar(Calendar cal)
	{
		return CalendarUtils.getMonthFromJavaCalendar(cal.get(Calendar.MONTH));
	}
	
	/**
	 * Sets the year field value in the provided Java {@code Calendar}
	 * using a provided year number in the CrimeMonitor notation.
	 * 
	 * @param cal the Java {@code Calendar} to set the year field value for
	 * @param year the year number in the CrimeMonitor notation
	 */
	public static void setYearForJavaCalendar(Calendar cal, int year)
	{
		cal.set(Calendar.YEAR, CalendarUtils.getYearForJavaCalendar(year));
	}
	
	/**
	 * Sets the month field value in the provided Java {@code Calendar}
	 * using a provided month number in the CrimeMonitor notation.
	 * 
	 * @param cal the Java {@code Calendar} to set the month field value for
	 * @param month the month number in the CrimeMonitor notation
	 */
	public static void setMonthForJavaCalendar(Calendar cal, int month)
	{
		cal.set(Calendar.MONTH, CalendarUtils.getMonthForJavaCalendar(month));
	}
	
	/**
	 * Sets both the year and the month field values in the provided Java {@code Calendar}
	 * using a provided year and month number in the CrimeMonitor notation.
	 *
	 * @param year  the year number in the CrimeMonitor notation
	 * @param month  the month number in the CrimeMonitor notation
	 * @param cal  the Java {@code Calendar} to set both the year and the month field values for
	 */
	public static void setYearAndMonthForJavaCalendar(int year, int month, Calendar cal)
	{
		cal.clear();
		CalendarUtils.setYearForJavaCalendar(cal, year);
		CalendarUtils.setMonthForJavaCalendar(cal, month);
	}
	
	/**
	 * Sets the year field value in the provided Java {@code Calendar}
	 * using a provided year number in the Java Calendar notation.
	 * 
	 * @param cal the Java {@code Calendar} to set the year field value for
	 * @param year the year number in the Java Calendar notation
	 */
	public static void setJavaCalendarYearForJavaCalendar(Calendar cal, int year)
	{
		cal.set(Calendar.YEAR, year);
	}
	
	/**
	 * Sets the month field value in the provided Java {@code Calendar}
	 * using a provided month number in the Java Calendar notation.
	 * 
	 * @param cal the Java {@code Calendar} to set the year field value for
	 * @param month the month number in the Java Calendar notation
	 */
	public static void setJavaCalendarMonthForJavaCalendar(Calendar cal, int month)
	{
		cal.set(Calendar.MONTH, month);
	}
	
	/**
	 * Sets both the year and the month field values in the provided Java {@code Calendar}
	 * using a provided year and month number in the Java Calendar notation.
	 *
	 * @param year  the year number in the Java Calendar notation
	 * @param month  the month number in the Java Calendar notation
	 * @param cal  the Java {@code Calendar} to set both the year and the month field values for
	 */
	public static void setJavaCalendarYearAndMonthForJavaCalendar(int year, int month, Calendar cal)
	{
		cal.clear();
		CalendarUtils.setJavaCalendarYearForJavaCalendar(cal, year);
		CalendarUtils.setJavaCalendarMonthForJavaCalendar(cal, month);
	}
	
	/**
	 * For a given year and month it returns the time as UTC milliseconds from the epoch.

	 * @param year  the year number
	 * @param month  the month number
	 * 
	 * @return  the time as UTC milliseconds from the epoch
	 */
	public static long getTimeInMillisForCalendar(int year, int month)
	{
		Calendar cal = Calendar.getInstance();
		CalendarUtils.setYearAndMonthForJavaCalendar(year, month, cal);

		return cal.getTimeInMillis();
	}
	
	public static Calendar getCalendar(int year, int month)
	{
	    Calendar calendar = Calendar.getInstance();
	    CalendarUtils.setYearAndMonthForJavaCalendar(year, month, calendar);
	    return calendar;
	}
	
	/**
	 * Gets the number of months between the two months.
	 * <p>
	 * The first month is given in {@code Calendar} and the second month is
	 * identified with the given year and month number.
	 * <p>
	 * The resulting number {@code m} is the number of months you would need to
	 * add to the Java {@code Calendar} with {@code cal.add(Calendar.MONTH, m)}
	 * so that
	 * {@code cal.get(Calendar.YEAR) == year & cal.get(Calendar.MONTH) == month}.
	 * 
	 * @param cal
	 *            the calendar to calculate the month difference for
	 * @param year
	 *            the year number
	 * @param month
	 *            the month number
	 * 
	 * @return the number of months between the two months
	 */
	public static int getMonthsBetweenDates(Calendar cal, int year, int month)
	{
		return (year - CalendarUtils.getYearFromJavaCalendar(cal)) * 12
				+ (month - CalendarUtils.getMonthFromJavaCalendar(cal));
	}

}
