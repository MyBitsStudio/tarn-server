package com.ruse.world.content.discordbot;
import java.awt.Color;
import java.util.List;
import java.util.Optional;

import com.ruse.GameSettings;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.PlayerHandler;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.util.logging.ExceptionLogger;


/**
 * @author Patrity || https://www.rune-server.ee/members/patrity/
 */

public class JavaCord {

    private static String token = "MTExNzIyMjQxMTAyMTA1ODE5Mw.GYHWOp.EJ92xFeHg1tZjfsJaomNDoGZMv1wjVZBB_69Xs";

    private static String serverName = "Tarn";

    private static DiscordApi api = null;

    public static void init() {
        new DiscordApiBuilder().setToken(token).login().thenAccept(api -> {
                    JavaCord.api = api;
                    //System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
                    if (GameSettings.LOCALHOST)
                        return;
                    api.addMessageCreateListener(event -> {

                        if (event.getMessageContent().equalsIgnoreCase("::players")) {
                            int online = World.getPlayers().size();
                            event.getChannel().sendMessage("Players currently online: "+online);
                        }

                        if (event.getMessageContent().equalsIgnoreCase("::online")) {
                            event.getChannel().sendMessage(":tada: "+serverName+" is Online! :tada:");
                        }

                    });
                })
                // Log any exceptions that happened
                .exceptionally(ExceptionLogger.get());
    }

    public static void sendMessage(long channel, String msg) {
        try {
            new MessageBuilder()
                    .append(msg)
                    .send((TextChannel) api.getChannelById(channel).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}