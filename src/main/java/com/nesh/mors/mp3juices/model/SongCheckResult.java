package com.nesh.mors.mp3juices.model;

public class SongCheckResult {
    private String hash;
    private String user;
    private int sid;

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHash() {
        return hash;
    }

    public String getUser() {
        return user;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }
}
