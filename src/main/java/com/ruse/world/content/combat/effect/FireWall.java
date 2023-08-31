package com.ruse.world.content.combat.effect;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Graphic;
import com.ruse.model.Hit;
import com.ruse.model.Position;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.max.MagicMax;

import java.util.Arrays;

public class FireWall {

    private final int width;

    private final Graphic graphic;
    private final int stages;
    private final int ticksBetweenStages;
    private final Position[][] positions;
    private final int damage;
    private final int openSpotCount;

    public FireWall(int width, Dir dir, int startX, int startY, Graphic graphic, int stages, int ticksBetweenStages, int damage, int openSpotCount) {
        this.width = width;
        this.graphic = graphic;
        this.stages = stages;
        this.ticksBetweenStages = ticksBetweenStages;
        this.damage = damage;
        this.openSpotCount = openSpotCount;

        positions = new Position[stages][width];
        int x = startX;
        int y = startY;

        for(int i = 0; i < positions.length; i++) {

            if(dir == Dir.NORTH) {
                y++;
                x = startX;
            } else if(dir == Dir.EAST) {
                x++;
                y = startY;
            } else if(dir == Dir.SOUTH) {
                y--;
                x = startX;
            } else {
                y = startY;
                x--;
            }
            for(int k = 0; k < positions[i].length; k++) {
                if(dir == Dir.NORTH || dir == Dir.SOUTH) {
                    x++;
                } else if(dir == Dir.EAST || dir == Dir.WEST) {
                    y++;
                }
                Position position = new Position(x, y);
                positions[i][k] = position;
            }

        }
    }

    public void start(boolean force, Player player){
        TaskManager.submit(new Task(ticksBetweenStages) {
            int stage = 0;
            @Override
            protected void execute() {
                if(stage == stages-1) stop();
                for (NPC next : player.getLocalNpcs()) {
                    if(next != null && next.getConstitution() > 0) {
                        if (next.getPosition().isWithinDistance(player.getPosition(), width)) {
                           next.performGraphic(graphic);
                           next.getCombatBuilder().setLastAttacker(player);
                           next.dealDamage(new Hit(MagicMax.newMagic(player, next) / 12));
                           next.setAggressive(true);
                           next.getCombatBuilder().attack(player);
                        }
                    }
                }
                stage++;
            }
        });
    }

    public void start(Player player) {
        TaskManager.submit(new Task(ticksBetweenStages) {
            int stage = 0;
            @Override
            protected void execute() {
                if(player == null) stop();
                if(stage == stages-1) stop();
                int[] spots = new int[openSpotCount];
                for(int i = 0; i < openSpotCount; i++) {
                    spots[i] = Misc.random(width);
                }
                for(int i = 0; i < positions[stage].length; i++) {
                    int finalI = i;
                    if(Arrays.stream(spots).anyMatch(spot -> spot == finalI)) continue;
                    Position position = positions[stage][i];
                    if(player != null) {
                        player.getPacketSender().sendGraphic(graphic, position);
                        if (player.getPosition().equals(position)) {
                            player.getPacketSender().sendMessage("@red@You got hit by the flames");
                            player.dealDamage(new Hit(damage));
                        }
                    }
                }
                stage++;
            }
        });
    }

    public enum Dir {
        NORTH,
        EAST,
        SOUTH,
        WEST
    }
}
