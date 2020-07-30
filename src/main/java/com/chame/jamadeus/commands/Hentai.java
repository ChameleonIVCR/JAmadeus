package com.chame.jamadeus.commands;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.Listener;
import com.chame.jamadeus.utils.ConfigFile;
import com.chame.jamadeus.scrappers.Booru;
import com.chame.jamadeus.utils.DiscordParameters;

import java.util.Random;
import java.util.concurrent.Callable;

import net.dv8tion.jda.api.entities.Message;

public class Hentai implements Command{
    private static final String prefix = Listener.trigger;
    private static final Boolean nsfwEverywhere = Boolean.parseBoolean(ConfigFile.getBooruProperty("nsfweverywhere"));
    private static final String noParameterMsg = ">>> No search specified. Example usage: \n`/h fate`\nYou can optionally include a post number:\n`/h fate 1`".replace("/", prefix);
    
    public Hentai() {
    }

    public void call(DiscordParameters parameters){
        String hentaiSearch;
        Integer pageNumber;
        Message msg = parameters.getMessage();
        Boolean safe = nsfwEverywhere || msg.getTextChannel().isNSFW() ? false : true;

        try {
            hentaiSearch = msg.getContentRaw().toLowerCase().split(" ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            parameters.sendMessage(noParameterMsg);
            return;
        }

        try {
            pageNumber = new Integer(msg.getContentRaw().split(" ")[2]);
        } catch (Exception e) { // not a number
            pageNumber = null;
        }

        String[] fetchResult = new Booru(hentaiSearch).getBooru(pageNumber, safe);

        if (fetchResult == null) {
            parameters.sendMessage(">>> Got no results for search "+hentaiSearch+"\nMake sure it exists, or try again later.");
            return;
        }

        parameters.sendMessage(String.format(">>> **%s** in *%s*\nby *%s*\n\n%s", fetchResult[0], fetchResult[1], fetchResult[2], fetchResult[3]));
        return;
    }
}