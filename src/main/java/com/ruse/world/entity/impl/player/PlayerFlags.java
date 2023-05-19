package com.ruse.world.entity.impl.player;

import com.ruse.GameSettings;
import com.ruse.model.input.impl.Enter2FAPacketListener;
import com.ruse.model.input.impl.EnterPinPacketListener;
import com.ruse.world.World;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerFlags {

    /***
     * Player Flags - Locks and other updating flags
     *
     * -- PinLock -- Lock for Pin to flag to enter pin -- *pinLock*
     * -- 2fa -- Lock for 2FA -- *fa*
     * -- Change Password -- Prompts player to change password -- *changePass*
     * -- Change Pin -- Prompts player to change pin -- *changePin*
     * -- Enter Pin -- Need to enter pin -- *pinEnter*
     */
    private final Map<String, Boolean> flags = new ConcurrentHashMap<>();

    private final Player player;

    private boolean reqs;

    public PlayerFlags(Player player){
        this.player = player;
        set();
    }

    public void set(){
        flags.put("pinLock", false);
        flags.put("fa", false);
        flags.put("changePass", false);
        flags.put("changePin", false);
        flags.put("pinEnter", false);
        flags.put("forceKick", false);
    }

    public boolean isFlagged(String key){
        return flags.getOrDefault(key, false);
    }

    public void setFlag(String key, boolean value){
        flags.replace(key, value);
    }

    public void process(){
        if(isFlagged(PIN_ENTER)){
            player.setPlayerLocked(true);
            setFlag(PIN_LOCK, true);
            setFlag(PIN_ENTER, false);
        }
        if(isFlagged(PIN_LOCK)){
            if(!reqs) {
                reqs = true;
                player.setInputHandling(new EnterPinPacketListener());
                player.getPacketSender().sendEnterInputPrompt("Enter your pin to play#confirmstatus");
            }
        }
        if(isFlagged(TWO_FACTOR_AUTH)){
            if(player.getPSecurity().faEnabled()){
                if(!reqs) {
                    reqs = true;
                    player.setInputHandling(new Enter2FAPacketListener());
                    player.getPacketSender().sendEnterInputPrompt("Enter your 2FA to unlock#confirm2fa");
                }
            } else {
                setFlag(TWO_FACTOR_AUTH, false);
                player.setPlayerLocked(false);
                reqs = false;
            }
        }
        if(isFlagged(CHANGE_PASSWORD)){
            player.getPacketSender().sendMessage("<@red@[SECURITY] It's been over 30 days. Change your password ASAP! ::changepass");
            setFlag(CHANGE_PASSWORD, false);
        }
        if(isFlagged(CHANGE_PIN)){
            player.getPacketSender().sendMessage("<@red@[SECURITY] It's been over 30 days. Change your PIN ASAP! ::changepin");
            setFlag(CHANGE_PIN, false);
        }
        if(isFlagged(FORCE_KICK)){
            World.deregister(player);
            setFlag(FORCE_KICK, false);
        }
    }

    public void successPin(){
        setFlag(PIN_LOCK, false);
        setFlag(PIN_ENTER, false);
        setFlag(TWO_FACTOR_AUTH, true);
        reqs = false;
    }

    public void success2FA(){
        setFlag(TWO_FACTOR_AUTH, false);
        player.setPlayerLocked(false);
        reqs = false;
    }


    /**
     * Flags below
     */

    public static String PIN_LOCK = "pinLock";
    public static String TWO_FACTOR_AUTH = "fa";
    public static String CHANGE_PASSWORD = "changePass";
    public static String CHANGE_PIN = "changePin";

    public static String PIN_ENTER = "pinEnter";
    public static String FORCE_KICK = "forceKick";
}
