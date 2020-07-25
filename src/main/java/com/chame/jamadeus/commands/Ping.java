package com.chame.jamadeus.commands;

import net.dv8tion.jda.api.JDA;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.utils.DiscordParameters;

public class Ping implements Commands{
    public void processCommand(DiscordParameters parameters){
        parameters.sendMessage(String.format("Pong!, my current ping to Discord servers is %d ms", Jamadeus.getJda().getGatewayPing()));
    }
}