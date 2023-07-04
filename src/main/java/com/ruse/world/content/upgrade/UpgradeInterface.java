package com.ruse.world.content.upgrade;

import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class UpgradeInterface {

    private Player player;
    private UpgradeInterfaceData[] category;
    private UpgradeInterfaceData data;

    public UpgradeInterface(Player player) {
        this.player = player;
    }

    public void open() {
        setCategory(UpgradeInterfaceData.forCategory(UpgradeInterfaceCategory.WEAPONS));
        player.getPacketSender().sendString(19407, "Weapons").sendString(19408, "Armour").sendString(19409, "Accessories").sendString(19410, "Other");
        player.getPacketSender().sendInterface(19400);
    }

    public void upgrade() {
        String name = getData().getToUpgrade().getDefinition().getName();
        if(!player.getInventory().contains(getData().getToUpgrade().getId())) {
            player.sendMessage("You do not have a " + name + " in your inventory.");
            return;
        }
        if(!player.getInventory().containsAll(getData().getRequiredItems())) {
            player.sendMessage("You do not have all the required items to upgrade an " + name + ".");
            return;
        }
        Item item = player.getInventory().getById(getData().getToUpgrade().getId()).copy();
      //  System.out.println("Found item "+item.getSlot());
        Item newItem =  getData().getUpgradedItem();
        player.getInventory().delete(item);
        player.getInventory().deleteItemSet(getData().getRequiredItems());
        player.getInventory().add(newItem);
        player.sendMessage("You upgrade your " + name + " into a " + getData().getUpgradedItem().getDefinition().getName());
    }

    public void openCategory(UpgradeInterfaceCategory category) {
        if(category == getCategory()[0].getCategory())
            return;
        setCategory(UpgradeInterfaceData.forCategory(category));
    }

    public boolean handleButton(int buttonId) {
        switch (buttonId) {
            case 19403, 19404, 19405, 19406 -> {
                openCategory(UpgradeInterfaceCategory.values()[buttonId - 19403]);
                return true;
            }
            case 19423 -> {
                if (getData().getToUpgrade().getId() == 23055
                        || getData().getToUpgrade().getId() == 23056
                        || getData().getToUpgrade().getId() == 23050
                        || getData().getToUpgrade().getId() == 23051
                        || getData().getToUpgrade().getId() == 23052
                        || getData().getToUpgrade().getId() == 23054 //zinq
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 25) {
                        player.sendMessage("You need a Crafting level of 25 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 19135
                        || getData().getToUpgrade().getId() == 20592
                        || getData().getToUpgrade().getId() == 20593
                        || getData().getToUpgrade().getId() == 20594
                        || getData().getToUpgrade().getId() == 4367
                        || getData().getToUpgrade().getId() == 8334
                        || getData().getToUpgrade().getId() == 11140
                        || getData().getToUpgrade().getId() == 8335
                        || getData().getToUpgrade().getId() == 19892 //abberant
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 28) {
                        player.sendMessage("You need a Crafting level of 28 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 20542
                        || getData().getToUpgrade().getId() == 13306
                        || getData().getToUpgrade().getId() == 13300
                        || getData().getToUpgrade().getId() == 13301
                        || getData().getToUpgrade().getId() == 13304
                        || getData().getToUpgrade().getId() == 18683
                        || getData().getToUpgrade().getId() == 13305
                        || getData().getToUpgrade().getId() == 13302 //inferno
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 30) {
                        player.sendMessage("You need a Crafting level of 30 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 21055
                        || getData().getToUpgrade().getId() == 21062
                        || getData().getToUpgrade().getId() == 21063
                        || getData().getToUpgrade().getId() == 21064
                        || getData().getToUpgrade().getId() == 21071
                        || getData().getToUpgrade().getId() == 21067
                        || getData().getToUpgrade().getId() == 21066
                        || getData().getToUpgrade().getId() == 21069
                        || getData().getToUpgrade().getId() == 21068 //nagi
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 31) {
                        player.sendMessage("You need a Crafting level of 31 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 21048
                        || getData().getToUpgrade().getId() == 21049
                        || getData().getToUpgrade().getId() == 21036
                        || getData().getToUpgrade().getId() == 21037
                        || getData().getToUpgrade().getId() == 21038
                        || getData().getToUpgrade().getId() == 21039
                        || getData().getToUpgrade().getId() == 21041
                        || getData().getToUpgrade().getId() == 21040 //tormented
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 33) {
                        player.sendMessage("You need a Crafting level of 33 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 17664
                        || getData().getToUpgrade().getId() == 23134
                        || getData().getToUpgrade().getId() == 23135
                        || getData().getToUpgrade().getId() == 23136
                        || getData().getToUpgrade().getId() == 23138
                        || getData().getToUpgrade().getId() == 23137 //sacred
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 35) {
                        player.sendMessage("You need a Crafting level of 35 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 14915
                        || getData().getToUpgrade().getId() == 14910
                        || getData().getToUpgrade().getId() == 14911
                        || getData().getToUpgrade().getId() == 14912
                        || getData().getToUpgrade().getId() == 14914
                        || getData().getToUpgrade().getId() == 14913 //forsaken
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 37) {
                        player.sendMessage("You need a Crafting level of 37 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 14377
                        || getData().getToUpgrade().getId() == 14733
                        || getData().getToUpgrade().getId() == 14732
                        || getData().getToUpgrade().getId() == 14734
                        || getData().getToUpgrade().getId() == 10865
                        || getData().getToUpgrade().getId() == 12864 //sinful
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 40) {
                        player.sendMessage("You need a Crafting level of 40 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 8816
                        || getData().getToUpgrade().getId() == 8817
                        || getData().getToUpgrade().getId() == 8818
                        || getData().getToUpgrade().getId() == 8820
                        || getData().getToUpgrade().getId() == 8819
                        || getData().getToUpgrade().getId() == 23146
                        || getData().getToUpgrade().getId() == 23145
                        || getData().getToUpgrade().getId() == 23144 //antique
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 42) {
                        player.sendMessage("You need a Crafting level of 42 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 15922
                        || getData().getToUpgrade().getId() == 16021
                        || getData().getToUpgrade().getId() == 15933
                        || getData().getToUpgrade().getId() == 12614 //doom
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 44) {
                        player.sendMessage("You need a Crafting level of 44 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 17710
                        || getData().getToUpgrade().getId() == 5420
                        || getData().getToUpgrade().getId() == 5422
                        || getData().getToUpgrade().getId() == 5428
                        || getData().getToUpgrade().getId() == 17684 //maze
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 47) {
                        player.sendMessage("You need a Crafting level of 47 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 9940
                        || getData().getToUpgrade().getId() == 21042
                        || getData().getToUpgrade().getId() == 21043
                        || getData().getToUpgrade().getId() == 21044
                        || getData().getToUpgrade().getId() == 21045
                        || getData().getToUpgrade().getId() == 21047
                        || getData().getToUpgrade().getId() == 21046 //unknown
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 50) {
                        player.sendMessage("You need a Crafting level of 50 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 8803
                        || getData().getToUpgrade().getId() == 8804
                        || getData().getToUpgrade().getId() == 8805 //titan
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 52) {
                        player.sendMessage("You need a Crafting level of 52 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 8809
                        || getData().getToUpgrade().getId() == 8806
                        || getData().getToUpgrade().getId() == 8807
                        || getData().getToUpgrade().getId() == 8808 //lust
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 55) {
                        player.sendMessage("You need a Crafting level of 55 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 21018
                        || getData().getToUpgrade().getId() == 14050
                        || getData().getToUpgrade().getId() == 14051
                        || getData().getToUpgrade().getId() == 14052
                        || getData().getToUpgrade().getId() == 1485
                        || getData().getToUpgrade().getId() == 14053
                        || getData().getToUpgrade().getId() == 14055 //godly
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 57) {
                        player.sendMessage("You need a Crafting level of 57 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 8088
                        || getData().getToUpgrade().getId() == 11001
                        || getData().getToUpgrade().getId() == 11002
                        || getData().getToUpgrade().getId() == 11003 //emerald
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 60) {
                        player.sendMessage("You need a Crafting level of 60 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 7014
                        || getData().getToUpgrade().getId() == 11183
                        || getData().getToUpgrade().getId() == 11184
                        || getData().getToUpgrade().getId() == 11179
                        || getData().getToUpgrade().getId() == 11762
                        || getData().getToUpgrade().getId() == 11182
                        || getData().getToUpgrade().getId() == 11181 //gold
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 62) {
                        player.sendMessage("You need a Crafting level of 62 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 21028
                        || getData().getToUpgrade().getId() == 21029
                        || getData().getToUpgrade().getId() == 21030 //saint
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 65) {
                        player.sendMessage("You need a Crafting level of 65 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 17391
                        || getData().getToUpgrade().getId() == 15888
                        || getData().getToUpgrade().getId() == 15818
                        || getData().getToUpgrade().getId() == 15924
                        || getData().getToUpgrade().getId() == 16023
                        || getData().getToUpgrade().getId() == 15935
                        || getData().getToUpgrade().getId() == 17686
                        || getData().getToUpgrade().getId() == 16272
                        || getData().getToUpgrade().getId() == 12994 //champion
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 68) {
                        player.sendMessage("You need a Crafting level of 68 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 22127
                        || getData().getToUpgrade().getId() == 22126
                        || getData().getToUpgrade().getId() == 17596
                        || getData().getToUpgrade().getId() == 22125
                        || getData().getToUpgrade().getId() == 22122
                        || getData().getToUpgrade().getId() == 22123
                        || getData().getToUpgrade().getId() == 12610 //rogue
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 70) {
                        player.sendMessage("You need a Crafting level of 70 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 22135
                        || getData().getToUpgrade().getId() == 15645
                        || getData().getToUpgrade().getId() == 15646
                        || getData().getToUpgrade().getId() == 15647 //solar
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 72) {
                        player.sendMessage("You need a Crafting level of 72 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 21023
                        || getData().getToUpgrade().getId() == 21020
                        || getData().getToUpgrade().getId() == 21021
                        || getData().getToUpgrade().getId() == 21022
                        || getData().getToUpgrade().getId() == 21024 //deviant
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 74) {
                        player.sendMessage("You need a Crafting level of 74 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 5012
                        || getData().getToUpgrade().getId() == 4684
                        || getData().getToUpgrade().getId() == 4685
                        || getData().getToUpgrade().getId() == 4686
                        || getData().getToUpgrade().getId() == 9939
                        || getData().getToUpgrade().getId() == 8274
                        || getData().getToUpgrade().getId() == 8273 //executive
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 76) {
                        player.sendMessage("You need a Crafting level of 76 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 17698
                        || getData().getToUpgrade().getId() == 17700
                        || getData().getToUpgrade().getId() == 17614
                        || getData().getToUpgrade().getId() == 17616
                        || getData().getToUpgrade().getId() == 17618
                        || getData().getToUpgrade().getId() == 17606
                        || getData().getToUpgrade().getId() == 17622
                        || getData().getToUpgrade().getId() == 11195 //nite amy
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 78) {
                        player.sendMessage("You need a Crafting level of 78 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 23066
                        || getData().getToUpgrade().getId() == 23067
                        || getData().getToUpgrade().getId() == 23061
                        || getData().getToUpgrade().getId() == 23062
                        || getData().getToUpgrade().getId() == 23063
                        || getData().getToUpgrade().getId() == 23068
                        || getData().getToUpgrade().getId() == 12612 //obby aura
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 80) {
                        player.sendMessage("You need a Crafting level of 80 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 14018
                        || getData().getToUpgrade().getId() == 19160
                        || getData().getToUpgrade().getId() == 19159
                        || getData().getToUpgrade().getId() == 19158 //malvek
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 82) {
                        player.sendMessage("You need a Crafting level of 82 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 20427
                        || getData().getToUpgrade().getId() == 20260
                        || getData().getToUpgrade().getId() == 20095 //onyx
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 84) {
                        player.sendMessage("You need a Crafting level of 84 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 8136
                        || getData().getToUpgrade().getId() == 8813
                        || getData().getToUpgrade().getId() == 8814
                        || getData().getToUpgrade().getId() == 8815
                        || getData().getToUpgrade().getId() == 17283
                        || getData().getToUpgrade().getId() == 16194 //blood
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 86) {
                        player.sendMessage("You need a Crafting level of 86 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 14188
                        || getData().getToUpgrade().getId() == 14184
                        || getData().getToUpgrade().getId() == 14178
                        || getData().getToUpgrade().getId() == 14186
                        || getData().getToUpgrade().getId() == 14180
                        || getData().getToUpgrade().getId() == 14182 //demonlord
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 88) {
                        player.sendMessage("You need a Crafting level of 88 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 22143
                        || getData().getToUpgrade().getId() == 22136
                        || getData().getToUpgrade().getId() == 22137
                        || getData().getToUpgrade().getId() == 22138
                        || getData().getToUpgrade().getId() == 22141
                        || getData().getToUpgrade().getId() == 22139
                        || getData().getToUpgrade().getId() == 22142 //lili aura
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 90) {
                        player.sendMessage("You need a Crafting level of 90 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 13640
                        || getData().getToUpgrade().getId() == 13964
                        || getData().getToUpgrade().getId() == 21934
                        || getData().getToUpgrade().getId() == 19918
                        || getData().getToUpgrade().getId() == 19913
                        || getData().getToUpgrade().getId() == 3107
                        || getData().getToUpgrade().getId() == 15448 //groudon aura
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 91) {
                        player.sendMessage("You need a Crafting level of 91 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 22148
                        || getData().getToUpgrade().getId() == 22151
                        || getData().getToUpgrade().getId() == 22145
                        || getData().getToUpgrade().getId() == 22146
                        || getData().getToUpgrade().getId() == 22147
                        || getData().getToUpgrade().getId() == 22149
                        || getData().getToUpgrade().getId() == 22150 //varth
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 92) {
                        player.sendMessage("You need a Crafting level of 92 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 17694
                        || getData().getToUpgrade().getId() == 17696
                        || getData().getToUpgrade().getId() == 14190
                        || getData().getToUpgrade().getId() == 14192
                        || getData().getToUpgrade().getId() == 14194
                        || getData().getToUpgrade().getId() == 14200
                        || getData().getToUpgrade().getId() == 14198
                        || getData().getToUpgrade().getId() == 14196
                        || getData().getToUpgrade().getId() == 12608 //tyrant aura
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 93) {
                        player.sendMessage("You need a Crafting level of 93 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 17644
                        || getData().getToUpgrade().getId() == 22100
                        || getData().getToUpgrade().getId() == 22101
                        || getData().getToUpgrade().getId() == 22102
                        || getData().getToUpgrade().getId() == 22105
                        || getData().getToUpgrade().getId() == 22103
                        || getData().getToUpgrade().getId() == 22104 //lucifer
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 94) {
                        player.sendMessage("You need a Crafting level of 94 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 14305
                        || getData().getToUpgrade().getId() == 14307
                        || getData().getToUpgrade().getId() == 14202
                        || getData().getToUpgrade().getId() == 14204
                        || getData().getToUpgrade().getId() == 14206
                        || getData().getToUpgrade().getId() == 14303
                        || getData().getToUpgrade().getId() == 14301 //virtuos
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 95) {
                        player.sendMessage("You need a Crafting level of 95 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 22155
                        || getData().getToUpgrade().getId() == 22152
                        || getData().getToUpgrade().getId() == 22153
                        || getData().getToUpgrade().getId() == 22154
                        || getData().getToUpgrade().getId() == 22158
                        || getData().getToUpgrade().getId() == 22159
                        || getData().getToUpgrade().getId() == 22160 //agumon aura
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 97) {
                        player.sendMessage("You need a Crafting level of 97 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 22167
                        || getData().getToUpgrade().getId() == 22163
                        || getData().getToUpgrade().getId() == 22165
                        || getData().getToUpgrade().getId() == 22164
                        || getData().getToUpgrade().getId() == 22166
                        || getData().getToUpgrade().getId() == 22161
                        || getData().getToUpgrade().getId() == 22162 //white
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 98) {
                        player.sendMessage("You need a Crafting level of 98 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 23079
                        || getData().getToUpgrade().getId() == 23080
                        || getData().getToUpgrade().getId() == 23075
                        || getData().getToUpgrade().getId() == 23076
                        || getData().getToUpgrade().getId() == 23077 //soldier
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 99) {
                        player.sendMessage("You need a Crafting level of 99 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 14319
                        || getData().getToUpgrade().getId() == 14309
                        || getData().getToUpgrade().getId() == 14311
                        || getData().getToUpgrade().getId() == 14313
                        || getData().getToUpgrade().getId() == 14321
                        || getData().getToUpgrade().getId() == 14317
                        || getData().getToUpgrade().getId() == 14315 //fighter
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 100) {
                        player.sendMessage("You need a Crafting level of 100 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 22133
                        || getData().getToUpgrade().getId() == 14325
                        || getData().getToUpgrade().getId() == 14327
                        || getData().getToUpgrade().getId() == 14331
                        || getData().getToUpgrade().getId() == 14329
                        || getData().getToUpgrade().getId() == 14323 //evil
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 101) {
                        player.sendMessage("You need a Crafting level of 101 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 14353
                        || getData().getToUpgrade().getId() == 14349
                        || getData().getToUpgrade().getId() == 14359
                        || getData().getToUpgrade().getId() == 14363
                        || getData().getToUpgrade().getId() == 14339
                        || getData().getToUpgrade().getId() == 14347
                        || getData().getToUpgrade().getId() == 14355
                        || getData().getToUpgrade().getId() == 14341
                        || getData().getToUpgrade().getId() == 14345
                        || getData().getToUpgrade().getId() == 14343
                        || getData().getToUpgrade().getId() == 14351
                        || getData().getToUpgrade().getId() == 14361
                        || getData().getToUpgrade().getId() == 14337
                        || getData().getToUpgrade().getId() == 14357 //death wings
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 102) {
                        player.sendMessage("You need a Crafting level of 102 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 8828
                        || getData().getToUpgrade().getId() == 8829
                        || getData().getToUpgrade().getId() == 8833
                        || getData().getToUpgrade().getId() == 8830
                        || getData().getToUpgrade().getId() == 8831 //charybde ring
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 104) {
                        player.sendMessage("You need a Crafting level of 104 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 14369
                        || getData().getToUpgrade().getId() == 14373
                        || getData().getToUpgrade().getId() == 14371
                        || getData().getToUpgrade().getId() == 14375
                        || getData().getToUpgrade().getId() == 14365
                        || getData().getToUpgrade().getId() == 14367 //scylla
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 106) {
                        player.sendMessage("You need a Crafting level of 106 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 22072
                        || getData().getToUpgrade().getId() == 22036
                        || getData().getToUpgrade().getId() == 22037
                        || getData().getToUpgrade().getId() == 22038
                        || getData().getToUpgrade().getId() == 5594
                        || getData().getToUpgrade().getId() == 6937
                        || getData().getToUpgrade().getId() == 3905 //death
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 108) {
                        player.sendMessage("You need a Crafting level of 108 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 20552
                        || getData().getToUpgrade().getId() == 15008
                        || getData().getToUpgrade().getId() == 15005
                        || getData().getToUpgrade().getId() == 15006
                        || getData().getToUpgrade().getId() == 15007
                        || getData().getToUpgrade().getId() == 15100
                        || getData().getToUpgrade().getId() == 15201
                        || getData().getToUpgrade().getId() == 15200 //gladiator
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 109) {
                        player.sendMessage("You need a Crafting level of 109 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 14379
                        || getData().getToUpgrade().getId() == 14381
                        || getData().getToUpgrade().getId() == 14383
                        || getData().getToUpgrade().getId() == 14385 //afreet
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 110) {
                        player.sendMessage("You need a Crafting level of 110 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 17702
                        || getData().getToUpgrade().getId() == 11763
                        || getData().getToUpgrade().getId() == 11764
                        || getData().getToUpgrade().getId() == 11765
                        || getData().getToUpgrade().getId() == 11767
                        || getData().getToUpgrade().getId() == 11766 //frieza
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 112) {
                        player.sendMessage("You need a Crafting level of 112 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 7543
                        || getData().getToUpgrade().getId() == 7544
                        || getData().getToUpgrade().getId() == 9481
                        || getData().getToUpgrade().getId() == 9482
                        || getData().getToUpgrade().getId() == 9483
                        || getData().getToUpgrade().getId() == 7545 //perfect cell
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 114) {
                        player.sendMessage("You need a Crafting level of 114 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 16249
                        || getData().getToUpgrade().getId() == 15832
                        || getData().getToUpgrade().getId() == 9478
                        || getData().getToUpgrade().getId() == 9479
                        || getData().getToUpgrade().getId() == 9480
                        || getData().getToUpgrade().getId() == 16265 //super buu
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 116) {
                        player.sendMessage("You need a Crafting level of 116 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 8410
                        || getData().getToUpgrade().getId() == 8411
                        || getData().getToUpgrade().getId() == 8412
                        || getData().getToUpgrade().getId() == 13323
                        || getData().getToUpgrade().getId() == 13324
                        || getData().getToUpgrade().getId() == 13325
                        || getData().getToUpgrade().getId() == 1486
                        || getData().getToUpgrade().getId() == 13327
                        || getData().getToUpgrade().getId() == 13326 //goku
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 118) {
                        player.sendMessage("You need a Crafting level of 118 to upgrade this item.");
                        return false;
                    }
                }
                if (getData().getToUpgrade().getId() == 13333
                        || getData().getToUpgrade().getId() == 13328
                        || getData().getToUpgrade().getId() == 13329
                        || getData().getToUpgrade().getId() == 13330
                        || getData().getToUpgrade().getId() == 4369
                        || getData().getToUpgrade().getId() == 13332
                        || getData().getToUpgrade().getId() == 3318 //byakuya
                ) {
                    if (player.getSkillManager().getCurrentLevel(Skill.CRAFTING) < 120) {
                        player.sendMessage("You need a Crafting level of 120 to upgrade this item.");
                        return false;
                    }
                }
                upgrade();
                return true;
            }
        }
        return false;
    }

    public void sendRequirements() {
        player.getPacketSender().resetItemsOnInterface(19422, 6);
        for(int i = 0; i < getData().getRequiredItems().length; i++) {
            player.getPacketSender().sendItemOnInterface(19422, getData().getRequiredItems()[i].getId(), i, getData().getRequiredItems()[i].getAmount());
        }
    }

    public void setCategory(UpgradeInterfaceData @NotNull [] category) {
        this.category = category;
        setData(getCategory()[0]);
        player.getPacketSender().setScrollMax(19419, (category.length/6)*45);
        player.getPacketSender().sendItemContainer(getCategory(),19420);
    }

    public void setData(UpgradeInterfaceData data) {
        this.data = data;
        sendRequirements();
        player.getPacketSender().sendString(19418, "Upgraded " + (getData().getName() == null ? getData().getUpgradedItem().getDefinition().getName() : getData().getName()));
        player.getPacketSender().sendItemOnInterface(19428, getData().getUpgradedItem().getId(), 0, getData().getUpgradedItem().getAmount());
    }

    public UpgradeInterfaceData[] getCategory() {
        return this.category;
    }

    public UpgradeInterfaceData getData() {
        return this.data;
    }

}
