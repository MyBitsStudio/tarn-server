package com.ruse.world.packages.discord.impl;

import com.ruse.world.packages.discord.MessageBot;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.server.ServerBecomesAvailableListener;

public class NormalBot extends MessageBot {
    @Override
    public String token() {
        return "MTExNjc1MzgzODE5NjY2MjM1Mg.GRs7Vx.51mftjoy9oDNGuWx6KsI1WaaLIw28Im_GORh6E";
    }

    @Override
    public String name() {
        return "NORMAL";
    }

    @Override
    public MessageCreateListener messageListener() {
        return null;
    }

    @Override
    public SlashCommandCreateListener slashListener() {
        return null;
    }

    @Override
    public ServerBecomesAvailableListener serverListener() {
        return null;
    }

    @Override
    public ReactionAddListener reactionListener() {
        return null;
    }
}
