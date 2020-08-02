package com.chame.jamadeus;

import com.chame.jamadeus.utils.*;
import com.chame.jamadeus.commands.*;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.*;

import java.util.List;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class Listener extends ListenerAdapter{
    private static String[] commList;
    public static final String trigger = ConfigFile.getDiscordProperty("trigger").replaceAll("\\s+", "");
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public Listener() {
        final String[] commandArray = {"ping", "cursedimg", "gae", "pp", "srdt", "imgurtag", "help", "h"};
        List<String> commandList = new ArrayList<String>();

        for (String command : commandArray){
            String textCommand = trigger + command;
            commandList.add(textCommand);
        }

        commList = commandList.toArray(new String[0]);
        commandList = null;
        CommandExecutor.initialize(commList);
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getMember() == null || event.getAuthor().equals(Jamadeus.getJda().getSelfUser())) 
            return;

        Message msg = event.getMessage();
        String pCommandCall = getFirstWord(msg.getContentRaw().toLowerCase());

        for (String command : commList){
            if (pCommandCall.equals(command)){
                FutureTask<Boolean> task = new FutureTask<Boolean>(new CommandExecutor(command, new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember())));
                executor.execute(task);
                return;
            }
        }
        
        if (event.isFromType(ChannelType.PRIVATE))
        {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                                    event.getMessage().getContentDisplay());
        }
        else
        {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                        event.getMessage().getContentDisplay());
        }
    }

    private String getFirstWord(String text) {
        int index = text.indexOf(' ');
        if (index > -1) {
            return text.substring(0, index).trim();
        } else {
            return text;
        }
    }
}