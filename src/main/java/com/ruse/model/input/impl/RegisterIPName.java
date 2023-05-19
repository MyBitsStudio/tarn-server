package com.ruse.model.input.impl;

import com.ruse.model.input.Input;
import com.ruse.world.entity.impl.player.Player;

public class RegisterIPName extends Input {

    @Override
    public void handleSyntax(Player player, String input) {
        if(input.equalsIgnoreCase("yes")) {
            player.getPSecurity().setIP();
            player.sendMessage("IP added to your list");
        } else if(input.equalsIgnoreCase("no")) {
            player.sendMessage("IP not added to your list");
        } else {
            player.sendMessage("Invalid input");
        }
    }
}
