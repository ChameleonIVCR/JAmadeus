package com.chame.jamadeus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import com.chame.jamadeus.utils.ConfigFile;

public class Jamadeus{
    private static JDA jda;
    public static void main(String[] args) throws Exception {
        ConfigFile.initialize();
        jda = JDABuilder.createLight(ConfigFile.getBotToken()).addEventListeners(new Listener()).setActivity(Activity.listening(ConfigFile.getDiscordProperty("trigger").replaceAll("\\s+", "")+"help")).build();
    }

    public static JDA getJda(){
        return jda;
    }
}
