package com.ruse.world.content;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

@Getter
public enum YoutuberLinks {

    CASA("https://www.youtube.com/channel/UClMvGtkUxyxU--uIM8SSRkQ", "casa", "casalusio"),
    FEWB("https://www.youtube.com/@Fewb/videos", "fewb"),
    PEACH("https://www.youtube.com/@princespeachrsps362", "peach"),
    REDX("https://www.youtube.com/@RedX12884/streams", "redx"),
    WALKCHAOS("https://www.youtube.com/channel/UCfSUF-7-QehS6ALll4nl3OA", "walkchaos", "walk"),
    NOOBSOWN("https://www.youtube.com/channel/UCfnBh-xkfCnjg4Gw6GyiHlA", "noobsown"),
    CERAXY("https://www.youtube.com/channel/UCe9NqnfPfqwWLv09Z3iXPgA", "ceraxy"),
    FPK("https://www.youtube.com/channel/UCeVDQGzCI3dxp8NAT_YBIkQ", "fpk", "fpk merk"),
    WRECKED("https://www.youtube.com/channel/UCNm7R0y8KN8kSn3yVn04pkg", "wrecked", "wr3cked you", "wr3cked"),
    VIHTIC("https://www.youtube.com/channel/UCyvtKISjnfLtbwe_69snxWA", "vihtic"),
    STABLE("https://www.youtube.com/channel/UCIFSpy1z7aMWZnQJlDF1Wcg", "stable"),
    EFFIGY("https://www.youtube.com/channel/UCR-GGPuNM7V51JYWVbcDURQ", "effigy", "effigyswiper"),
    SPRAD("https://www.youtube.com/channel/UCL5_jd65woKzjKfXhbBIuJQ", "sprad"),
    DIDY("https://www.youtube.com/channel/UC6ag3g4fFug0ZmXOeDpHnTw", "didy", "didyscape"),
    WET("https://www.youtube.com/user/Pemscapeofficial", "wet", "wet wizard"),
    EGGY("https://www.youtube.com/channel/UC3S8CHERuzF7N5T3ynrBroA", "eggy"),
    BAG("https://www.youtube.com/channel/UCrqwnpbmGYQACq5Nytb99eA", "bag", "baguette"),
    MORGEN("https://www.youtube.com/channel/UC2bith9opq5dC19OQoNW5kg",  "morgen"),
    NUB("https://www.youtube.com/channel/UCoCca4ERC7jDWWIbE6Eg8Tg",  "nub"),
    ENTS("https://www.youtube.com/@EntsRSPS",  "ents"),
    NOBLE("https://www.youtube.com/@NobleDefeat",  "noble"),
    KABAL("https://www.youtube.com/@kabal9900",  "kabal"),
    SIPSICK("https://www.youtube.com/@sipsick5494/videos",  "sipsick"),
    PERP("https://www.youtube.com/channel/UCtb_rEautI4iFM0ZVIB0lkw",  "perp"),
    WINALLDAY("https://www.youtube.com/@WinAllDay1",  "winallday"),
    IPKMAXJR("https://www.youtube.com/@IPkMaxJr",  "max"),
    JIPY("https://www.youtube.com/@jipyrsps",  "jipy"),
    STYLO("https://www.youtube.com/@1Stylo",  "stylo"),
    LANO("https://www.youtube.com/channel/UCIz0u4h_J2e_c5MNJBN0uiw/videos?view_as=subscriber",  "lano", "lanos"),


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
                    if (link == VIHTIC)
                        player.getPacketSender().sendString(1, "https://www.youtube.com/channel/UCyvtKISjnfLtbwe_69snxWA");
                     else
                        player.getPacketSender().sendString(1, link.getURL());

                    player.getPacketSender().sendMessage("Attempting to open the link.");
                    return true;
                }
            }
        }
        return false;
    }


}
