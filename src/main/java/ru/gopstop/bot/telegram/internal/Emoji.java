package ru.gopstop.bot.telegram.internal;

public enum Emoji {
    // Emoticones group
    GRINNING_FACE_WITH_SMILING_EYES('\uD83D', '\uDE01'),
    FACE_WITH_TEARS_OF_JOY('\uD83D', '\uDE02'),
    SMILING_FACE_WITH_OPEN_MOUTH('\uD83D', '\uDE03'),
    SMILING_FACE_WITH_OPEN_MOUTH_AND_SMILING_EYES('\uD83D', '\uDE04'),
    SMILING_FACE_WITH_OPEN_MOUTH_AND_COLD_SWEAT('\uD83D', '\uDE05'),
    SMILING_FACE_WITH_OPEN_MOUTH_AND_TIGHTLY_CLOSED_EYES('\uD83D', '\uDE06'),
    WINKING_FACE('\uD83D', '\uDE09'),
    SMILING_FACE_WITH_SMILING_EYES('\uD83D', '\uDE0A'),
    FACE_SAVOURING_DELICIOUS_FOOD('\uD83D', '\uDE0B'),
    RELIEVED_FACE('\uD83D', '\uDE0C'),
    SMILING_FACE_WITH_HEART_SHAPED_EYES('\uD83D', '\uDE0D'),
    SMIRKING_FACE('\uD83D', '\uDE0F'),
    UNAMUSED_FACE('\uD83D', '\uDE12'),
    FACE_WITH_COLD_SWEAT('\uD83D', '\uDE13'),
    PENSIVE_FACE('\uD83D', '\uDE14'),
    CONFOUNDED_FACE('\uD83D', '\uDE16'),
    FACE_THROWING_A_KISS('\uD83D', '\uDE18'),
    KISSING_FACE_WITH_CLOSED_EYES('\uD83D', '\uDE1A'),
    FACE_WITH_STUCK_OUT_TONGUE_AND_WINKING_EYE('\uD83D', '\uDE1C'),
    FACE_WITH_STUCK_OUT_TONGUE_AND_TIGHTLY_CLOSED_EYES('\uD83D', '\uDE1D'),
    DISAPPOINTED_FACE('\uD83D', '\uDE1E'),
    ANGRY_FACE('\uD83D', '\uDE20'),
    POUTING_FACE('\uD83D', '\uDE21'),
    CRYING_FACE('\uD83D', '\uDE22'),
    PERSEVERING_FACE('\uD83D', '\uDE23'),
    FACE_WITH_LOOK_OF_TRIUMPH('\uD83D', '\uDE24'),
    DISAPPOINTED_BUT_RELIEVED_FACE('\uD83D', '\uDE25'),
    FEARFUL_FACE('\uD83D', '\uDE28'),
    WEARY_FACE('\uD83D', '\uDE29'),
    SLEEPY_FACE('\uD83D', '\uDE2A'),
    TIRED_FACE('\uD83D', '\uDE2B'),
    LOUDLY_CRYING_FACE('\uD83D', '\uDE2D'),
    FACE_WITH_OPEN_MOUTH_AND_COLD_SWEAT('\uD83D', '\uDE30'),
    FACE_SCREAMING_IN_FEAR('\uD83D', '\uDE31'),
    ASTONISHED_FACE('\uD83D', '\uDE32'),
    FLUSHED_FACE('\uD83D', '\uDE33'),
    DIZZY_FACE('\uD83D', '\uDE35'),
    FACE_WITH_MEDICAL_MASK('\uD83D', '\uDE37'),
    GRINNING_CAT_FACE_WITH_SMILING_EYES('\uD83D', '\uDE38'),
    CAT_FACE_WITH_TEARS_OF_JOY('\uD83D', '\uDE39'),
    SMILING_CAT_FACE_WITH_OPEN_MOUTH('\uD83D', '\uDE3A'),
    SMILING_CAT_FACE_WITH_HEART_SHAPED_EYES('\uD83D', '\uDE3B'),
    CAT_FACE_WITH_WRY_SMILE('\uD83D', '\uDE3C'),
    KISSING_CAT_FACE_WITH_CLOSED_EYES('\uD83D', '\uDE3D'),
    POUTING_CAT_FACE('\uD83D', '\uDE3E'),
    CRYING_CAT_FACE('\uD83D', '\uDE3F'),
    WEARY_CAT_FACE('\uD83D', '\uDE40'),
    FACE_WITH_NO_GOOD_GESTURE('\uD83D', '\uDE45'),
    FACE_WITH_OK_GESTURE('\uD83D', '\uDE46'),
    PERSON_BOWING_DEEPLY('\uD83D', '\uDE47'),
    SEE_NO_EVIL_MONKEY('\uD83D', '\uDE48'),
    HEAR_NO_EVIL_MONKEY('\uD83D', '\uDE49'),
    SPEAK_NO_EVIL_MONKEY('\uD83D', '\uDE4A'),
    HAPPY_PERSON_RAISING_ONE_HAND('\uD83D', '\uDE4B'),
    PERSON_RAISING_BOTH_HANDS_IN_CELEBRATION('\uD83D', '\uDE4C'),
    PERSON_FROWNING('\uD83D', '\uDE4D'),
    PERSON_WITH_POUTING_FACE('\uD83D', '\uDE4E'),
    PERSON_WITH_FOLDED_HANDS('\uD83D', '\uDE4F'),

    // Dingbats group
    BLACK_SCISSORS(null, '\u2702'),
    WHITE_HEAVY_CHECK_MARK(null, '\u2705'),
    AIRPLANE(null, '\u2708'),
    ENVELOPE(null, '\u2709'),
    RAISED_FIST(null, '\u270A'),
    RAISED_HAND(null, '\u270B'),
    VICTORY_HAND(null, '\u270C'),
    PENCIL(null, '\u270F'),
    BLACK_NIB(null, '\u2712'),
    HEAVY_CHECK_MARK(null, '\u2714'),
    HEAVY_MULTIPLICATION_X(null, '\u2716'),
    SPARKLES(null, '\u2728'),
    EIGHT_SPOKED_ASTERISK(null, '\u2733'),
    EIGHT_POINTED_BLACK_STAR(null, '\u2734'),
    SNOWFLAKE(null, '\u2744'),
    SPARKLE(null, '\u2747'),
    CROSS_MARK(null, '\u274C'),
    NEGATIVE_SQUARED_CROSS_MARK(null, '\u274E'),
    BLACK_QUESTION_MARK_ORNAMENT(null, '\u2753'),
    WHITE_QUESTION_MARK_ORNAMENT(null, '\u2754'),
    WHITE_EXCLAMATION_MARK_ORNAMENT(null, '\u2755'),
    HEAVY_EXCLAMATION_MARK_SYMBOL(null, '\u2757'),
    HEAVY_BLACK_HEART(null, '\u2764'),
    HEAVY_PLUS_SIGN(null, '\u2795'),
    HEAVY_MINUS_SIGN(null, '\u2796'),
    HEAVY_DIVISION_SIGN(null, '\u2797'),
    BLACK_RIGHTWARDS_ARROW(null, '\u27A1'),
    CURLY_LOOP(null, '\u27B0'),

    // Transport and map symbols Group
    ROCKET('\uD83D', '\uDE80'),
    RAILWAY_CAR('\uD83D', '\uDE83'),
    HIGH_SPEED_TRAIN('\uD83D', '\uDE84'),
    HIGH_SPEED_TRAIN_WITH_BULLET_NOSE('\uD83D', '\uDE85'),
    METRO('\uD83D', '\uDE87'),
    STATION('\uD83D', '\uDE89'),
    BUS('\uD83D', '\uDE8C'),
    BUS_STOP('\uD83D', '\uDE8F'),
    AMBULANCE('\uD83D', '\uDE91'),
    FIRE_ENGINE('\uD83D', '\uDE92'),
    POLICE_CAR('\uD83D', '\uDE93'),
    TAXI('\uD83D', '\uDE95'),
    AUTOMOBILE('\uD83D', '\uDE97'),
    RECREATIONAL_VEHICLE('\uD83D', '\uDE99'),
    DELIVERY_TRUCK('\uD83D', '\uDE9A'),
    SHIP('\uD83D', '\uDEA2'),
    SPEEDBOAT('\uD83D', '\uDEA4'),
    HORIZONTAL_TRAFFIC_LIGHT('\uD83D', '\uDEA5'),
    CONSTRUCTION_SIGN('\uD83D', '\uDEA7'),
    POLICE_CARS_REVOLVING_LIGHT('\uD83D', '\uDEA8'),
    TRIANGULAR_FLAG_ON_POST('\uD83D', '\uDEA9'),
    DOOR('\uD83D', '\uDEAA'),
    NO_ENTRY_SIGN('\uD83D', '\uDEAB'),
    SMOKING_SYMBOL('\uD83D', '\uDEAC'),
    NO_SMOKING_SYMBOL('\uD83D', '\uDEAD'),
    BICYCLE('\uD83D', '\uDEB2'),
    PEDESTRIAN('\uD83D', '\uDEB6'),
    MENS_SYMBOL('\uD83D', '\uDEB9'),
    WOMENS_SYMBOL('\uD83D', '\uDEBA'),
    RESTROOM('\uD83D', '\uDEBB'),
    BABY_SYMBOL('\uD83D', '\uDEBC'),
    TOILET('\uD83D', '\uDEBD'),
    WATER_CLOSET('\uD83D', '\uDEBE'),
    BATH('\uD83D', '\uDEC0'),

    // Weather
    UMBRELLA_WITH_RAIN_DROPS(null, '\u2614'),
    HIGH_VOLTAGE_SIGN(null, '\u26A1'),
    SNOWMAN_WITHOUT_SNOW(null, '\u26C4'),
    SUN_BEHIND_CLOUD(null, '\u26C5'),
    CLOSED_UMBRELLA('\uD83C', '\uDF02'),
    SUN_WITH_FACE('\uD83C', '\uDF1E'),
    FOGGY('\uD83C', '\uDF01'),
    CLOUD(null, '\u2601'),

    // Others
    LEFT_RIGHT_ARROW(null, '\u2194'),
    ALARM_CLOCK(null, '\u23F0'),
    SOON_WITH_RIGHTWARDS_ARROW_ABOVE('\uD83D', '\uDD1C'),
    EARTH_GLOBE_EUROPE_AFRICA('\uD83C', '\uDF0D'),
    GLOBE_WITH_MERIDIANS('\uD83C', '\uDF10'),
    STRAIGHT_RULER('\uD83D', '\uDCCF'),
    INFORMATION_SOURCE(null, '\u2139'),
    BLACK_RIGHT_POINTING_DOUBLE_TRIANGLE(null, '\u23E9'),
    BLACK_RIGHT_POINTING_TRIANGLE(null, '\u25B6'),
    BACK_WITH_LEFTWARDS_ARROW_ABOVE('\uD83D', '\uDD19'),
    WRENCH('\uD83D', '\uDD27'),
    DIGIT_THREE(null, '\u0033'),
    CLIPBOARD('\uD83D', '\uDCCB'),
    THUMBS_UP_SIGN('\uD83D', '\uDC4D'),
    WHITE_RIGHT_POINTING_BACKHAND_INDEX('\uD83D', '\uDC49'),
    TEAR_OFF_CALENDAR('\uD83D', '\uDCC6'),
    LARGE_ORANGE_DIAMOND('\uD83D', '\uDD36'),
    HUNDRED_POINTS_SYMBOL('\uD83D', '\uDCAF'),
    ROUND_PUSHPIN('\uD83D', '\uDCCD'),
    WAVING_HAND_SIGN('\uD83D', '\uDC4B');

    Character firstChar;
    Character secondChar;

    Emoji(Character firstChar, Character secondChar) {
        this.firstChar = firstChar;
        this.secondChar = secondChar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (this.firstChar != null) {
            sb.append(this.firstChar);
        }
        if (this.secondChar != null) {
            sb.append(this.secondChar);
        }

        return sb.toString();
    }
}