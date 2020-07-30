package com.chame.jamadeus.scrappers;

import com.chame.jamadeus.utils.ConfigFile;

import java.lang.Integer;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.net.URL;
import java.net.URLConnection;

import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.json.Json;

public class Booru {
    private static final int fetchRefreshHour = getFetchHour();
    private static final String[] blackList = {"loli", "gore"};
    private final Map<Integer, String[]> booruSearchStorage;
    private final String booruSearch;
    private ZonedDateTime nextFetch;
    
    public Booru(String search) {
        this.booruSearchStorage = new HashMap();
        this.booruSearch = search;
        this.nextFetch = null;
    }

    public String[] getAlbum(Integer indexn, boolean safe) {
        if (checkTime() && !fetchJson(safe)) {
            return null;
        }

        int index;
        int imgurAlbumCount = this.booruSearchStorage.size();

        if (indexn == null || indexn.intValue() > imgurAlbumCount && indexn.intValue() != 0) {
            Random rand = new Random();
            index = rand.nextInt(imgurAlbumCount);
        } else {
            index = indexn.intValue();
        }

        return  booruSearchStorage.get(new Integer(index));
        //TODO return

    }

    private boolean checkTime(){
        if (nextFetch == null || ZonedDateTime.now(ZoneId.of("UTC")).isAfter(nextFetch)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean fetchJson(boolean safe){
        JsonArray booruJson;
        String safeToggle = safe ? "s" : "e";
        String toSearch = this.booruSearch == null ? "" : "q="+this.booruSearch+"&";

        try {
            URLConnection connectionHandle = new URL("https://cure.ninja/booru/api/json/1?"+toSearch+"o=r&f="+safeToggle).openConnection();
            connectionHandle.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            booruJson = Json.createReader(connectionHandle.getInputStream()).readObject().getJsonArray("results");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't fetch Booru tag "+toSearch);
            return false;
        }

        ZonedDateTime lastFetch = ZonedDateTime.now(ZoneId.of("UTC"));
        if (lastFetch.getHour() >= fetchRefreshHour) {
            this.nextFetch = lastFetch.plusDays(1).withHour(fetchRefreshHour);
        } else {
            this.nextFetch = lastFetch.withHour(fetchRefreshHour);
        }

        this.booruSearchStorage.clear();
        int albumCount = 0;
        
        for(int i = 0; i < booruJson.size() || i < 20; i++) {
            JsonObject items = booruJson.getJsonObject(i);
            for (String blackWord : blackList){
                if (!items.getString("tag").contains(blackWord)){
                    try {
                        String[] post = {items.getString("page"), //page url
                                        items.getString("userName"), //author
                                        items.getString("preview")};  //preview image
                        this.booruSearchStorage.put(new Integer(albumCount), post);
                        albumCount++;
                    } catch (NullPointerException e) {
                        //ignore
                    }
                }
            }
        }

        if (albumCount == 0 || this.booruSearchStorage.size() == 0) {
            System.out.println("Nothing fetched.");
            return false;
        }

        return true;
    }

    private static Integer getFetchHour() {
        try {
            int hour = new Integer(ConfigFile.getImgurProperty("fetchhour"));
            if (hour < 0 || hour > 24) {
                return 6;
            }
            return hour;
        } catch(Exception e){
            return 6;
        }
    }
}