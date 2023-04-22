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

    private static String token = "OTIwOTEzMjUyMjk5NDYwNjA5.Gql2R2.KKP2ae3t9YeD_I7pVW2Mnf_UEhK7c_j-XWPyF8";

    private static String serverName = "Tarn";

    private static DiscordApi api = null;

    public static void init() {
        new DiscordApiBuilder().setToken(token).login().thenAccept(api -> {
                    JavaCord.api = api;
                    //System.out.println("You can invite the bot by using the following url: " + api.createBotInvite());
                    if (GameSettings.LOCALHOST)
                        return;
               // sendMessage("\uD83D\uDD14│\uD835\uDDF4\uD835\uDDF2\uD835\uDDFB\uD835\uDDF2\uD835\uDDFF\uD835\uDDEE\uD835\uDDF9", ":tada: **" + serverName + " is now online!** :tada:");
                    //sendMessage("general", serverName+" is now online!");
                    api.addMessageCreateListener(event -> {

                        if (event.getMessageContent().equalsIgnoreCase("::players")) {
                            int online = (int) World.getPlayers().size();
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

    public static void sendMessage(String channel, String msg) {
        try {
            new MessageBuilder()
                    .append(msg)
                    .send((TextChannel) api.getTextChannelsByNameIgnoreCase(channel).toArray()[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}