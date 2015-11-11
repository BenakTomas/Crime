package benak.tomas.crimemonitor.client.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import benak.tomas.crimemonitor.library.utils.CalendarUtils;

/**
 * A class used to render the date and time labels.
 * 
 * @author Tom
 */
public class Utils
{
	/**
	 * For a given crime data interval it returns a textual label describing that interval.
	 * <p>
	 * This is typically used by an activity that needs to display a textual representation of the currently selected
	 * crime data interval in its title.
	 * 
	 * @param startYear the initial year of the crime data interval
	 * @param startMonth the initial month of the crime data interval
	 * @param endYear the terminal year of the crime data interval
	 * @param endMonth the terminal month of the crime data interval
	 * @return the textual label describing the given crime data interval
	 */
	public static String getCrimeIntervalString(int startYear, int startMonth, int endYear, int endMonth)
	{
		return
				String.format("%s - %s",
						getCrimeIntervalBoundString(startYear, startMonth),
						getCrimeIntervalBoundString(endYear, endMonth));
	}
	
	/**
	 * For a given number of year and month it returns the equivalent {@code Date}.
	 * 
	 * @param year the year number
	 * @param month the month number 
	 * 
	 * @return the equivalent date for the given month within the given year 
	 */
	private static Date getDate(int year, int month)
	{
		Calendar cal = Calendar.getInstance();
		CalendarUtils.setYearAndMonthForJavaCalendar(year, month, cal);
		
		return cal.getTime();
	}
	
	/**
	 * For a given year and month, it returns a textual label describing that particular month in the given year.
	 * 
	 * @param year the year number
	 * @param month the number of month within the given year
	 * 
	 * @return the textual label describing a particular month in a given year
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCrimeIntervalBoundString(int year, int month)
	{
		return new SimpleDateFormat("MMM yyyy").format(Utils.getDate(year, month));
	}
}
