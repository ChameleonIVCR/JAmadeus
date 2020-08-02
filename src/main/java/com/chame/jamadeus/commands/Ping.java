package com.chame.jamadeus.commands;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.utils.DiscordParameters;

public class Ping implements Command{

    public Ping() {

    }

    @Override
    public void call(DiscordParameters parameters){
        parameters.sendMessage(String.format(">>> Pong!, my current ping to "
                +"Discord servers is %d ms"
                , Jamadeus.getJda().getGatewayPing()));
    }
}