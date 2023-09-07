package com.ruse.world.content;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

@Getter
public enum YoutuberLinks {

    WALKCHAOS("https://www.youtube.com/channel/UCfSUF-7-QehS6ALll4nl3OA", "walkchaos", "walk"),
    NOOBSOWN("https://www.youtube.com/channel/UCfnBh-xkfCnjg4Gw6GyiHlA", "noobsown"),
    WINALLDAY("https://www.youtube.com/@WinAllDay1",  "winallday"),
    IPKMAXJR("https://www.youtube.com/@IPkMaxJr",  "max"),
    JIPY("https://www.youtube.com/@jipyrsps",  "jipy"),


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
