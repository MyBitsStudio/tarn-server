package com.ruse.world.packages.discordbot;

import com.ruse.GameSettings;
import com.ruse.world.World;
import com.ruse.world.packages.discordbot.events.MessageReceived;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

/*
 * @project Vanity-Server
 * @author Patrity - https://github.com/Patrity
 * Created on - 4/13/2020
 */
public class Bot {

    private static String TOKEN = "OTIwOTEzMjUyMjk5NDYwNjA5.Gql2R2.KKP2ae3t9YeD_I7pVW2Mnf_UEhK7c_j-XWPyF8";
    public static String PREFIX = ";;";
    public static String OWNER_ROLE = "966547980796715075";
    public static String DEVELOPER_ROLE = "966547980796715072";
    public static String ADMIN_ROLE = "966547980796715070";
    public static String GLOBAL_MOD_ROLE = "966547980796715068";

    private static int TIMER = 60;

    public static JDA discord;

    public static void init() {
        try {
            discord = JDABuilder.createDefault(TOKEN)
            .setToken(TOKEN)
            .setActivity(Activity.watching("Tarn"))
            .build()
            .awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            discord.addEventListener(new MessageReceived());
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int countDown = 0;

    public static void updatePlayers() {
        if (GameSettings.LOCALHOST)
            return;
        if (countDown == TIMER) {
            int players = World.getPlayers().size() + GameSettings.players;
            discord.getPresence().setActivity(Activity.watching((players) + " Players"));
            countDown = 0;
        } else {
            countDown++;
        }
    }
}
