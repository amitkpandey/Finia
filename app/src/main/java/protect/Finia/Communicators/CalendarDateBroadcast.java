package protect.Finia.Communicators;

import java.util.Date;

/**
 * The communicator between CalendarDialog and an activity.
 * This interface must be implemented by the activity receiving the message
 * and the method message can only be called in the CalendarDialog that
 * belongs to this activity.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public interface CalendarDateBroadcast {
    void message(Date date);
}
