package com.chame.jamadeus.commands;

import com.chame.jamadeus.utils.DiscordParameters;

import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class PpTest implements Command{

    public PpTest() {
    }

    @Override
    public void call(DiscordParameters parameters){
        List<Member> mentionedUsers 
                = parameters.getMessage()
                .getMentionedMembers();
        
        if (!mentionedUsers.isEmpty()) {
            for (int i = 0; i < mentionedUsers.size(); i++) {
                Member currentMember = mentionedUsers.get(i);
                int ppCounter 
                        = Integer.parseInt(currentMember
                        .getId().substring(2, 4));
                
                sendPpStatus(ppCounter, currentMember, parameters);
            }
        } else {
            Member currentMember = parameters.getMember();
            int ppCounter 
                    = Integer.parseInt(currentMember
                    .getId().substring(2, 4));
            
            sendPpStatus(ppCounter, currentMember, parameters);
        }
    }

    private boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private void sendPpStatus(int ppStatus, Member member
                              , DiscordParameters parameters){
        if (ppStatus == 0){
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"has **no pp** at all, :mag: sorry king."
                    , member.getEffectiveName()));
            
        } else if (isBetween(ppStatus, 0, 25)){
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"has a **small pp**. sorry :pinching_hand:"
                    , member.getEffectiveName()));
            
        } else if (isBetween(ppStatus, 26, 50)){
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"has an **average pp**. 5/10 :thumbsup:"
                    , member.getEffectiveName()));
            
        } else if (isBetween(ppStatus, 51, 75)){
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"has a **big pp**. I rate it 7.5/10 :ok_hand:"
                    , member.getEffectiveName()));
            
        } else if (isBetween(ppStatus, 76, 98)){
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"has a **giant pp**. Nice dick, bro :eggplant:"
                    , member.getEffectiveName()));
            
        } else if (ppStatus == 99) {
            parameters.sendMessage(String.format(">>> **%s**\n"
                    +"has a **monstrous dong**, so big it broke the scale,"
                    +" 10/10. Congratulations :french_bread:"
                    , member.getEffectiveName()));
            
        }
    }

}