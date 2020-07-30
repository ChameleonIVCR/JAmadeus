package com.chame.jamadeus.commands;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.scrappers.Reddit;
import com.chame.jamadeus.scrappers.Imgur;
import com.chame.jamadeus.utils.DiscordParameters;

import java.util.Random;
import java.util.concurrent.Callable;

public class ImgurtagSearch implements Callable<String>{
    private Imgur imgurHandle;
    private DiscordParameters parameters;
    
    public ImgurtagSearch(DiscordParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public String call() throws Exception{
        String searchImgurTag;
        Integer imgNumber;
        int pageNumber;

        try {
            searchImgurTag = parameters.getMessage().getContentRaw().toLowerCase().split(" ")[1].replace("#", "");
        } catch (ArrayIndexOutOfBoundsException e) {
            parameters.sendMessage(">>> No Imgur tag specified. Example usage: \n`/imgurtag cursed`\nYou can optionally include a image number:\n`/imgurtag cursed 1`\nYou can include a page number too:\n`/imgurtag cursed 1 2`");
            return null;
        }

        try {
            imgNumber = new Integer(parameters.getMessage().getContentRaw().split(" ")[2]);
        } catch (Exception e) {
            imgNumber = null;
        }

        try {
            pageNumber = new Integer(parameters.getMessage().getContentRaw().split(" ")[3]);
        } catch (Exception e) {
            pageNumber = 1;
        }

        this.imgurHandle = new Imgur(searchImgurTag);

        String[] fetchResult;
        fetchResult = imgurHandle.getAlbum(imgNumber, pageNumber);

        if (fetchResult == null) {
            parameters.sendMessage(">>> Couldn't fetch Imgur tag #"+searchImgurTag+"\nMake sure it exists, or try again later.");
            return null;
        }

        parameters.sendMessage(String.format(">>> **%s**\nby *%s*\n\n*%s*\n:eyes: %s\n%s", fetchResult[0], fetchResult[1], fetchResult[3], fetchResult[2], fetchResult[4]));
        return null;
    }
}