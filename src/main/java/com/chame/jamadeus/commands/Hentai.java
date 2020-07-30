package com.chame.jamadeus.commands;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.scrappers.Booru;
import com.chame.jamadeus.utils.DiscordParameters;

import java.util.Random;
import java.util.concurrent.Callable;

public class Hentai implements Callable<String>{
    private static final Reddit redditHandle = new Booru(null);
    private DiscordParameters parameters;
    
    public Hentai(DiscordParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public String call() throws Exception{
        String[] fetchResult;
        int random = new Random().nextInt(2);
        if (random == 0){
            fetchResult = redditHandle.getSubReddit(null);
        } else if (random == 2) {
            fetchResult = redditHandle2.getSubReddit(null);
        } else {
            fetchResult = imgurHandle.getAlbum(null, 1);
        }
        if (fetchResult == null) {
            parameters.sendMessage("Couldn't fetch subreddit 'CursedImages'");
            return null;
        }
        if (fetchResult.length == 5){
            parameters.sendMessage(String.format(">>> **%s**\nby *%s*\n\n*%s*\n:eyes: %s\n%s", fetchResult[0], fetchResult[1], fetchResult[3], fetchResult[2], fetchResult[4]));
        } else {
            parameters.sendMessage(String.format(">>> **%s**\nby *%s*\n\n%s", fetchResult[0], fetchResult[1], fetchResult[2]));
        }
        return null;
    }
}