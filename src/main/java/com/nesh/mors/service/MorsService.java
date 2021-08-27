package com.nesh.mors.service;

import com.nesh.mors.model.MorsSong;
import com.nesh.mors.model.MorsSongSearchResult;

import java.util.List;

public interface MorsService<S extends MorsSongSearchResult, L extends MorsSong> {
    void initialize();
    List<S> searchSongs(String searchString);
    L loadSong(MorsSongSearchResult song);
}
