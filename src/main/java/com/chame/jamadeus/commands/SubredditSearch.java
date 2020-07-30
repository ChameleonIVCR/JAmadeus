package com.chame.jamadeus.commands;

import com.chame.jamadeus.utils.ConfigFile;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.scrappers.Reddit;
import com.chame.jamadeus.scrappers.Imgur;
import com.chame.jamadeus.utils.DiscordParameters;

import net.dv8tion.jda.api.entities.Message;

import java.util.Random;
import java.util.concurrent.Callable;

public class SubredditSearch implements Callable<String>{
    private static final String[] nsfwWordList = ConfigFile.getRedditNsfwWordList();
    private static final Boolean nsfwEverywhere = Boolean.parseBoolean(ConfigFile.getRedditProperty("nsfweverywhere"));
    private Reddit redditHandle;
    private DiscordParameters parameters;
    
    public SubredditSearch(DiscordParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public String call() throws Exception{
        String searchsbreddit;
        Integer pageNumber;
        Message msg = parameters.getMessage();

        try {
            searchsbreddit = msg.getContentRaw().toLowerCase().split(" ")[1].replace("r/", "");
        } catch (ArrayIndexOutOfBoundsException e) {
            parameters.sendMessage(">>> No subreddit specified. Example usage: \n`/srdt cursedimages`\nYou can optionally include a post number:\n`/srdt cursedimages 1`");
            return null;
        }

        try {
            pageNumber = new Integer(msg.getContentRaw().split(" ")[2]);
        } catch (Exception e) { // not a number
            pageNumber = null;
        }

        if (!nsfwEverywhere && !msg.getTextChannel().isNSFW()) {
            for (String nsfwWord : nsfwWordList){
                if(searchsbreddit.contains(nsfwWord)){
                    parameters.sendMessage(">>> No *NSFW* subreddits allowed in non-NSFW channels.");
                    return null;
                }
            }
        }

        this.redditHandle = new Reddit(searchsbreddit);

        String[] fetchResult;
        fetchResult = redditHandle.getSubReddit(pageNumber);

        if (fetchResult == null) {
            parameters.sendMessage(">>> Couldn't fetch subreddit r/"+searchsbreddit+"\nMake sure it exists, or try again later.");
            return null;
        }

        parameters.sendMessage(String.format(">>> **%s**\nby *%s*\n\n%s", fetchResult[0], fetchResult[1], fetchResult[2]));
        return null;
    }
}