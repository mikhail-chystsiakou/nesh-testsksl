package com.nesh.mors.mp3juices.model;

import com.nesh.mors.model.Mors;
import com.nesh.mors.model.MorsSong;

public class Mp3JuicesSong implements MorsSong {
    private String title;
    private String title64;
    private String code;
    private String hash;
    private String user;

    private Mors source;
    private String downloadURL;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Mors getSource() {
        return source;
    }

    public void setSource(Mors source) {
        this.source = source;
    }

    public String getTitle64() {
        return title64;
    }

    public void setTitle64(String title64) {
        this.title64 = title64;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
