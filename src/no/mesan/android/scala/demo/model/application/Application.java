package no.mesan.android.scala.demo.model.application;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Class to deal with common methods
 * 
 * @author Thomas Pettersen
 *
 */
public class Application {
	
	private static ConnectivityManager conManager;
	
	private final static String[] months = { "januar", "februar", "mars", "april", "mai", "juni", "juli", "august", "september", "oktober", "november", "desember" };

	/**
	 * Hides the keyboard when view loses focus
	 * 
	 * @param context
	 * @param view
	 */
	public static void hideKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * Formats a Date to a time unit denoting the date's time different 
	 * from current time
	 * 
	 * @param date
	 * @return String 
	 */
	public synchronized static String formatDateToTimeDiff(Date eventDate) {
		StringBuilder stringBuilder = new StringBuilder();
		
		Calendar eventCalendar = Calendar.getInstance();
		eventCalendar.setTime(eventDate);
		eventCalendar.set(Calendar.MONTH, (eventCalendar.get(Calendar.MONTH)));

		Calendar currentTime = Calendar.getInstance();
		currentTime.setTime(new Date());
		
		int hour = eventDate.getHours();
		String textHours = hour + "";
		if(hour < 10){
			textHours = "0" + hour;
		}
		
		int minute = eventDate.getMinutes();
		String textMinutes = minute + "";
		if(minute < 10){
			textMinutes = "0" + minute;
		}

		stringBuilder.append(eventCalendar.get(Calendar.DAY_OF_MONTH)).append(". ").append(months[(eventCalendar.get(Calendar.MONTH))]).append(" ").append(eventCalendar.get(Calendar.YEAR)).append(" kl. ").append(textHours).append(".").append(textMinutes);

		long currentTimeMillis = currentTime.getTimeInMillis();
		long eventMillis = eventCalendar.getTimeInMillis();
		long minutes = (currentTimeMillis / 60000) - (eventMillis / 60000);


		if (minutes >= 150 && minutes <= 180) {
			stringBuilder.replace(0, stringBuilder.length(), "ca 3 timer siden");
		}
		if (minutes >= 90 && minutes < 150) {
			stringBuilder.replace(0, stringBuilder.length(), "ca 2 timer siden");
		}
		if (minutes >= 45 && minutes < 90) {
			stringBuilder.replace(0, stringBuilder.length(), "ca 1 time siden");
		}
		if (minutes < 45 && minutes != 1) {
			stringBuilder.replace(0, stringBuilder.length(), minutes + " minutter siden");
		}
		if (minutes == 1) {
			stringBuilder.replace(0, stringBuilder.length(), minutes + " minutt siden");
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * Check if network is available 
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isNetworkAvailable(Context context) {
		conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conManager != null) {
			if (conManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED || conManager.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING || conManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || conManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING) {
				return true;
			}
		}
		return false;
	}
	
	
}
