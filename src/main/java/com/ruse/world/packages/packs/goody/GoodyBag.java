package com.ruse.world.packages.packs.goody;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public abstract class GoodyBag {

    private final Player player;

    @Setter
    private boolean claimed = false;
    public GoodyBag(Player player) {
        this.player = player;
    }

    public abstract Item[] rewards();

    public abstract int boxId();

    public void open() {
        player.getPacketSender().sendInterface(49200);
        player.getPacketSender().resetItemsOnInterface(49270, 20);
        shuffle(rewards());
        claimed = false;
        player.selectedGoodieBag = -1;

        for (int i = 1; i <= 20; i++) {
            player.getPacketSender().sendString(49232 + i, String.valueOf(i));
        }
    }

    private void shuffle(Item @NotNull [] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Item a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    private void showRewards() {

        for (int i = 1; i <= 20; i++) {
            player.getPacketSender().sendString(49232 + i, "");
        }

        for (int i = 0; i < rewards().length; i++) {
            player.getPacketSender().sendItemOnInterface(49270, rewards()[i].getId(), i, rewards()[i].getAmount());
        }
    }

    public void claim() {
        if (player.selectedGoodieBag == -1) {
            player.sendMessage("@red@You haven't picked a number yet");
            return;
        }

        if (boxId() == -1) {
            player.sendMessage("You already opened this box"); // basically for it to refresh rewards(this is the best way) u gotta reopen each box
            // because otherwise, for it to reset back, u'd need another button etc.
            return;
        }
        if (claimed) {
            player.sendMessage("@red@You've already claimed the reward for this box");
        } else if (player.getInventory().contains(boxId())) { // gg this is guaranteed to work
            showRewards();
            player.getInventory().delete(boxId(), 1);
            player.getInventory().add(rewards()[player.selectedGoodieBag]);
            claimed = true;
        } else {
            player.sendMessage("@red@You need a goodiebag box to claim the reward");
        }
    }

    public boolean handleClick(int buttonId) {
        if (!(buttonId >= -16325 && buttonId <= -16306)) {
            return false;
        }

        if(claimed) {
            return false;
        }

        int index = -1;

        if (buttonId >= -16325) {
            index = 16325 + buttonId;
        }
        player.getPacketSender().sendString(49232 + player.selectedGoodieBag + 1,
                String.valueOf(player.selectedGoodieBag + 1));
        player.selectedGoodieBag = index;
        player.getPacketSender().sendString(49232 + index + 1, "Pick");

        return true;
    }
}
