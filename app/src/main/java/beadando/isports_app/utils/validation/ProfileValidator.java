package beadando.isports_app.utils.validation;

import beadando.isports_app.R;

public class ProfileValidator {

    public static void isValidProfile( String fullName,  String age, String description) throws ValidationException {
        isValidFullName(fullName);
        isValidAge(age);
        isValidDescription(description);
    }

    public static void isValidFullName( String fullName) throws ValidationException {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ValidationException(Integer.toString(R.string.error_full_name_required));
        }
        if (fullName.length() > ValidationConstants.PROFILE_FULL_NAME_MAX_LENGTH) {
            throw new ValidationException(Integer.toString(R.string.error_full_name_too_long));
        }
        if (fullName.length() < ValidationConstants.PROFILE_FULL_NAME_MIN_LENGTH) {
            throw new ValidationException(Integer.toString(R.string.error_full_name_too_short));
        }
    }

    public static void isValidDescription(String description) throws ValidationException {
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException(Integer.toString(R.string.error_description_required));
        }
        if (description.length() > ValidationConstants.PROFILE_DESCRIPTION_MAX_LENGTH) {
            throw new ValidationException(Integer.toString(R.string.error_description_too_long));
        }
        if (description.length() < ValidationConstants.PROFILE_DESCRIPTION_MIN_LENGTH) {
            throw new ValidationException(Integer.toString(R.string.error_description_too_short));
        }
    }

    public static void isValidAge(String age) throws ValidationException {
        if (age == null || age.trim().isEmpty()) {
            throw new ValidationException(Integer.toString(R.string.error_age_required));
        }

        int ageInt;
        try {
            ageInt = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            throw new ValidationException(Integer.toString(R.string.error_age_invalid));
        }

        if (ageInt > ValidationConstants.PROFILE_AGE_MAX_VALUE) {
            throw new ValidationException(Integer.toString(R.string.error_age_too_large));
        }
        if (ageInt < ValidationConstants.PROFILE_AGE_MIN_VALUE) {
            throw new ValidationException(Integer.toString(R.string.error_age_too_small));
        }
    }
}
