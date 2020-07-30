package com.chame.jamadeus.commands;

import net.dv8tion.jda.api.JDA;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.utils.DiscordParameters;

import java.util.concurrent.Callable;

public class Ping implements Command{

    public Ping() {
        //empty
    }

    public void call(DiscordParameters parameters){
        parameters.sendMessage(String.format(">>> Pong!, my current ping to Discord servers is %d ms", Jamadeus.getJda().getGatewayPing()));
        return;
    }
}