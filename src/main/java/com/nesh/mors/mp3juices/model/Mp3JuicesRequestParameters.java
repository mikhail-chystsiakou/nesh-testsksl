package com.nesh.mors.mp3juices.model;

import com.nesh.mors.model.Mors;

public class Mp3JuicesRequestParameters {
    String cookies;
    String jQueryExpando;
    String k; // special salt

    public Mp3JuicesRequestParameters(String cookies, String jQueryExpando, String k) {
        this.cookies = cookies;
        this.jQueryExpando = jQueryExpando;
        this.k = k;
    }

    public String getCookies() {
        return cookies;
    }

    public String getJQueryExpando() {
        return jQueryExpando;
    }

    public String getK() {
        return k;
    }

    public void enableMorses(Mors... morses) {
        // s=yt%3A1-sc%3A0-vk%3A1-yd%3A0-4s%3A0-ar%3A0
        for (Mors mors : morses) {
            cookies = cookies.replaceAll("^(.*s=[^;]*" + mors.getAbbreviation() + "%3A)(\\d)(-.*)$", "$11$3");
        }
    }
}
