package com.chame.jamadeus.commands;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.Listener;
import com.chame.jamadeus.utils.DiscordParameters;

import java.util.concurrent.Callable;

public class Help implements Callable<String>{
    private DiscordParameters parameters;
    private String prefix;

    public Help(DiscordParameters parameters) {
        this.parameters = parameters;
        this.prefix = Listener.trigger;
    }

    public String call() throws Exception{
        parameters.sendMessage(">>> My commands are:\n`"+prefix+"ping` returns the ping to Discord servers.\n`"+prefix+"cursedimg` returns a random cursed image.\n`"+prefix+"gae`\n`"+prefix+"pp`\n`"+prefix+"srdt`\n`"+prefix+"imgurtag`");
        return null;
    }
}