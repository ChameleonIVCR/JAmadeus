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

public class Imgur {
    private static final int fetchRefreshHour = getFetchHour();
    private static final String clientid = ConfigFile.getImgurProperty("clientid").replaceAll("\\s+", "");
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

        if (indexn == null || indexn.intValue() > imgurAlbumCount && indexn.intValue() != 0) {
            Random rand = new Random();
            index = rand.nextInt(imgurAlbumCount);
        } else {
            index = indexn.intValue();
        }

        return  imgurAlbumStorage.get(new Integer(index));
        //TODO return

    }

    private boolean checkTime(){
        if (nextFetch == null || ZonedDateTime.now(ZoneId.of("UTC")).isAfter(nextFetch)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean fetchJson(int page){
        JsonArray imgurJson;

        try {
            URLConnection connectionHandle = new URL("https://api.imgur.com/3/gallery/t/"+this.imgurTag+"/viral/day/"+String.valueOf(page)+".json").openConnection();
            connectionHandle.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
            connectionHandle.setRequestProperty("Authorization", "Client-ID "+clientid);
            imgurJson = Json.createReader(connectionHandle.getInputStream()).readObject().getJsonObject("data").getJsonArray("items");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't fetch Imgur tag "+this.imgurTag);
            return false;
        }
        ZonedDateTime lastFetch = ZonedDateTime.now(ZoneId.of("UTC"));
        if (lastFetch.getHour() >= fetchRefreshHour) {
            this.nextFetch = lastFetch.plusDays(1).withHour(fetchRefreshHour);
        } else {
            this.nextFetch = lastFetch.withHour(fetchRefreshHour);
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
                                items.getJsonArray("images").getJsonObject(0).getString("link")}; //image link
                this.imgurAlbumStorage.put(new Integer(albumCount), post);
                albumCount++;
            } catch (NullPointerException e) {
                //ignore
            }
        }
        if (albumCount == 0 || this.imgurAlbumStorage.size() == 0) {
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
        } catch(Exception e){ //not a number
            return 6;
        }
    }
}