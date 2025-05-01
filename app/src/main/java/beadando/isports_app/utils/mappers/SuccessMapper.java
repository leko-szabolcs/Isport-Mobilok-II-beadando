package beadando.isports_app.utils.mappers;

import androidx.annotation.StringRes;

import beadando.isports_app.R;

public class SuccessMapper {

    @StringRes
    public static int mapSuccessCodeToMessage(String e) {
        if (e == null) {
            return R.string.default_success_message;
        }
        switch (e) {
            case "successful_apply":
                return R.string.successful_apply;
            case "create_event_success":
                return R.string.create_event_success;
            default:
                return R.string.default_success_message;
        }
    }
}



