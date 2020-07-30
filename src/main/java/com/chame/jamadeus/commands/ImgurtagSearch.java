package com.chame.jamadeus.commands;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.Listener;
import com.chame.jamadeus.scrappers.Imgur;
import com.chame.jamadeus.utils.DiscordParameters;

import java.util.Random;
import java.util.concurrent.Callable;

import net.dv8tion.jda.api.entities.Message;

public class ImgurtagSearch implements Command{
    private static final String prefix = Listener.trigger;
    private static final String noParameterMsg = ">>> No Imgur tag specified. Example usage: \n`/imgurtag cursed`\nYou can optionally include a image number:\n`/imgurtag cursed 1`\nYou can include a page number too:\n`/imgurtag cursed 1 2`".replace("/", prefix);
    
    public ImgurtagSearch() {
    }

    public void call(DiscordParameters parameters){
        String searchImgurTag;
        Integer imgNumber;
        int pageNumber;
        Message msg = parameters.getMessage();

        try {
            searchImgurTag = msg.getContentRaw().toLowerCase().split(" ")[1].replace("#", "");
        } catch (ArrayIndexOutOfBoundsException e) {
            parameters.sendMessage(noParameterMsg);
            return;
        }

        try {
            imgNumber = new Integer(msg.getContentRaw().split(" ")[2]);
        } catch (Exception e) {
            imgNumber = null;
        }

        try {
            pageNumber = new Integer(msg.getContentRaw().split(" ")[3]);
        } catch (Exception e) {
            pageNumber = 1;
        }

        String[] fetchResult = new Imgur(searchImgurTag).getAlbum(imgNumber, pageNumber);

        if (fetchResult == null) {
            parameters.sendMessage(">>> Couldn't fetch Imgur tag #"+searchImgurTag+"\nMake sure it exists, or try again later.");
            return;
        }

        parameters.sendMessage(String.format(">>> **%s**\nby *%s*\n\n*%s*\n:eyes: %s\n%s", fetchResult[0], fetchResult[1], fetchResult[3], fetchResult[2], fetchResult[4]));
        return;
    }
}