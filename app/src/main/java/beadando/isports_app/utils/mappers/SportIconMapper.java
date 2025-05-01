package beadando.isports_app.utils.mappers;

import beadando.isports_app.R;

public class SportIconMapper {


    public static int mapSportToIcon(String title) {
        if (title == null) {
            return R.drawable.ic_sport_logo;
        }

        switch (title) {
            case "Kosárlabda":
                return R.drawable.basketball;
            case "Labdarúgás":
                return R.drawable.football;
            case "Tenisz":
                return R.drawable.tennis;
            case "Küzdősportok":
                return R.drawable.martial_art;
            case "Jóga":
                return R.drawable.yoga;
            case "Rögbi":
                return R.drawable.rugby;
            default:
                return R.drawable.ic_sport_logo;
        }
    }
}
