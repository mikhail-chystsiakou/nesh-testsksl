package com.nesh.mors.model;

import java.util.Locale;

public enum Mors {
    YOUTUBE("-1", "yt"),
    SOUNDCLOUD("2", "sc"),
    VK("3", "vk"),
    YANDEX("4", "yd"),
    SHARED("5", "4s"),
    ARCHIVE("6", "ar");

    private String number;
    private String abbreviation;

    Mors(String number, String abbreviation) {
        this.number = number;
        this.abbreviation = abbreviation;
    }

    public String getNumber() {
        return number;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public static Mors fromString(String morsString) {
        if ("4shared".equalsIgnoreCase(morsString)) {
            return Mors.SHARED;
        }
        return Mors.valueOf(morsString.toUpperCase(Locale.ROOT));
    }
}
