package com.ruse.world.content;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

@Getter
public enum YoutuberLinks {

    NOOBSOWN("https://www.youtube.com/@NoobsOwn", "noobsown"),
    WINALLDAY("https://www.youtube.com/@WinAllDay1",  "winallday"),
    JIPY("https://www.youtube.com/@jipyrsps",  "jipy"),
    DRAGONIC("https://www.youtube.com/@DragonicNL",  "dragonic"),

    WRECKED("https://www.youtube.com/wr3ckedyou", "wrecked"),

    ;

    private String URL;
    private String[] names;

    YoutuberLinks(String URL, String... names) {
        this.URL = URL;
        this.names = names;
    }

    public static boolean handleCommand(Player player, String command){
        for (YoutuberLinks link : YoutuberLinks.values()){
            for (String name : link.getNames()) {
                if (command.equalsIgnoreCase(name)){
                    player.getPacketSender().sendString(1, link.getURL());

                    player.getPacketSender().sendMessage("Attempting to open the link.");
                    return true;
                }
            }
        }
        return false;
    }


}
