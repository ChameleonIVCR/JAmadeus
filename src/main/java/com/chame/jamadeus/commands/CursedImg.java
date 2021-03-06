package com.chame.jamadeus.commands;

import com.chame.jamadeus.scrappers.Reddit;
import com.chame.jamadeus.scrappers.Imgur;
import com.chame.jamadeus.utils.DiscordParameters;

import java.util.Random;

public class CursedImg implements Command{
    private static final Reddit REDDIT_HANDLE = new Reddit("cursedimages");
    private static final Reddit REDDIT_HANDLE_2 = new Reddit("cursed_Images");
    private static final Imgur IMGUR_HANDLE  = new Imgur("cursed");

    public CursedImg(){

    }
    
    @Override
    public void call(DiscordParameters parameters){
        String[] fetchResult;
        switch (new Random().nextInt(2)) {
            case 1:
                fetchResult = REDDIT_HANDLE.getSubReddit(null);
                break;
            case 2:
                fetchResult = REDDIT_HANDLE_2.getSubReddit(null);
                break;
            default:
                fetchResult = IMGUR_HANDLE.getAlbum(null, 1);
                break;
        }
        if (fetchResult == null) {
            parameters.sendMessage("Couldn't fetch subreddit 'CursedImages'");
            return;
        }
        if (fetchResult.length == 5){
            parameters.sendMessage(String.format(">>> **%s**\nby *%s*\n\n*%s*\n"
                    +":eyes: %s\n%s", fetchResult[0], 
                    fetchResult[1], fetchResult[3], 
                    fetchResult[2], fetchResult[4]));
        } else {
            parameters.sendMessage(String.format(">>> **%s**\nby *%s*\n\n%s", 
                    fetchResult[0], fetchResult[1], 
                    fetchResult[2]));
        }
    }
}