package com.ruse.world.content.teleport;

public enum TeleInterfaceCategory {

    MONSTERS, BOSSES, MINIGAMES, MISC;

    public String getTitle() {
        return this.toString().substring(0, 1) + this.toString().substring(1, this.toString().length()).toLowerCase();
    }

}
