package ie.gtludwig.pa.util;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ControllerUtil {

    	public static void initBinderDate(WebDataBinder binder) {
		SimpleDateFormat dateFormat = getDateFormat();
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	public static SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("dd/MM/yyyy");
	}

	public static Date getEndDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	public static Date getFirstDayOfMonth() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	public static Date getLastDayOfMonth() {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static Date getStartDayDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
}
