package com.chame.jamadeus.commands;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.Listener;
import com.chame.jamadeus.utils.DiscordParameters;

import java.util.concurrent.Callable;

public class Help implements Command{
    private static final String prefix = Listener.trigger;
    private static final String helpMsg = ">>> My commands are:\n`"+prefix+"ping` returns the ping to Discord servers.\n`"+prefix+"cursedimg` returns a random cursed image.\n`"+prefix+"gae`\n`"+prefix+"pp`\n`"+prefix+"srdt`\n`"+prefix+"imgurtag`";

    public Help() {
    }

    public void call(DiscordParameters parameters){
        parameters.sendMessage(helpMsg);
        return;
    }
}