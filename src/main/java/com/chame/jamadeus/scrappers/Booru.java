package com.chame.jamadeus.scrappers;

import com.chame.jamadeus.utils.ConfigFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.net.URL;
import java.net.URLConnection;

import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.Json;

public class Booru {
    private static final int FETCH_REFRESH_HOUR = getFetchHour();
    private static final List<String> blackList = Arrays.asList("loli", "gore");
    private final Map<Integer, String[]> booruSearchStorage;
    private final String booruSearch;
    private ZonedDateTime nextFetch;
    
    public Booru(String search) {
        this.booruSearchStorage = new HashMap();
        this.booruSearch = search;
        this.nextFetch = null;
    }

    public String[] getBooru(Integer indexn, boolean safe) {
        if (checkTime() && !fetchJson(safe)) {
            return null;
        }

        int index;
        int booruAlbumCount = this.booruSearchStorage.size();

        if (indexn == null || indexn > booruAlbumCount && indexn != 0) {
            Random rand = new Random();
            index = rand.nextInt(booruAlbumCount);
        } else {
            index = indexn;
        }

        return booruSearchStorage.get(index);
    }

    private boolean checkTime(){
        return this.nextFetch == null 
                || ZonedDateTime.now(ZoneId.of("UTC")).isAfter(this.nextFetch);
    }

    private boolean fetchJson(boolean safe){
        JsonArray booruJson;
        String safeToggle = safe ? "s" : "e";
        String toSearch 
                = this.booruSearch == null ? "" : "q="+this.booruSearch+"&";

        try {
            URLConnection connectionHandle 
                    = new URL("https://cure.ninja/booru/api/json/1?"
                    +toSearch
                    +"o=r&f="+safeToggle)
                    .openConnection();
            
            connectionHandle.setRequestProperty("User-Agent"
                    , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    +"AppleWebKit/537.36 (KHTML, like Gecko) "
                    +"Chrome/84.0.4147.89 Safari/537.36");
            
            booruJson 
                    = Json.createReader(connectionHandle.getInputStream())
                    .readObject()
                    .getJsonArray("results");
            
        } catch (IOException e) {
            System.out.println("Can't fetch Booru tag "+toSearch);
            return false;
        }

        ZonedDateTime lastFetch = ZonedDateTime.now(ZoneId.of("UTC"));
        if (lastFetch.getHour() >= FETCH_REFRESH_HOUR) {
            this.nextFetch = lastFetch.plusDays(1).withHour(FETCH_REFRESH_HOUR);
        } else {
            this.nextFetch = lastFetch.withHour(FETCH_REFRESH_HOUR);
        }

        this.booruSearchStorage.clear();
        int albumCount = 0;
        
        for(int i = 0; i < booruJson.size() || i < 20; i++) {
            JsonObject items = booruJson.getJsonObject(i);
            if (!blackList.stream().anyMatch(items.getString("tag")::contains)){
                try {
                    String[] post = {items.getString("id"), //page url
                                    items.getString("source"), //source page
                                    items.getString("userName"), //author
                                    items.getString("url")};  //preview image
                    this.booruSearchStorage.put(albumCount, post);
                    albumCount++;
                } catch (NullPointerException e) {
                    //ignore
                    System.out.println("nullpointer at Booru search");
                }
            }
        }

        if (albumCount == 0 || this.booruSearchStorage.isEmpty()) {
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
        } catch(NumberFormatException e){
            return 6;
        }
    }
}