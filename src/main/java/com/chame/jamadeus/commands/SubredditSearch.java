package com.chame.jamadeus.commands;

import com.chame.jamadeus.utils.ConfigFile;

import com.chame.jamadeus.Listener;
import com.chame.jamadeus.scrappers.Reddit;
import com.chame.jamadeus.utils.DiscordParameters;

public class SubredditSearch implements Command{
    private static final String PREFIX = Listener.TRIGGER;
    private static final String[] NSFW_WORDLIST 
            = ConfigFile.getRedditNsfwWordList();
    
    private static final Boolean NSFW_EVERYWHERE 
            = Boolean.parseBoolean(ConfigFile
            .getRedditProperty("nsfweverywhere"));
    
    private static final String NO_PARAMETER_MSG 
            = ">>> No subreddit specified. Example usage: \n"
            +"`/srdt cursedimages`\n"
            +"You can optionally include a post number:\n"
            +"`/srdt cursedimages 1`"
            .replace("/", PREFIX);
    
    public SubredditSearch() {
    }

    @Override
    public void call(DiscordParameters parameters){
        String searchsbreddit;
        Integer pageNumber;
        String[] splitMsg 
                = parameters.getMessage()
                .getContentRaw()
                .toLowerCase()
                .split(" ");

        try {
            searchsbreddit = splitMsg[1].replace("r/", "");
        } catch (ArrayIndexOutOfBoundsException e) {
            parameters.sendMessage(NO_PARAMETER_MSG);
            return;
        }

        try {
            pageNumber = new Integer(splitMsg[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            pageNumber = null;
        }

        if (!NSFW_EVERYWHERE 
                && !parameters.getMessage().getTextChannel().isNSFW()) {
            for (String nsfwWord : NSFW_WORDLIST){
                if(searchsbreddit.contains(nsfwWord)){
                    parameters.sendMessage(">>> No *NSFW* subreddits allowed in"
                            +" non-NSFW channels.");
                    return;
                }
            }
        }

        String[] fetchResult 
                = new Reddit(searchsbreddit).getSubReddit(pageNumber);

        if (fetchResult == null) {
            parameters.sendMessage(">>> Couldn't fetch subreddit r/"
                    +searchsbreddit
                    +"\nMake sure it exists, or try again later.");
            return;
        }

        parameters.sendMessage(String.format(">>> **%s**\nby *%s*\n\n%s", 
                fetchResult[0], fetchResult[1], 
                fetchResult[2]));
    }
}