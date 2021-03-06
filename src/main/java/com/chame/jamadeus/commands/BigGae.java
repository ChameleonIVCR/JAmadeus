package com.chame.jamadeus.commands;

import com.chame.jamadeus.utils.DiscordParameters;

import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class BigGae implements Command{

    public BigGae() {
    }

    @Override
    public void call(DiscordParameters parameters){
        List<Member> mentionedUsers 
                = parameters.getMessage()
                .getMentionedMembers();
        
        if (!mentionedUsers.isEmpty()) {
            for (int i = 0; i < mentionedUsers.size(); i++) {
                Member currentMember = mentionedUsers.get(i);
                int gaeCounter 
                        = Integer.parseInt(currentMember
                        .getId().substring(0, 2));
                
                sendGaeStatus(gaeCounter, currentMember, parameters);
            }
        } else {
            Member currentMember = parameters.getMember();
            int gaeCounter 
                    = Integer.parseInt(currentMember
                    .getId().substring(0, 2));
            
            sendGaeStatus(gaeCounter, currentMember, parameters);
        }
    }

    private boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private void sendGaeStatus(int gaeStatus, Member member
                               , DiscordParameters parameters){
        if (gaeStatus == 0){
            
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"is **0%** gae and is looking for some side hoes."
                    , member.getEffectiveName()));
            
        } else if (isBetween(gaeStatus, 0, 25)){
            
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"is as straight as an *arrow* :arrow_right: \n\n"
                    +" Totally **not** gae."
                    , member.getEffectiveName()));
            
        } else if (isBetween(gaeStatus, 26, 50)){
            
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"is **slightly** gae, you better watch out."
                    , member.getEffectiveName()));
            
        } else if (isBetween(gaeStatus, 51, 75)){
            
            parameters.sendMessage(String.format(">>> **%s**\nis **gae**."
                    , member.getEffectiveName()));
            
        } else if (isBetween(gaeStatus, 76, 98)){
            
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"is **gae lord** and shall rule over all gae."
                    , member.getEffectiveName()));
            
        } else if (gaeStatus == 99) {
            
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"is as gay as it gets\n\n"
                    +"You shall be awarded the title of **gae king**.\n"
                    +"Your gae powers include bending dongs up to 99 degrees."
                    , member.getEffectiveName()));
        }
    }

}