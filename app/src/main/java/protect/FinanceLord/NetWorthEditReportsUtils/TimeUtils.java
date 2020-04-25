package protect.FinanceLord.NetWorthEditReportsUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtils {

    public static Date firstSecondOfThisMinute() {
        Calendar calendar = new GregorianCalendar();
        int currentMinute = calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, currentMinute - currentMinute % 2);
        Date date = calendar.getTime();
        return date;
    }

    public Date setTime(int year, int month, int day, int hour, int minute){
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        Date date = calendar.getTime();
        return date;
    }
}