package com.chame.jamadeus;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.utils.*;
import com.chame.jamadeus.commands.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.*;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Listener extends ListenerAdapter{
    private final String[] commList;
    private final ConcurrentHashMap<String, Commands> commands;

    public Listener() {
        this.commands = new ConcurrentHashMap();
        final String[] commandArray = {"ping"};
        String trigger = ConfigFile.getDiscordProperty("trigger").replaceAll("\\s+", "");
        List<String> commandList = new ArrayList<String>();

        for (String command : commandArray){
            String textCommand = trigger + command;
            commandList.add(textCommand);
        }

        commands.put(trigger+"ping", new Ping());

        commList = commandList.toArray(new String[0]);
        commandList = null;
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getMember() == null || event.getAuthor().equals(Jamadeus.getJda().getSelfUser())) 
            return;

        Message msg = event.getMessage();

        for (String command : commList){
            if (getFirstWord(msg.getContentRaw().toLowerCase()).equals(command)){
                commands.get(command).processCommand(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember()));
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