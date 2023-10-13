package com.ruse.world.packages.discord.impl.admin;

import com.ruse.world.packages.discord.MessageBot;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.server.ServerBecomesAvailableListener;

public class AdminBot extends MessageBot {

    public static long ACTIVITY = 1116222317937311835L, TRADE = 1116222330931265607L, DROPS = 1116222355673464883L,
    NEW_PLAYERS = 1116222396756676760L, PUNISHMENTS = 1116230613507256371L, SECURITY = 1116230759225765968L,
    DEALS = 1116230874170667028L, ALERT = 1116222441614745610L, COMMANDS = 1116230874170667028L;

    @Override
    public String token() {
        return "MTEwOTMzNTM1MzM5NzA4NDI1MA.GH5DjI.hNNNf3dGGTKr79pLp0-Ki3WY8zS1njWwuxFNo4";
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
