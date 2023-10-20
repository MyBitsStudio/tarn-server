package com.ruse.world.packages.discord;

import com.ruse.world.packages.discord.impl.NormalBot;
import com.ruse.world.packages.discord.impl.admin.AdminBot;
import com.ruse.world.packages.discord.modal.MessageCreate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BotManager {

    private static BotManager instance;

    public static BotManager getInstance() {
        if (instance == null) {
            instance = new BotManager();
        }
        return instance;
    }

    private final List<MessageBot> bots = new ArrayList<>();

    public void init(){
        addBots();
        bots.stream().filter(Objects::nonNull).forEach(MessageBot::init);
    }

    private void addBots(){
        bots.add(new NormalBot());
        bots.add(new AdminBot());
    }

    public void sendMessage(String bot, long channel, MessageCreate msg){
        bots.stream().filter(b -> b.name().equalsIgnoreCase(bot)).forEach(b -> b.sendMessage(channel, msg));
    }
}

