package com.ruse.world.packages.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public abstract class Bot {

    public JDA discord;
    public abstract String token();

    public abstract String prefix();

    public void init(){
        try{
            discord = JDABuilder.createDefault(token())
                    .setToken(token())
                    .setActivity(Activity.watching("Tarn"))
                    .build()
                    .awaitReady(); // Blocking guarantees that JDA will be completely loaded.
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
