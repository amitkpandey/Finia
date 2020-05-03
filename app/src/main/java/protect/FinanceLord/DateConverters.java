package protect.FinanceLord;

import androidx.room.TypeConverter;

import java.sql.Date;

public class DateConverters {
    @TypeConverter
    public static Date timestampToDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}