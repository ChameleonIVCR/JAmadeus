package com.chame.jamadeus.commands;

import com.chame.jamadeus.Jamadeus;
import com.chame.jamadeus.utils.DiscordParameters;

import net.dv8tion.jda.api.entities.Member;

import java.util.concurrent.Callable;
import java.util.List;

public class PpTest implements Callable<String>{

    private DiscordParameters parameters;

    public PpTest(DiscordParameters parameters) {
        this.parameters = parameters;
    }

    public String call() throws Exception{
        List<Member> mentionedUsers = parameters.getMessage().getMentionedMembers();
        if (!mentionedUsers.isEmpty()) {
            for (int i = 0; i < mentionedUsers.size(); i++) {
                Member currentMember = mentionedUsers.get(i);
                int ppCounter = Integer.parseInt(currentMember.getId().substring(2, 4));
                sendPpStatus(ppCounter, currentMember);
            }
        } else {
            Member currentMember = parameters.getMember();
            int ppCounter = Integer.parseInt(currentMember.getId().substring(2, 4));
            sendPpStatus(ppCounter, currentMember);
        }
        return null;
    }

    private boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private void sendPpStatus(int ppStatus, Member member){
        if (ppStatus == 0){
            parameters.sendMessage(String.format(">>> **%s**\nhas **no pp** at all, :mag: sorry king.", member.getEffectiveName()));
            return;
        } else if (isBetween(ppStatus, 0, 25)){
            parameters.sendMessage(String.format(">>> **%s**\nhas a **small pp**. sorry :pinching_hand:", member.getEffectiveName()));
            return;
        } else if (isBetween(ppStatus, 26, 50)){
            parameters.sendMessage(String.format(">>> **%s**\nhas an **average pp**. 5/10 :thumbsup:", member.getEffectiveName()));
            return;
        } else if (isBetween(ppStatus, 51, 75)){
            parameters.sendMessage(String.format(">>> **%s**\nhas a **big pp**. I rate it 7.5/10 :ok_hand:", member.getEffectiveName()));
            return;
        } else if (isBetween(ppStatus, 76, 98)){
            parameters.sendMessage(String.format(">>> **%s**\nhas a **giant pp**. Nice dick, bro :eggplant:", member.getEffectiveName()));
            return;
        } else if (ppStatus == 99) {
            parameters.sendMessage(String.format(">>> **%s**\nhas a **monstrous dong**, so big it broke the scale, 10/10. Congratulations :french_bread:", member.getEffectiveName()));
            return;
        }
    }

}