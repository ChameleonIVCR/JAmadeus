package com.chame.jamadeus.commands;

import com.chame.jamadeus.utils.DiscordParameters;

public interface Command {
    void call (DiscordParameters parameters);
}