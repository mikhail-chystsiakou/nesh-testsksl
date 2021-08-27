package com.nesh.mors.mp3juices;

import com.nesh.mors.model.MorsSong;
import com.nesh.mors.model.MorsSongSearchResult;
import com.nesh.mors.mp3juices.model.Mp3JuicesSong;
import com.nesh.mors.mp3juices.service.Mp3JuicesService;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Mp3JuicesService service = new Mp3JuicesService();
        service.initialize();
        List<Mp3JuicesSong> foundSongs = service.searchSongs("black eyed peace");

        for (MorsSong song : foundSongs) {
            service.loadSong(song);
            System.out.println(song.getTitle() + "(from " + song.getSource() + ") - " + song.getDownloadURL());
        }
    }
}
