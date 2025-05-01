package beadando.isports_app.utils;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String formatFirebaseTimestampForDisplay(Timestamp timestamp){
        Date date = timestamp.toDate();
        Calendar now = Calendar.getInstance();

        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(date);

        SimpleDateFormat simpleDateFormat;

        if (dateCal.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
             simpleDateFormat = new SimpleDateFormat("MMM dd. HH:mm", Locale.getDefault());
        } else {
             simpleDateFormat = new SimpleDateFormat("yyyy. MMM dd. HH:mm", Locale.getDefault());
        }
        return simpleDateFormat.format(date);
    }
}
