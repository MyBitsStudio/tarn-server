package com.ruse.net.packet.impl;

import com.ruse.model.ChatMessage.Message;
import com.ruse.model.Flag;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.util.StringCleaner;
import com.ruse.webhooks.discord.DiscordMessager;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.PlayerPunishment;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.entity.impl.player.Player;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
//import com.sun.xml.internal.ws.util.StringUtils;

/**
 * This packet listener manages the spoken text by a player.
 *
 * @author relex lawl
 */

public class ChatPacketListener implements PacketListener {

    private static final char[] CHAR_TABLE = {' ', 'e', 't', 'a', 'o', 'i', 'h', 'n', 's', 'r', 'd', 'l', 'u', 'm',
            'w', 'c', 'y', 'f', 'g', 'p', 'b', 'v', 'k', 'x', 'j', 'q', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', ' ', '!', '?', '.', ',', ':', ';', '(', ')', '-', '&', '*', '\\', '\'', '@', '#', '+', '=',
            '\243', '$', '%', '"', '[', ']', '_', '/', '<', '>'};

    public static String decode(byte[] bytes, int size) {
        char[] chars = new char[size];

        /*
         * if (format) { for (int i = 0; i < size; i++) { int key = bytes[i] & 0xFF;
         * char ch = CHAR_TABLE[key];
         *
         * if (capitalize && (ch >= 'a') && (ch <= 'z')) { ch += '\uFFE0'; capitalize =
         * false; }
         *
         * if ((ch == '.') || (ch == '!') || (ch == '?')) { capitalize = true; }
         *
         * chars[i] = ch; } } else {
         */
        for (int i = 0; i < size; i++) {
            int key = bytes[i] & 0xFF;
            chars[i] = CHAR_TABLE[key];
        }
        // }

        return new String(chars);
    }

    public static byte[] encode(String str) {
        char[] chars = str.toLowerCase().toCharArray();
        byte[] buf = new byte[chars.length];

        for (int i = 0; i < chars.length; i++) {
            for (int n = 0; n < CHAR_TABLE.length; n++) {
                if (chars[i] == CHAR_TABLE[n]) {
                    buf[i] = (byte) n;
                    break;
                }
            }
        }

        return buf;
    }


    @Override
    public void handleMessage(@NotNull Player player, @NotNull Packet packet) {
        int effects = packet.readUnsignedByteS();
        int color = packet.readUnsignedByteS();
        int size = packet.getSize();
        byte[] text = packet.readReversedBytesA(size);
        if (PlayerPunishment.muted(player.getUsername()) || PlayerPunishment.IPMuted(player.getHostAddress())) {
            player.getPacketSender().sendMessage("You are muted and cannot chat.");
            return;
        }

        String readable = StringUtils.capitalize(decode(text, size).toLowerCase());

        if(StringCleaner.securityBreach(readable)){
            player.getPSecurity().getPlayerLock().increase("secLock", readable);
            System.out.println("Security breach: "+readable);
            player.getPacketSender().sendMessage("@red@[SECURITY] This is your only warning. Do not attempt to breach the security of the server again.");
            return;
        }

        readable = StringCleaner.cleanInput(readable);

        if(StringCleaner.censored(readable)){
            player.getPSecurity().getPlayerLock().increase("wordAtt", readable);
            System.out.println("Censored word: "+readable);
            player.getPacketSender().sendMessage("@red@[SECURITY] This is your only warning. Do not attempt to breach the security of the server again.");
            return;
        }

       // System.out.println(player.getLocation().toString()+"|"+player.getPosition().getX()+","+player.getPosition().getY()+","+player.getPosition().getZ()+"|Said:"+readable);

        String str = Misc.textUnpack(text, size).toLowerCase().replaceAll(";", ".");
        //System.out.println("str: "+str);

        player.getChatMessages().set(new Message(color, effects, text));

        PlayerLogs.log(player.getUsername(), player.getLocation().toString() + "|" + player.getPosition().getX() + ","
                + player.getPosition().getY() + "," + player.getPosition().getZ() + "|Said: " + readable);
        player.getUpdateFlag().flag(Flag.CHAT);
        if(!readable.contains("@")) {
            DiscordMessager.sendChatMessage("**" + player.getUsername() + "**|" + player.getLocation().toString() + "|"
                    + player.getPosition().getX() + "," + player.getPosition().getY() + "," + player.getPosition().getZ()
                    + "|Said: " + readable);
        }

        PlayerLogs.logChat(player.getUsername(), player.getLocation().toString() + "|" + player.getPosition().getX() + ","
                + player.getPosition().getY() + "," + player.getPosition().getZ() + "|Said: " + readable);

    }

}
