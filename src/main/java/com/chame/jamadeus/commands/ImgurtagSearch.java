package com.chame.jamadeus.commands;

import com.chame.jamadeus.Listener;
import com.chame.jamadeus.scrappers.Imgur;
import com.chame.jamadeus.utils.DiscordParameters;

public class ImgurtagSearch implements Command{
    private static final String PREFIX = Listener.TRIGGER;
    private static final String NO_PARAMETER_MSG 
            = ">>> No Imgur tag specified. Example usage: \n"
            +"`/imgurtag cursed`\n"
            +"You can optionally include a image number:\n"
            +"`/imgurtag cursed 1`\n"
            +"You can include a page number too:\n"
            +"`/imgurtag cursed 1 2`"
            .replace("/", PREFIX);
    
    public ImgurtagSearch() {
    }

    @Override
    public void call(DiscordParameters parameters){
        String searchImgurTag;
        Integer imgNumber;
        int pageNumber;
        String[] splitMsg 
                = parameters.getMessage()
                .getContentRaw()
                .toLowerCase()
                .split(" ");

        try {
            searchImgurTag = splitMsg[1].replace("#", "");
        } catch (ArrayIndexOutOfBoundsException e) {
            parameters.sendMessage(NO_PARAMETER_MSG);
            return;
        }

        try {
            imgNumber = new Integer(splitMsg[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            imgNumber = null;
        }

        try {
            pageNumber = new Integer(splitMsg[3]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            pageNumber = 1;
        }

        String[] fetchResult = new Imgur(searchImgurTag)
                .getAlbum(imgNumber, pageNumber);

        if (fetchResult == null) {
            parameters.sendMessage(">>> Couldn't fetch Imgur tag #"
                +searchImgurTag
                +"\nMake sure it exists, or try again later.");
            
            return;
        }

        parameters.sendMessage(String.format(">>> **%s**\nby *%s*\n\n*%s*\n"
                +":eyes: %s\n%s", fetchResult[0], 
                fetchResult[1], fetchResult[3], 
                fetchResult[2], fetchResult[4]));
    }
}