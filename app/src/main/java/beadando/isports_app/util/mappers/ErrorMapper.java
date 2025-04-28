package beadando.isports_app.util.mappers;

import androidx.annotation.StringRes;

import beadando.isports_app.R;

public class ErrorMapper {

    @StringRes
    public static int mapExceptionToMessage(Exception e) {
        if (e == null || e.getMessage() == null) {
            return R.string.error_unknown;
        }


        switch (e.getMessage()) {
            case "error_event_title_required":
                return R.string.error_event_title_required;
            case "error_event_full":
                return R.string.error_event_full;
            case "error_already_applied":
                return R.string.error_already_applied;
            case "error_event_not_found":
                return R.string.error_event_not_found;
            default:
                return R.string.error_unknown;
        }
    }
}