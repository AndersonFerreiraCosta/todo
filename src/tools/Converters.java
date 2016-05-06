package tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Converters {

	public static Timestamp calendarToTimestamp(Calendar value) {
		if (value == null) return null;
		
		return new Timestamp(value.getTimeInMillis());
	}
	
	public static Calendar timestampToCalendar(Timestamp value) {
		if (value == null) return null;
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(value.getTime());
		
		return c;
	}
	
	public static int stringToInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch(Exception e) {
			return 0;
		}
	}

	public static double stringToDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch(Exception e) {
			return 0;
		}
	}
	
	public static Calendar stringToCalendar(String value) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			
			Calendar c = Calendar.getInstance();
			c.setTime(sdf.parse(value));
			
			return c;
		} catch(Exception e) {
			return null;
		}
	}
	
	public static String calendarToString(Calendar c) {
		if (c == null) return "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(c.getTime());
	}
}