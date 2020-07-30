package com.chame.jamadeus.commands;

import net.dv8tion.jda.api.JDA;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.utils.DiscordParameters;

import java.util.concurrent.Callable;

public class Ping implements Callable<String>{
    private DiscordParameters parameters;

    public Ping(DiscordParameters parameters) {
        this.parameters = parameters;
    }

    public String call() throws Exception{
        parameters.sendMessage(String.format(">>> Pong!, my current ping to Discord servers is %d ms", Jamadeus.getJda().getGatewayPing()));
        return null;
    }
}