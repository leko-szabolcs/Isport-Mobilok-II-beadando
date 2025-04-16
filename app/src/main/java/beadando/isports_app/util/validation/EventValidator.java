package beadando.isports_app.util.validation;

import android.content.Context;


import com.google.firebase.Timestamp;

import beadando.isports_app.R;

public class EventValidator {

    public static void isValidEvent(
            Context context,
            String title,
            String location,
            Timestamp date,
            String participantsStr,
            String description
    ) throws ValidationException {

        if (isNullOrEmpty(title) )  {
            throw new ValidationException(context.getString(R.string.error_event_title_required));
        }

        if (isNullOrEmpty(location)) {
            throw new ValidationException(context.getString(R.string.error_event_location_required));
        }

        if (date == null) {
            throw new ValidationException(context.getString(R.string.error_event_date_required));
        }

        if (isNullOrEmpty(participantsStr)) {
            throw new ValidationException(context.getString(R.string.error_event_participants_required));
        }

        int participants;
        try {
            participants = Integer.parseInt(participantsStr);
        } catch (NumberFormatException e) {
            throw new ValidationException(context.getString(R.string.error_event_participants_invalid));
        }

        if (isNullOrEmpty(description)) {
            throw new ValidationException(context.getString(R.string.error_event_description_required));
        }

        isValidEventTitle(context, title);
        isValidEventLocation(context, location);
        isValidEventDate(context, date);
        isValidEventParticipants(context, participants);
        isValidEventDescription(context, description);
    }

    public static void isValidEventTitle(Context context, String title)  throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException(context.getString(R.string.error_event_title_required));
        }
        if (title.length() > ValidationConstants.EVENT_TITLE_MAX_LENGTH) {
            throw new ValidationException(context.getString(R.string.error_event_title_too_long));
        }
        if (title.length() < ValidationConstants.EVENT_TITLE_MIN_LENGTH) {
            throw new ValidationException(context.getString(R.string.error_event_title_too_short));
        }
    }

    public static void isValidEventLocation(Context context, String address) throws ValidationException {
        if (address == null || address.trim().isEmpty()) {
            throw new ValidationException(context.getString(R.string.error_event_location_required));
        }
        if (address.length() > ValidationConstants.EVENT_lOCATION_MAX_LENGTH) {
            throw new ValidationException(context.getString(R.string.error_event_location_too_long));
        }
        if (address.length() < ValidationConstants.EVENT_lOCATION_MIN_LENGTH) {
            throw new ValidationException(context.getString(R.string.error_event_location_too_short));
        }
    }

    public static void isValidEventDate(Context context, Timestamp date)  throws ValidationException {
        if (date == null) {
            throw new ValidationException(context.getString(R.string.error_event_date_required));
        }
        if (date.compareTo(Timestamp.now()) < 0) {
            throw new ValidationException(context.getString(R.string.error_event_date_invalid));
        }
    }

    public static void isValidEventParticipants(Context context, int participants)  throws ValidationException  {
        if (participants > ValidationConstants.EVENT_CAPACITY_MAX_VALUE) {
            throw new ValidationException(context.getString(R.string.error_event_participants_size_too_large));
        }
        if(participants < ValidationConstants.EVENT_CAPACITY_MIN_VALUE){
            throw new ValidationException(context.getString(R.string.error_event_participants_size_too_small));
        }
    }

    public static void isValidEventDescription(Context context, String description) throws ValidationException {
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException(context.getString(R.string.error_event_description_required));
        }
        if (description.length() > ValidationConstants.EVENT_DESCRIPTION_MAX_LENGTH) {
            throw new ValidationException(context.getString(R.string.error_event_description_too_long));
        }
        if (description.length() < ValidationConstants.EVENT_DESCRIPTION_MIN_LENGTH) {
            throw new ValidationException(context.getString(R.string.error_event_description_too_short));
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    private static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
