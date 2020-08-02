package com.chame.jamadeus.commands;

import com.chame.jamadeus.utils.DiscordParameters;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Callable;

public class CommandExecutor implements Callable<Boolean>{
    private static ConcurrentHashMap<String, Command> commandStorage;
    private final String command;
    private final DiscordParameters parameters;

    public CommandExecutor(String command, DiscordParameters parameters){
        this.command = command;
        this.parameters = parameters;
    }

    @Override
    public Boolean call() throws Exception{
        Constructor<?> constructor = commandStorage.get(this.command).getClass().getDeclaredConstructor();
        Command runnable = (Command) constructor.newInstance();
        runnable.call(this.parameters);
        runnable = null;
        return true;
    }

    public static void initialize(String[] commands){
        commandStorage = new ConcurrentHashMap<>();
        commandStorage.put(commands[0], new Ping());
        commandStorage.put(commands[1], new CursedImg());
        commandStorage.put(commands[2], new BigGae());
        commandStorage.put(commands[3], new PpTest());
        commandStorage.put(commands[4], new SubredditSearch());
        commandStorage.put(commands[5], new ImgurtagSearch());
        commandStorage.put(commands[6], new Help());
        commandStorage.put(commands[7], new Hentai());
    }
}