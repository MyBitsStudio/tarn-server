package com.ruse.world.packages.discord;

import com.ruse.io.ThreadProgressor;
import com.ruse.world.packages.discord.modal.MessageCreate;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;
import org.javacord.api.listener.server.ServerBecomesAvailableListener;

public abstract class MessageBot {

    public abstract String token();
    public abstract String name();

    public abstract MessageCreateListener messageListener();
    public abstract SlashCommandCreateListener slashListener();
    public abstract ServerBecomesAvailableListener serverListener();

    public abstract ReactionAddListener reactionListener();

    public DiscordApi api;

    public void init(){
        DiscordApiBuilder builder = new DiscordApiBuilder();
        builder.setToken(token());
        builder.setWaitForServersOnStartup(false);
        if(messageListener() != null)
            builder.addMessageCreateListener(messageListener());
        if(slashListener() != null)
            builder.addSlashCommandCreateListener(slashListener());
        if(serverListener() != null)
            builder.addServerBecomesAvailableListener(serverListener());
        if(reactionListener() != null)
            builder.addReactionAddListener(reactionListener());
        api = builder.login().join();
    }

    public void sendMessage(long channel, MessageCreate msg){
        ThreadProgressor.submit(false, () -> {
            try {
                msg.getMessage()
                        .send((TextChannel) api.getChannelById(channel).orElse(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

    }
}
