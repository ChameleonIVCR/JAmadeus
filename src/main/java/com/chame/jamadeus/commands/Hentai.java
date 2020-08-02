package com.chame.jamadeus.commands;

import com.chame.jamadeus.Listener;
import com.chame.jamadeus.utils.ConfigFile;
import com.chame.jamadeus.scrappers.Booru;
import com.chame.jamadeus.utils.DiscordParameters;

public class Hentai implements Command{
    private static final String PREFIX = Listener.TRIGGER;
    private static final Boolean NSFW_EVERYWHERE 
            = Boolean.parseBoolean(ConfigFile
            .getBooruProperty("nsfweverywhere"));
    
    private static final String NO_PARAMETER_MSG 
            = ">>> No search specified. Example usage: \n"
            +"`/h fate`\n"
            +"You can optionally include a post number:\n"
            +"`/h fate 1`"
            .replace("/", PREFIX);
    
    public Hentai() {
    }

    @Override
    public void call(DiscordParameters parameters){
        String hentaiSearch;
        Integer pageNumber;
        String[] splitMsg 
                = parameters.getMessage()
                .getContentRaw()
                .toLowerCase()
                .split(" ");
        Boolean safe 
                = !NSFW_EVERYWHERE 
                && !parameters.getMessage().getTextChannel().isNSFW();

        try {
            hentaiSearch = splitMsg[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            parameters.sendMessage(NO_PARAMETER_MSG);
            return;
        }

        try {
            pageNumber = new Integer(splitMsg[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            pageNumber = null;
        }

        String[] fetchResult 
                = new Booru(hentaiSearch)
                .getBooru(pageNumber, safe);

        if (fetchResult == null) {
            parameters.sendMessage(">>> Got no results for search "
                    +hentaiSearch
                    +"\nMake sure it exists, or try again later.");
            return;
        }

        parameters.sendMessage(String.format(">>> **%s** in *%s*\n"
                +"by *%s*\n\n%s", 
                fetchResult[0], fetchResult[1], 
                fetchResult[2], fetchResult[3]));
    }
}