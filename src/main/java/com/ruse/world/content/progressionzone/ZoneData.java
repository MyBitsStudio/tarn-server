package com.ruse.world.content.progressionzone;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import lombok.Getter;

public class ZoneData {

    public enum Monsters {

        GOBLIN(9837, 10, new Item[]{new Item(19984), new Item(19985), new Item(19986)}),
        ORC(9027, 20, new Item[]{new Item(19989), new Item(19988), new Item(20400)}),
        DEMON(9835, 30, new Item[]{new Item(14488, 3), new Item(19992), new Item(19991)}),
        WEREWOLF(9911, 40, new Item[]{new Item(14488, 3), new Item(15359), new Item(15358)}),
        CENTAUR(9922, 50, new Item[]{new Item(2736, 3), new Item(2023, 80), new Item(995, 1_000_000)}),
        //HOUND(9838, 60, new Item[]{new Item(20086),  new Item(20087),  new Item(20088)}),
        //SCORPION(9845, 70, new Item[]{new Item(20089),  new Item(20091),  new Item(20092)}),
        //RANGER(9910, 80, new Item[]{new Item(14487, 3),  new Item(23118),  new Item(20093)}),
        //PALADIN(9807, 100, new Item[]{new Item(14487, 3),  new Item(23124),  new Item(19119)}),
        //BOWSER(9006, 150, new Item[]{new Item(ItemDefinition.COIN_ID, 10000000)})
        ;

        @Getter
        private int npcId;
        @Getter
        private int amountToKill;
        @Getter
        private Item[] rewards;

        Monsters(int npcId, int amountToKill, Item[] rewards) {
            this.npcId = npcId;
            this.amountToKill = amountToKill;
            this.rewards = rewards;
        }

        public static Monsters forID(int npcId) {
            for (Monsters monster : Monsters.values()) {
                if (monster.getNpcId() == npcId) {
                    return monster;
                }
            }
            return null;
        }

        public String getName() {
            return Misc.ucFirst(name().toLowerCase());
        }

        public Position getCoords() {
            return new Position(3037, 10280, ordinal() * 4);
        }
    }

}
