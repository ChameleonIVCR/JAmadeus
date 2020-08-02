package com.chame.jamadeus.utils;

import com.chame.jamadeus.Jamadeus;

import java.io.*;
import java.util.*;
import java.util.HashMap;

public class ConfigFile{
    private static String botToken;
    private static HashMap<String, Properties> propertiesStorage;

    public static String getBotToken(){
        return botToken;
    }

    //probably unused
    public static String getDiscordProperty(String property){
        Properties properties = propertiesStorage.get("discord");
        return properties.getProperty(property);
    }

    public static String getBooruProperty(String property){
        Properties properties = propertiesStorage.get("booru");
        return properties.getProperty(property);
    }

    public static String getImgurProperty(String property){
        Properties properties = propertiesStorage.get("imgur");
        return properties.getProperty(property);
    }

    public static String getRedditProperty(String property){
        Properties properties = propertiesStorage.get("reddit");
        return properties.getProperty(property);
    }

    public static String[] getRedditNsfwWordList(){
        Properties properties = propertiesStorage.get("reddit");
        return properties.getProperty("nsfwlist").split(",");
    }

    public static String getR34Property(String property){
        Properties properties = propertiesStorage.get("r34");
        return properties.getProperty(property);
    }

    public static void initialize(){
        propertiesStorage = new HashMap();
        final String [] configFiles = {"discord", "reddit"
                , "r34", "imgur", "booru"};
        
        for (String configurationFile : configFiles){
            propertiesStorage.put(configurationFile
                    , loadProperties(configurationFile));
        }
        
        Properties discordProperties = propertiesStorage.get("discord");
        botToken = discordProperties.getProperty("botToken");
    }

    private static Properties loadProperties(String name){
        String nameProperties = "config/"+name+".properties";
        File propFile = new File(nameProperties);
        Properties properties = new Properties();
        Properties defProperties = new Properties();

        try {
            File directory = new File("config/");
            directory.mkdir();
            defProperties.load(Jamadeus.class
                    .getResourceAsStream("/"+nameProperties));
            
            if (propFile.exists()){
                FileInputStream fileis = new FileInputStream(propFile);
                properties.load(fileis);
                fileis.close();
                if (!properties.stringPropertyNames()
                        .equals(defProperties.stringPropertyNames())){
                    
                    if(!propFile.delete()){
                        throw new IOException();
                    }
                    FileOutputStream fileos = new FileOutputStream(propFile);
                    defProperties.store(fileos, name+" properties");
                    fileos.close();
                    return defProperties;
                } else {
                    return properties;
                }
            } else {
                FileOutputStream fileos = new FileOutputStream(propFile);
                defProperties.store(fileos, name+" properties");
                fileos.close();
                return defProperties;
            }
        } catch(FileNotFoundException e) {
            System.out.println("File not found");
            return null;
        } catch(IOException e) {
            System.out.println("Can't write to storage, "
                    +"check the file system permissions");
            return null;
        }
    }
}