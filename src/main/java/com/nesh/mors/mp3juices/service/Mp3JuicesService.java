package com.nesh.mors.mp3juices.service;

import com.nesh.mors.model.Mors;
import com.nesh.mors.model.MorsSongSearchResult;
import com.nesh.mors.mp3juices.exception.Mp3JuicesException;
import com.nesh.mors.mp3juices.json.Mp3JuicesJsonpProcessor;
import com.nesh.mors.mp3juices.model.*;
import com.nesh.mors.service.MorsService;
import com.nesh.mors.service.RetrofitMp3JuicesService;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpCookie;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import static com.nesh.mors.model.Mors.*;

public class Mp3JuicesService implements MorsService<Mp3JuicesSong, Mp3JuicesSong> {
    private final static String MP3JUICES_BASE_URL = "https://www.mp3juices.cc/";
    private final static String MP3JUICES_SECRET_K_VALUE = "cheesecake";
    private final static String[] DOWNLOAD_PREFIXES = new String[] {
        ".eex.cx", "ccc", "cce", "cec", "cee", "ceo", "cex", "coc",
        "coe", "coo", "cox", "cxc", "cxo", "ece", "eco", "eee",
        "eex", "eoc", "eoe", "eoo", "eox", "exc", "exe", "exx",
        "occ", "oce", "oco", "ocx", "oec", "oee", "oeo", "oex",
        "ooe", "oox", "oxc", "oxe", "oxx", "xcc", "xco", "xcx",
        "xec", "xee", "xex", "xoc", "xox", "xxo", "xxx"
    };
    private final static String SOUNDCLOUD_HOST = "eex.cx";

    private Retrofit retrofit;
    private RetrofitMp3JuicesService retrofitMp3JuicesService;
    private Mp3JuicesRequestParameters requestParameters;

    @Override
    public void initialize() {
        retrofit = new Retrofit.Builder()
            .baseUrl(MP3JUICES_BASE_URL)
            .build();

        retrofitMp3JuicesService = retrofit.create(RetrofitMp3JuicesService.class);

        requestParameters = getRequestParameters();
        requestParameters.enableMorses(YOUTUBE, SOUNDCLOUD, VK, YANDEX, SHARED);
    }

    @Override
    public List<Mp3JuicesSong> searchSongs(String searchString) {
        if (requestParameters == null) {
            throw new Mp3JuicesException("Method 'initialize' must be called before first use");
        }

        String currentTimestamp = String.valueOf(System.currentTimeMillis() / 1000);
        Response<ResponseBody> listSongsResponse;
        try {
            listSongsResponse = retrofitMp3JuicesService.listSongs(
                requestParameters.getCookies(),
                requestParameters.getJQueryExpando(),
                searchString,
                MP3JUICES_SECRET_K_VALUE,
                currentTimestamp
            ).execute();
            if (listSongsResponse.body() == null) {
                throw new Mp3JuicesException("Empty response");
            }
        } catch (IOException e) {
            throw new Mp3JuicesException(e,
                "Failed to get list of songs, args was: ",
                requestParameters.getCookies(),
                requestParameters.getJQueryExpando(),
                searchString,
                MP3JUICES_SECRET_K_VALUE,
                currentTimestamp
            );
        }
        String searchResult = extractGzipBody(listSongsResponse);

        return new Mp3JuicesJsonpProcessor(
            requestParameters.getJQueryExpando()
        ).parseSongList(searchResult);
    }

    @Override
    public Mp3JuicesSong loadSong(MorsSongSearchResult songInterface) {
        Mp3JuicesSong mp3JuicesSong;
        try {
            mp3JuicesSong = (Mp3JuicesSong) songInterface;
        } catch (ClassCastException e) {
            throw new Mp3JuicesException(e,
                "Expected Mp3JuicesSong type, got '{}', value: {}",
                songInterface.getClass().getSimpleName(),
                songInterface
            );
        }

        String timeNow = String.valueOf(System.currentTimeMillis() / 1000);
        String responseBody;
        try {
            Response<ResponseBody> responseBodyCall = retrofitMp3JuicesService.loadSong(
                requestParameters.getCookies(),
                requestParameters.getJQueryExpando(),
                mp3JuicesSong.getCode(),
                "mp3",
                requestParameters.getK(),
                mp3JuicesSong.getTitle64(),
                timeNow
            ).execute();
            if (responseBodyCall.body() == null) {
                throw new Mp3JuicesException("Empty response");
            }
            responseBody = responseBodyCall.body().string();
        } catch (IOException e) {
            throw new Mp3JuicesException(e,
                "Failed to load song, args was: ",
                requestParameters.getCookies(),
                requestParameters.getJQueryExpando(),
                mp3JuicesSong.getCode(),
                "mp3",
                requestParameters.getK(),
                mp3JuicesSong.getTitle64(),
                timeNow
            );
        }

        SongCheckResult songCheckResult = new Mp3JuicesJsonpProcessor(
            requestParameters.getJQueryExpando()
        ).parseCheckSongResult(responseBody);
        mp3JuicesSong.setUser(songCheckResult.getUser());
        mp3JuicesSong.setHash(songCheckResult.getHash());

        String downloadURL = null;
        if (YOUTUBE.equals(mp3JuicesSong.getSource())) {
            downloadURL = "https://" + DOWNLOAD_PREFIXES[songCheckResult.getSid()] + DOWNLOAD_PREFIXES[0]
                + "/" + mp3JuicesSong.getHash()
                + "/" + mp3JuicesSong.getCode()
                + "/" + mp3JuicesSong.getUser();

        } else if (
            SOUNDCLOUD.equals(mp3JuicesSong.getSource())
                || VK.equals(mp3JuicesSong.getSource())
                || YANDEX.equals(mp3JuicesSong.getSource())
                || SHARED.equals(mp3JuicesSong.getSource())
                || ARCHIVE.equals(mp3JuicesSong.getSource())
        ) {
            downloadURL = "https://" + SOUNDCLOUD_HOST + "/" + mp3JuicesSong.getSource().getNumber()
                + "/" + mp3JuicesSong.getCode()
                + "/" + mp3JuicesSong.getTitle64();
        }

        mp3JuicesSong.setDownloadURL(downloadURL);

        return mp3JuicesSong;
    }

    private String extractGzipBody(Response<ResponseBody> response) {
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            throw new Mp3JuicesException("Empty response for request {}", response);
        }

        InputStream responseStream = responseBody.byteStream();
        return decompressGzip(responseStream);
    }

    public static String decompressGzip(InputStream gzipStream) {
        try (final GZIPInputStream gzipInput = new GZIPInputStream(gzipStream);
             final StringWriter stringWriter = new StringWriter()) {
            IOUtils.copy(gzipInput, stringWriter, "UTF-8");
            return stringWriter.toString();
        } catch (IOException e) {
            throw new Mp3JuicesException(e, "Error while decompression!");
        }
    }

    private Mp3JuicesRequestParameters getRequestParameters() {
        Response<ResponseBody> initialGetRequest;
        String responseBody;
        try {
            initialGetRequest = retrofitMp3JuicesService.getRequest().execute();
            if (initialGetRequest.body() == null) {
                throw new Mp3JuicesException("Empty response");
            }
            responseBody = initialGetRequest.body().string();
        } catch (Exception e) {
            throw new Mp3JuicesException(e, "Failed to perform get request", e);
        }

        return new Mp3JuicesRequestParameters(
            extractCookies(initialGetRequest.headers()),
            extractJQueryExpando(responseBody),
            extractParameterK(responseBody)
        );
    }

    private static String extractCookies(Headers headers) {
        StringBuilder requestCookies = new StringBuilder();
        List<String> allCookies = headers.values("Set-Cookie");
        for (String cookieString : allCookies) {
            List<HttpCookie> cookies = HttpCookie.parse(cookieString);
            for (HttpCookie cookie : cookies) {
                requestCookies.append(cookie).append(';');
            }
        }

        return requestCookies.toString();
    }

    private static String extractJQueryExpando(String htmlPage) {
        Pattern p = Pattern.compile("src=\"/js/jquery-(.*).min.js\"");
        Matcher m = p.matcher(htmlPage);
        if (!m.find()) {
            throw new Mp3JuicesException(
                "No jquery version found, pattern was: {}, htmlPage: {}",
                p.pattern(),
                htmlPage
            );
        }
        String jQueryVersion = m.group(1);

        double randomDouble = Math.random();
        String jQueryExpando = "jQuery" + jQueryVersion + randomDouble + "_" + System.currentTimeMillis();
        return jQueryExpando.replaceAll("\\.", "");
    }

    private static String extractParameterK(String htmlPage) {
        Pattern p = Pattern.compile("src=\"/js/juices.js\\?\\w=(.*)&_");
        Matcher m = p.matcher(htmlPage);
        if (!m.find()) {
            throw new Mp3JuicesException(
                "No special code found for juices.js, pattern was: {}, htmlPage: {}",
                p.pattern(),
                htmlPage
            );
        }
        return encodeParameterK(m.group(1));
    }

    private static String encodeParameterK(String scriptCode) {
        int r = 0;
        int t = 0;
        StringBuilder result = new StringBuilder();
        for (; t < scriptCode.length(); t++) {
            r = scriptCode.charAt(t);
            if (64 < r && r < 91) { // lower-case - shift alphabetically to left
                r = (r == 65) ? 90 : r - 1;
            } else if (96 < r && r < 123) { // upper-case - shift alphabetically to left
                r = (r == 122) ? 97 : r + 1;
            } else if (47 < r && r < 53) { // number [0;4] - substract from 9
                switch (r) {
                    case 48:
                        r = 57;
                        break;
                    case 49:
                        r = 56;
                        break;
                    case 50:
                        r = 55;
                        break;
                    case 51:
                        r = 54;
                        break;
                    case 52:
                        r = 53;
                }
            } else if (52 < r && r < 58) { // 5->2 6->3 7->3 8->4 9->4
                r = ((r - 48) / 2) + 48;
            } else if (r == 45) { // '-' -> '_'
                r = 95;
            }
            result.append((char)r);
        }
        return result.toString();
    }
}
