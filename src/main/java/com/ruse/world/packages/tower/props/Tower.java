package com.ruse.world.packages.tower.props;

import com.ruse.model.Item;
import com.ruse.model.Position;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum Tower {

    T_0_L_1(0, 0, new int[]{9027, 9837, 9837}, new int[]{}, new Item(985, 10000),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),

    T_0_L_2(0, 1, new int[]{9835, 9027, 9027}, new int[]{}, new Item(985, 20000),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),

    T_0_L_3(0, 2, new int[]{9911, 9835, 9835}, new int[]{}, new Item(985, 30000),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),

    T_0_L_4(0, 3, new int[]{9922, 9911, 9911}, new int[]{}, new Item(985, 40000),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),

    T_0_L_5(0, 4, new int[]{9838, 9838, 9838}, new int[]{}, new Item(985, 75000),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),

    ;
    private final int tier, level;
    private final Item reward;
    private final int[] buffs, npcIds;
    private final Position position;
    private final Position[] npcPositions;
    Tower(int tier, int level, int[] npcIds, int[] buffs, Item reward,
          Position position, Position... npcPositions){
        this.tier = tier;
        this.level = level;
        this.npcIds = npcIds;
        this.buffs = buffs;
        this.reward = reward;
        this.position = position;
        this.npcPositions = npcPositions;
    }

    public static @Nullable Tower get(int tier, int level){
        for(Tower tower : values()){
            if(tower.getTier() == tier && tower.getLevel() == level){
                return tower;
            }
        }
        return null;
    }
}
