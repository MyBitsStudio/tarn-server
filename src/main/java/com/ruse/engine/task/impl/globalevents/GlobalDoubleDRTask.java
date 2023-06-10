package com.ruse.engine.task.impl.globalevents;

import com.ruse.GameSettings;
import com.ruse.engine.task.Task;
import com.ruse.world.World;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.content.discordbot.JavaCord;

public class GlobalDoubleDRTask extends Task {

    public GlobalDoubleDRTask() {
        super(1, 0, true);
    }

    int timer = 0;

    @Override
    protected void execute() {
                    if (timer < 1) {
                        timer = 6000;
                        World.sendMessage("2X Drop Rate Event has begun for 60 minutes!");
                        JavaCord.sendMessage(1117224370587304057L, "2X Drop Rate Event has begun for 60 minutes!");
                        AdminCord.sendMessage(1116222317937311835L, "2X Drop Rate Event has begun for 60 minutes!");
                        GameSettings.DOUBLE_DROP = true;
                    } else if(timer == 1) {
                        World.sendMessage("2X Drop Rate Event has ended!");
                        JavaCord.sendMessage(1117224370587304057L, "2X Drop Rate Event has ended!");
                        AdminCord.sendMessage(1116222317937311835L, "2X Drop Rate Event has ended!");
                        GameSettings.DOUBLE_DROP = false;
                        stop();
                    } else {
                        timer--;
                    }
    }
}
