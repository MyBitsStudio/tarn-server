package com.ruse.world.content.clans;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.PlayerRights;
import com.ruse.security.ServerSecurity;
import com.ruse.security.save.impl.server.ClanChatLoad;
import com.ruse.security.save.impl.server.ClanSave;
import com.ruse.util.NameUtils;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.entity.impl.player.Player;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ClanManager {

    private static ClanManager manager;

    public static ClanManager getManager(){
        if(manager == null){
            manager = new ClanManager();
        }
        return manager;
    }

    private final List<Clan> clans = new CopyOnWriteArrayList<>();

    public List<Clan> getClans(){
        return clans;
    }
    public Clan getClan(String name){
        for(Clan clan : clans){
            if(clan.getName().equalsIgnoreCase(name))
                return clan;
        }
        return null;
    }

    public void init(){
        System.out.println("Loading clans...");
        File folder = new File("./.core/server/clans/");
        if(!folder.exists())
            return;
        for(File file : Objects.requireNonNull(folder.listFiles())){
            if(file.getName().endsWith(".json")){
               new ClanChatLoad(this).loadJSON(file.getPath()).run();
            }
        }
    }

    public void addChat(Clan clan){
        if(getClan(clan.getName()) != null){
            System.out.println("Clan already exists: "+clan.getName());
            return;
        }
        clans.add(clan);
    }

    public void save(){
        for(Clan clan : clans){
            new ClanSave(clan).create().save();
        }
    }

    public void joinChat(Player player, String name){
        Clan clan = getClan(name);
        if(clan == null){
            player.getPacketSender().sendMessage("The clan you tried to join does not exist.");
            return;
        }
        if (clan.getMembers().size() >= 500) {
            player.getPacketSender().sendMessage("This clan channel is currently full.");
            return;
        }
        if(clan.isBanned(player.getUsername())){
            player.getPacketSender().sendMessage("You are banned from this clan chat.");
            return;
        }
        if(clan.getName().equalsIgnoreCase("staff")){
            if(!player.getRights().isStaff()){
                player.getPacketSender().sendMessage("You do not have the required rank to join this clan chat.");
                return;
            }
        }
        if(clan.addMember(player)){
            player.setClan(clan);
            updateList(clan);
            player.getPacketSender().sendMessage("You have joined the clan chat: "+clan.getName());
        } else {
            player.getPacketSender().sendMessage("You could not join the clan chat.");
        }
    }

    public void updateList(Clan clan){
        clan.getMembers().sort((o1, o2) -> {
            if(o1.getRights().isStaff() && !o2.getRights().isStaff())
                return -1;
            if(!o1.getRights().isStaff() && o2.getRights().isStaff())
                return 1;
            return 0;
        });
        clan.getMembers().stream()
                .filter(Objects::nonNull)
                .forEach(player -> {
                    for (int i = 29344; i < 29444; i++) {
                        player.getPacketSender().sendString(i, "");
                    }
                    AtomicInteger childId = new AtomicInteger(29344);
                    clan.getMembers().stream()
                            .filter(Objects::nonNull)
                            .filter(other -> other.getSession().getChannel().isConnected())
                            .forEach(others -> {
                                int image = 0;
                                switch(others.getRights()){
                                    case DEVELOPER : image = 861; break;
                                    case ADMINISTRATOR: image = 860; break;
                                    case MODERATOR : image = 863; break;
                                    case HELPER : image = 866; break;
                                    case YOUTUBER: image = 865; break;
                                }
                                String prefix = image > 0 ? ("<img=" + image + "> ") : "";
                                player.getPacketSender().sendString(childId.get(), prefix + others.getUsername());
                                image = 0;
                                childId.getAndIncrement();
                            });

                    if(player.getRights().OwnerDeveloperOnly()){
                        player.getPacketSender().sendClanChatListOptionsVisible(2);
                    } else if(player.getRights().isStaff()){
                        player.getPacketSender().sendClanChatListOptionsVisible(1);
                    } else {
                        player.getPacketSender().sendClanChatListOptionsVisible(0);
                    }
                });

    }

    public void sendMessage(Player player, String message){
        Clan clan = player.getClan();
        if (clan == null) {
            player.getPacketSender().sendMessage("You're not in a clanchat channel.");
            return;
        }
        if(ServerSecurity.getInstance().isPlayerMuted(player.getUsername())){
            player.getPacketSender().sendMessage("You are muted and cannot chat.");
            return;
        }
        long time = player.getTotalPlayTime() + player.getRecordedLogin().elapsed();
        if((time < 1_800_000) && !Objects.equals(clan.getName(), "Help") && !player.getRights().isStaff()){
            player.getPacketSender().sendMessage("New players can only chat in Help chat.");
            return;
        }
        if (!player.isCanChat()) {
            player.getPacketSender().sendMessage("@red@You can only send messages every few seconds as a new player!");
            return;
        }
        clan.getMembers().stream()
                .filter(Objects::nonNull)
                .filter(players -> !players.getRelations().getIgnoreList().contains(player.getLongUsername()))
                .forEach(players -> {
            int img = players.getRights() == PlayerRights.FORSAKEN_DONATOR ? 1508 : players.getRights().ordinal();
            String formatted = String.format("%02d", clan.getName().length() + 1);

            String rankImg = img > 0 ? " <img=" + img + ">" : "";
            players.getPacketSender().sendMessage(":clan:" + formatted +    "["
                    + clan.getName() +  "]" + rankImg + " " + NameUtils.capitalizeWords(player.getUsername()) + ": "
                    + NameUtils.capitalize(message));
            });
        if(time < 1_800_000 && !player.getRights().isStaff()){
            player.setCanChat(false);
            TaskManager.submit(new Task(19, false) {
                @Override
                public void execute() {
                    player.setCanChat(true);
                    this.stop();
                }
            });
        }

        PlayerLogs.log(player.getUsername(),
                "(CC) " + (player.getClan() != null
                        ? player.getClan().getName()
                        : "NULL") + ". Said: " + StringUtils.capitalize(message.toLowerCase()));

        PlayerLogs.logClanMessages(clan.getName(), "Sender: " + NameUtils.capitalize(player.getUsername())
                + ", Message: " + NameUtils.capitalize(message));
    }

    public void leave(Player player, boolean kicked){
        Clan clan = player.getClan();
        if (clan == null) {
            clans.forEach(c -> c.removeMember(player));
            player.getPacketSender().sendMessage("You're not in a clanchat channel.");
            return;
        }

        player.setClan(null);
        clan.removeMember(player);
        ClanManager.getManager().reset(player);
        player.getPacketSender().sendClanChatListOptionsVisible(0);
        updateList(clan);
        player.getPacketSender()
                .sendMessage(kicked ? "You have been kicked from the channel." : "You have left the channel.");
    }

    public void kick(Player player, Player other){

    }

    public void reset(Player player){
        for (int i = 29344; i < 29444; i++) {
            player.getPacketSender().sendString(i, "");
        }
    }

    public void handleMemberOption(Player player, int index, int menuId) {
		if ((player.getClan() == null
				|| !player.getRights().OwnerDeveloperOnly() && menuId != 1)) {
			player.getPacketSender().sendMessage("Only the clanchat owner can do that.");
			return;
		}
		Player target = getPlayer(index, player.getClan());
		if (target == null || target.equals(player)) {
			return;
		}
		switch (menuId) {
			case 8:
			case 7:
			case 6:
			case 5:
			case 4:
			case 3:
            case 2:
                break;
            case 1:
				kick(player, target);
				break;
		}
	}

    public Player getPlayer(int index, Clan clan) {
		int clanIndex = 0;
		for (Player members : clan.getMembers()) {
			if (members != null) {
				if (clanIndex == index) {
					return members;
				}
				clanIndex++;
			}
		}
		return null;
	}

    public boolean handleButtons(Player player, int id){
        switch(id){
            case 70103:
                ClanManager.getManager().leave(player, false);
                ClanManager.getManager().joinChat(player, "Help");
                break;
            case 70104:
                ClanManager.getManager().leave(player, false);
                ClanManager.getManager().joinChat(player, "Trade");
                break;
            case 70105:
                ClanManager.getManager().leave(player, false);
                ClanManager.getManager().joinChat(player, "Groups");
                break;
            case 70106:
                ClanManager.getManager().leave(player, false);
                ClanManager.getManager().joinChat(player, "Raids");
                break;
            case 70107:
                if(player.getRights().isStaff()){
                    ClanManager.getManager().leave(player, false);
                    ClanManager.getManager().joinChat(player, "Staff");
                } else {
                    player.getPacketSender().sendMessage("You must be a staff member to join this channel.");
                }
                break;
        }
        return false;
    }
}
