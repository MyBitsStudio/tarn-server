package com.ruse.world.packages.combat.drops;

public record Drop(int id, int chance, int min, int max, double modifier, boolean announce) {
}
