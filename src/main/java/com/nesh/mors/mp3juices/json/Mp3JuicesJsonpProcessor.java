package com.nesh.mors.mp3juices.json;

import com.google.gson.stream.JsonReader;
import com.nesh.mors.mp3juices.exception.Mp3JuicesException;
import com.nesh.mors.model.Mors;
import com.nesh.mors.mp3juices.model.Mp3JuicesSong;
import com.nesh.mors.mp3juices.model.SongCheckResult;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Works with jQuery jsonp-formatted data
 *
 * jQuery34109455470757493205_1626374855916({
 *     "count": 12,
 *     "0": {
 *         "bitrate": "192",
 *         "duration": "1:49:10",
 *         "id": 1,
 *         "link": {
 *             "href": "",
 *             "target": "_self"
 *         },
 *         "player": {
 *             "mode": "true",
 *             "data": "8C2MAUeNFz4"
 *         },
 *         "source": {
 *             "data": "8C2MAUeNFz4",
 *             "id": "1",
 *             "name": "YouTube"
 *         },
 *         "title": {
 *             "default": "La La Land - Full OST \/ Soundtrack (HQ)",
 *             "base64": "TGEgTGEgTGFuZCAtIEZ1bGwgT1NUU291bmR0cmFjayAoSFEpLm1wMw%3D%3D"
 *         }
 *     },
 *     "query": "la la land"
 * })
 */
public class Mp3JuicesJsonpProcessor {
    public String jQueryExpando;

    public Mp3JuicesJsonpProcessor(String jQueryExpando) {
        this.jQueryExpando = jQueryExpando;
    }

    public List<Mp3JuicesSong> parseSongList(String songListJsonp) {
        String songListJson = trimExpando(songListJsonp);
        List<Mp3JuicesSong> songList = new ArrayList<>();

        try (JsonReader reader = new JsonReader(new StringReader(songListJson))) {
            reader.beginObject();
            while (reader.hasNext()) {
                String elementName = reader.nextName();
                if (elementName.matches("\\d+")) {
                    songList.add(parseSong(reader));
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (Exception e) {
            throw new Mp3JuicesException(e, "Failure during song list response parsing: {}", songListJsonp);
        }
        return songList;
    }

    public SongCheckResult parseCheckSongResult(String checkSongResultJsonp) {
        String checkSongResult = trimExpando(checkSongResultJsonp);
        SongCheckResult songCheckResult = new SongCheckResult();

        try (JsonReader reader = new JsonReader(new StringReader(checkSongResult))) {
            reader.beginObject();
            while (reader.hasNext()) {
                String elementName = reader.nextName();
                if ("hash".equals(elementName)) {
                    songCheckResult.setHash(reader.nextString());
                } else if ("user".equals(elementName)) {
                    songCheckResult.setUser(reader.nextString());
                } else if ("sid".equals(elementName)) {
                    songCheckResult.setSid(reader.nextInt());
                } else if ("error".equals(elementName)) {
                    String errorValue = reader.nextString();
                    if ("0".equals(errorValue)) {
                        //throw new Mp3JuicesException("Failed to get user and hash for song: " + checkSongResult);
                    }
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            throw new Mp3JuicesException("Failed to parse check song result: " + checkSongResultJsonp, e);
        }

        return songCheckResult;
    }

    public Mp3JuicesSong parseSong(JsonReader reader) throws IOException {
        Mp3JuicesSong song = new Mp3JuicesSong();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            if ("source".equals(name)) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String sourceName = reader.nextName();
                    if ("data".equals(sourceName)) {
                        song.setCode(reader.nextString());
                    } else if ("name".equals(sourceName)) {
                        song.setSource(Mors.fromString(reader.nextString()));
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else if ("title".equals(name)) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String nextName = reader.nextName();
                    if ("default".equals(nextName)) {
                        song.setTitle(reader.nextString());
                    } else if ("base64".equals(nextName)) {
                        song.setTitle64(reader.nextString());
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return song;
    }

    private String trimExpando(String jsonp) {
        return jsonp.substring(jQueryExpando.length()+1, jsonp.length()-1);
    }
}
