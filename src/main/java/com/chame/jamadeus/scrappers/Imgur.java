package com.chame.jamadeus.scrappers;

import com.chame.jamadeus.utils.ConfigFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.net.URL;
import java.net.URLConnection;

import javax.json.JsonObject;
import javax.json.JsonArray;
import javax.json.Json;

public class Imgur {
    private static final int FETCH_REFRESH_HOUR = getFetchHour();
    private static final String CLIENT_ID 
            = ConfigFile.getImgurProperty("clientid")
            .replaceAll("\\s+", "");
    
    private final Map<Integer, String[]> imgurAlbumStorage;
    private final String imgurTag;
    private ZonedDateTime nextFetch;
    
    public Imgur(String imgurTag) {
        this.imgurAlbumStorage = new HashMap();
        this.imgurTag = imgurTag;
        this.nextFetch = null;
    }

    public String[] getAlbum(Integer indexn, int page) {
        if (checkTime() && !fetchJson(page)) {
            return null;
        }

        int index;
        int imgurAlbumCount = this.imgurAlbumStorage.size();

        if (indexn == null || indexn > imgurAlbumCount && indexn != 0) {
            Random rand = new Random();
            index = rand.nextInt(imgurAlbumCount);
        } else {
            index = indexn;
        }

        return  imgurAlbumStorage.get(index);
    }

    private boolean checkTime(){
        return this.nextFetch == null 
                || ZonedDateTime.now(ZoneId.of("UTC")).isAfter(this.nextFetch);
    }

    private boolean fetchJson(int page){
        JsonArray imgurJson;

        try {
            URLConnection connectionHandle 
                    = new URL("https://api.imgur.com/3/gallery/t/"
                    +this.imgurTag
                    +"/viral/day/"
                    +String.valueOf(page)+".json")
                    .openConnection();
            
            connectionHandle.setRequestProperty("User-Agent"
                    , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    +"AppleWebKit/537.36 (KHTML, like Gecko) "
                    +"Chrome/84.0.4147.89 Safari/537.36");
            
            connectionHandle.setRequestProperty("Authorization"
                    , "Client-ID "+CLIENT_ID);
            
            imgurJson 
                    = Json.createReader(connectionHandle.getInputStream())
                    .readObject()
                    .getJsonObject("data")
                    .getJsonArray("items");
            
        } catch (IOException e) {
            System.out.println("Can't fetch Imgur tag "+this.imgurTag);
            return false;
        }
        
        ZonedDateTime lastFetch = ZonedDateTime.now(ZoneId.of("UTC"));
        
        if (lastFetch.getHour() >= FETCH_REFRESH_HOUR) {
            this.nextFetch = lastFetch.plusDays(1).withHour(FETCH_REFRESH_HOUR);
        } else {
            this.nextFetch = lastFetch.withHour(FETCH_REFRESH_HOUR);
        }

        this.imgurAlbumStorage.clear();
        int albumCount = 0;
        
        for(int i = 0; i < imgurJson.size(); i++) {
            JsonObject items = imgurJson.getJsonObject(i);
            try {
                String[] post = {items.getString("title"), //title
                                items.getString("account_url"), //author
                                String.valueOf(items.getInt("views")), //views
                                items.getString("link"),  //album link
                                items.getJsonArray("images")
                                        .getJsonObject(0)
                                        .getString("link")}; //image link
                this.imgurAlbumStorage.put(albumCount, post);
                albumCount++;
                
            } catch (NullPointerException e) {
                //ignore
            }
        }
        if (albumCount == 0 || this.imgurAlbumStorage.isEmpty()) {
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