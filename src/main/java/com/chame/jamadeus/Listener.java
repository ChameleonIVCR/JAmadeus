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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.Callable;

public class Listener extends ListenerAdapter{
    private static String[] commList;
    public static String trigger;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public Listener() {
        final String[] commandArray = {"ping", "cursedimg", "gae", "pp", "srdt", "imgurtag", "help"};
        trigger = ConfigFile.getDiscordProperty("trigger").replaceAll("\\s+", "");
        List<String> commandList = new ArrayList<String>();

        for (String command : commandArray){
            String textCommand = trigger + command;
            commandList.add(textCommand);
        }

        commList = commandList.toArray(new String[0]);
        commandList = null;
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getMember() == null || event.getAuthor().equals(Jamadeus.getJda().getSelfUser())) 
            return;

        Message msg = event.getMessage();
        String pCommandCall = getFirstWord(msg.getContentRaw().toLowerCase());
        
        if (pCommandCall.equals(commList[0])){
            FutureTask<String> commandTask = new FutureTask<String>(new Ping(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember())));
            executor.execute(commandTask);
            return;
        } else if (pCommandCall.equals(commList[1])){
            FutureTask<String> commandTask2 = new FutureTask<String>(new CursedImg(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember())));
            executor.execute(commandTask2);
            return;
        } else if (pCommandCall.equals(commList[2])){
            FutureTask<String> commandTask3 = new FutureTask<String>(new BigGae(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember())));
            executor.execute(commandTask3);
            return;
        } else if (pCommandCall.equals(commList[3])){
            FutureTask<String> commandTask4 = new FutureTask<String>(new PpTest(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember())));
            executor.execute(commandTask4);
            return;
        } else if (pCommandCall.equals(commList[4])){
            FutureTask<String> commandTask5 = new FutureTask<String>(new SubredditSearch(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember())));
            executor.execute(commandTask5);
            return;
        } else if (pCommandCall.equals(commList[5])){
            FutureTask<String> commandTask6 = new FutureTask<String>(new ImgurtagSearch(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember())));
            executor.execute(commandTask6);
            return;
        } else if (pCommandCall.equals(commList[6])){
            FutureTask<String> commandTask7 = new FutureTask<String>(new Help(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember())));
            executor.execute(commandTask7);
            return;
        }
        // for (String command : commList){
        //     if (getFirstWord(msg.getContentRaw().toLowerCase()).equals(command)){
        //         Callable callabe1 = new CursedImg(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember()));
        //         FutureTask<String> futureTask1 = new FutureTask<String>(callabe1);

        //         executor.execute(futureTask1);
        //         //commands.get(command).start(new DiscordParameters(msg, event.getChannel(), event.getGuild(), event.getMember()));
        //         return;
        //     }
        // }
        
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