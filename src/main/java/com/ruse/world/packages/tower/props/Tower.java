package com.ruse.world.packages.tower.props;

import com.ruse.model.Item;
import com.ruse.model.Position;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum Tower {

    T_0_L_1(0, 0, new int[]{9910, 9910, 9910}, new int[]{}, new Item(12852, 50),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),

    T_0_L_2(0, 1, new int[]{9910, 9910, 9838}, new int[]{}, new Item(12852, 75),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),

    T_0_L_3(0, 2, new int[]{9910, 9838, 9838}, new int[]{}, new Item(12852, 100),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),

    T_0_L_4(0, 3, new int[]{9838, 9838, 9838}, new int[]{}, new Item(12852, 125),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),

    T_0_L_5(0, 4, new int[]{9838, 9838, 9807}, new int[]{}, new Item(15004, 2),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),
    T_0_L_6(0, 5, new int[]{9838, 9807, 9807}, new int[]{}, new Item(12852, 150),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),
    T_0_L_7(0, 6, new int[]{9807, 9807, 9807}, new int[]{}, new Item(12852, 175),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),
    T_0_L_8(0, 7, new int[]{9807, 9845, 9845}, new int[]{}, new Item(12852, 200),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),
    T_0_L_9(0, 8, new int[]{9807, 9845, 9845}, new int[]{}, new Item(12852, 250),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791)),
    T_0_L_10(0, 9, new int[]{9815, 9845, 9807, 9838, 9910}, new int[]{}, new Item(15004, 5),
            new Position(2872, 2759), new Position(2825, 2806),
            new Position(2843, 2801), new Position(2828, 2791),
            new Position(2849, 2788), new Position(2827, 2796)),

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
