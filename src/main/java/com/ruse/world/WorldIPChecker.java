package com.ruse.world;

import com.ruse.model.WorldIPLog;
import com.ruse.security.save.impl.server.WorldIPLoad;
import com.ruse.security.save.impl.server.WorldIPSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.GameModeConstants;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldIPChecker {

    private static WorldIPChecker instance;

    public static WorldIPChecker getInstance() {
        if (instance == null) {
            instance = new WorldIPChecker();
        }
        return instance;
    }

    private final List<WorldIPLog> worldIPLogs = new CopyOnWriteArrayList<>();
    private final Map<String, Map<String, Boolean>> contents = new ConcurrentHashMap<>();

    public List<WorldIPLog> getWorldIPLogs() {
        return worldIPLogs;
    }

    public boolean strictCheck(Player player, String content) {
        boolean added = worldIPLogs.stream().anyMatch(worldIPLog -> worldIPLog.getIp().equals(player.getHostAddress()) && worldIPLog.getContent().equals(content));
        if (added) {
            List<WorldIPLog> worldIPLog = worldIPLogs.stream().filter(worldIPLog1 -> worldIPLog1.getIp().equals(player.getHostAddress()) && worldIPLog1.getContent().equals(content)).toList();
            if (!worldIPLog.isEmpty()) {
                boolean run = true, bypass = false;
                for (WorldIPLog log : worldIPLogs) {
                    if (log.getUsername().equals(player.getUsername())) {
                        bypass = true;
                        continue;
                    }
                    if(log.getIp().equals(player.getHostAddress()) && log.getContent().equals(content)
                            && !log.getUsername().equals(player.getUsername())) {
                        run = false;
                        break;
                    }
                }
                if(run) {
                    if(!bypass) {
                        worldIPLogs.add(new WorldIPLog(player.getUsername(), content, player.getHostAddress(), WorldCalendar.getInstance().getTime(), GameModeConstants.isIronman(player) ? "ironman" : player.getMode().toString()));
                        save();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            worldIPLogs.add(new WorldIPLog(player.getUsername(), content, player.getHostAddress(), WorldCalendar.getInstance().getTime(), player.getMode().toString()));
            save();
            return true;
        }
        return false;
    }

    public boolean check(Player player, String content){
        boolean added = worldIPLogs.stream().anyMatch(worldIPLog -> worldIPLog.getIp().equals(player.getHostAddress()) && worldIPLog.getContent().equals(content));
        if(added) {
            List<WorldIPLog> worldIPLog = worldIPLogs.stream().filter(worldIPLog1 -> worldIPLog1.getIp().equals(player.getHostAddress()) && worldIPLog1.getContent().equals(content)).toList();
            if(!worldIPLog.isEmpty()) {
                boolean run = true, bypass = false;
                for(WorldIPLog log : worldIPLogs) {
                    if(log.getUsername().equals(player.getUsername())) {
                        bypass = true;
                        continue;
                    }
                    if(log.getIp().equals(player.getHostAddress()) && log.getContent().equals(content)
                        && !log.getUsername().equals(player.getUsername())) {
                        if(log.gameMode().equals(player.getMode().toString())) {
                            run = false;
                            break;
                        } else if(log.gameMode().equals("ironman")){
                            if(GameModeConstants.isIronman(player)){
                                run = false;
                                break;
                            }
                        }
                    }
                }
                if(run) {
                    if(!bypass) {
                        worldIPLogs.add(new WorldIPLog(player.getUsername(), content, player.getHostAddress(), WorldCalendar.getInstance().getTime(), GameModeConstants.isIronman(player) ? "ironman" : player.getMode().toString()));
                        save();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            worldIPLogs.add(new WorldIPLog(player.getUsername(), content, player.getHostAddress(), WorldCalendar.getInstance().getTime(), player.getMode().toString()));
            save();
            return true;
        }
        return false;
    }

    public boolean addToContent(@NotNull Player player, String content){
        if(!contents.containsKey(player.getHostAddress())){
            contents.put(player.getHostAddress(), new ConcurrentHashMap<>());
        }
        if(!contents.get(player.getHostAddress()).containsKey(content)){
            contents.get(player.getHostAddress()).put(content, true);
            return true;
        }
        if(!contents.get(player.getHostAddress()).get(content)){
            contents.get(player.getHostAddress()).put(content, true);
            return true;
        }
        return false;
    }

    public void leaveContent(@NotNull Player player){
        if(contents.containsKey(player.getHostAddress())){
            contents.get(player.getHostAddress()).forEach((s, aBoolean) -> {
                if(aBoolean){
                    contents.get(player.getHostAddress()).put(s, false);
                }
            });
        }
    }

    public void save(){
        new WorldIPSave().create().save();
    }

    public void load(List<WorldIPLog> logs){
        worldIPLogs.addAll(logs);
    }

    public void load(){
        new WorldIPLoad().loadJSON(SecurityUtils.IP_LOGS).run();
    }
}
