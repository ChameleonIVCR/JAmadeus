package com.chame.jamadeus.scrappers;

import com.chame.jamadeus.utils.ConfigFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class Reddit {
    private static final int FETCH_REFRESH_HOUR = getFetchHour();
    private static final String LINK_PATTERN 
            = "<span><a href=\\\"https?:\\/\\/\\S+\\\">\\[link\\]";
    
    private final String subReddit;
    private final Map<Integer, String[]> redditPost;
    private ZonedDateTime nextFetch;
    
    public Reddit(String subReddit) {
        this.redditPost = new HashMap();
        this.subReddit = subReddit;
        this.nextFetch = null;
    }

    public String[] getSubReddit(Integer indexn) {
        if (checkTime() && !fetchXml(this.subReddit)) {
            return null;
        }
        
        int index;
        int redditPostCount = this.redditPost.size();

        if (indexn == null || indexn > redditPostCount && indexn != 0) {
            Random rand = new Random();
            index = rand.nextInt(redditPostCount);
        } else {
            index = indexn;
        }

        return redditPost.get(index);

    }

    private boolean checkTime(){
        return this.nextFetch == null 
                || ZonedDateTime.now(ZoneId.of("UTC")).isAfter(this.nextFetch);
    }

    private boolean fetchXml(String sreddit){
        Document redditRss;
        int redditPostCount;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            URLConnection connectionHandle = new URL("https://reddit.com/r/"
                    +sreddit
                    +".rss")
                    .openConnection();
            
            connectionHandle.setRequestProperty("User-Agent"
                    , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                    +"AppleWebKit/537.36 (KHTML, like Gecko) "
                    +"Chrome/84.0.4147.89 Safari/537.36");
            
            redditRss = dBuilder.parse(connectionHandle.getInputStream());
            
        } catch (IOException | SAXException | ParserConfigurationException e) {
            System.out.println("Can't fetch https://reddit.com/r/"
                    +sreddit
                    +".rss");
            return false;
        }
        
        ZonedDateTime lastFetch = ZonedDateTime.now(ZoneId.of("UTC"));
        
        if (lastFetch.getHour() >= FETCH_REFRESH_HOUR) {
            this.nextFetch = lastFetch.plusDays(1).withHour(FETCH_REFRESH_HOUR);
        } else {
            this.nextFetch = lastFetch.withHour(FETCH_REFRESH_HOUR);
        }

        this.redditPost.clear();
        redditPostCount = 0;

        NodeList items = redditRss.getElementsByTagName("content");
        NodeList titles = redditRss.getElementsByTagName("title");
        NodeList authors = redditRss.getElementsByTagName("name");
        

        for(int i = 0; i < items.getLength(); i++) {
            String link = getLink(items.item(i).getTextContent());
            if (link != null){
                String[] post = {titles.item(i+1).getTextContent()
                        , authors.item(i).getTextContent(), link};
                this.redditPost.put(i, post);
                redditPostCount++;
            }
        }
        if (redditPostCount == 0 || this.redditPost.isEmpty()) {
            System.out.println("Nothing fetched.");
            return false;
        }
        return true;
    }

    private String getLink(String search) {
        Matcher matcher = Pattern.compile(LINK_PATTERN).matcher(search);
        matcher.find();
        String link = matcher.group();
        if (link == null) {
            return null;
        }
        return link.substring(15, link.length() - 8);
    }

    private static Integer getFetchHour() {
        try {
            int hour = new Integer(ConfigFile.getRedditProperty("fetchhour"));
            if (hour < 0 || hour > 24) {
                return 6;
            }
            return hour;
        } catch(NumberFormatException e){
            return 6;
        }
    }
}