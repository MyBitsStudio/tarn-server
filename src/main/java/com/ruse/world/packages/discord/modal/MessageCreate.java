package com.ruse.world.packages.discord.modal;

import org.javacord.api.entity.message.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

public class MessageCreate {

    private final List<String> messages;
    private final String[] args;

    private Embed embed;

    public MessageCreate(List<String> messages, String... args){
        this.messages = messages;
        this.args = args;
    }

    public MessageCreate(String message){
        this.messages = new ArrayList<>();
        this.messages.add(message);
        this.args = new String[0];
    }

    public MessageCreate(List<String> messages, Embed embed, String... args){
        this.messages = messages;
        this.embed = embed;
        this.args = args;
    }

    public MessageBuilder getMessage(){
        MessageBuilder builder = new MessageBuilder()
                .append(String.join("\n", messages));
        if(embed != null)
            builder.setEmbed(embed.getBuilder());
        return builder;
    }
}
