package com.chame.jamadeus;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.AccountType;
import com.chame.jamadeus.utils.ConfigFile;

public class Jamadeus{
    private static JDA jda;
    public static void main(String[] args) throws Exception {
        ConfigFile.initialize();
        jda = new JDABuilder(AccountType.BOT).setToken(ConfigFile.getBotToken()).addEventListeners(new Listener()).build();
    }

    public static JDA getJda(){
        return jda;
    }
}
