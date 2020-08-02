package com.chame.jamadeus.commands;

import com.chame.jamadeus.Listener;
import com.chame.jamadeus.utils.DiscordParameters;

public class Help implements Command{
    private static final String PREFIX = Listener.TRIGGER;
    private static final String HELP_MSG 
            = ">>> My commands are:\n`"
            +PREFIX+"ping` returns the ping to Discord servers.\n`"
            +PREFIX+"cursedimg` returns a random cursed image.\n`"
            +PREFIX+"gae` returns a gae score for the pinged user.\n`"
            +PREFIX+"pp` returns a pp score for the pinged user.\n`"
            +PREFIX+"srdt` returns the selected subreddit.\n`"
            +PREFIX+"imgurtag` returns the selected Imgur tag";

    public Help() {
    }

    @Override
    public void call(DiscordParameters parameters){
        parameters.sendMessage(HELP_MSG);
    }
}