package com.ruse.world.packages.discord.impl.admin;

import com.ruse.world.packages.discord.MessageBot;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.server.ServerBecomesAvailableListener;

public class AdminBot extends MessageBot {
    @Override
    public String token() {
        return "MTEwOTMzNTM1MzM5NzA4NDI1MA.G5fNJg.uY3QIWhjz9hqqVUnxyAgJcG8Ae6iAiQKrBS80I";
    }

    @Override
    public String name() {
        return "ADMIN";
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
