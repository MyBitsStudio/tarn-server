package com.ruse.world.packages.dissolve;

import com.ruse.model.Animation;
import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.player.Player;

public class DissolveItem {

    public static int TOKENS = 10835;
    public static boolean dissolveItem(Player player, int id, int slot){
        switch(id){
            case 19984:
            case 19985:
            case 19986:
            case 20400:
            case 19989:
            case 19988:
            case 19992:
            case 19991:
            case 14484:
            case 22174:
            case 22175:
                Item toDissolveItem = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem.getId())) {
                    player.getInventory().delete(toDissolveItem)
                            .add(TOKENS, 5);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 142);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 5 + " @or2@tokens.");
                }
                return true;

            case 20086:
            case 20087:
            case 20088:
            case 20089:
            case 20091:
            case 20093:
            case 20092:
            case 18750:
            case 18636:
            case 18629:
                Item toDissolveItem1 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem1.getId())) {
                    player.getInventory().delete(toDissolveItem1)
                            .add(TOKENS, 20);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 298);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 20 + " @or2@tokens.");
                }
                return true;

            case 18011:
            case 17999:
            case 18001:
            case 18003:
            case 18005:
            case 18009:
                Item toDissolveItem2 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem2.getId())) {
                    player.getInventory().delete(toDissolveItem2)
                            .add(TOKENS, 30);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 352);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 30 + " @or2@tokens.");
                }
                return true;

            case 23026:
            case 23027:
            case 23021:
            case 23022:
            case 23023:
            case 23025:
            case 23024:
            case 23033:
            case 23028:
            case 23029:
            case 23030:
            case 23032:
            case 23031:
            case 23039:
            case 23034:
            case 23035:
            case 23036:
            case 23038:
            case 23037:
                Item toDissolveItem3 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem3.getId())) {
                    player.getInventory().delete(toDissolveItem3)
                            .add(TOKENS, 50);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 472);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 50 + " @or2@tokens.");
                }
                return true;

            case 23055:
            case 23056:
            case 23050:
            case 23051:
            case 23052:
            case 23054:
            case 19135:
            case 20592:
            case 20593:
            case 20594:
            case 4367:
            case 8334:
            case 11140:
            case 8335:
            case 19892:
            case 20542:
            case 13306:
            case 13300:
            case 13301:
            case 13304:
            case 18683:
            case 13305:
            case 13302:
            case 21055:
            case 21062:
            case 21063:
            case 21064:
            case 21071:
            case 21067:
            case 21066:
            case 21069:
            case 21068:
            case 21048:
            case 21049:
            case 21036:
            case 21037:
            case 21038:
            case 21039:
            case 21041:
            case 21040:
            case 17664:
            case 23134:
            case 23135:
            case 23136:
            case 23138:
            case 23137:
            case 14915:
            case 14910:
            case 14911:
            case 14912:
            case 14914:
            case 14913:
            case 14377:
            case 14733:
            case 14732:
            case 14734:
            case 10865:
            case 12864:
            case 8816:
            case 8817:
            case 8818:
            case 8820:
            case 8819:
            case 23146:
            case 23145:
            case 23144:
                Item toDissolveItem4 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem4.getId())) {
                    player.getInventory().delete(toDissolveItem4)
                            .add(TOKENS, 75);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 3022);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 75 + " @or2@tokens.");
                }
                return true;

            case 15922:
            case 16021:
            case 15933:
            case 12614:
            case 17710:
            case 5420:
            case 5422:
            case 5428:
            case 17684:
            case 9940:
            case 21042:
            case 21043:
            case 21044:
            case 21045:
            case 21047:
            case 21046:
            case 8803:
            case 8804:
            case 8805:
            case 8809:
            case 8806:
            case 8807:
            case 8808:
            case 21018:
            case 14050:
            case 14051:
            case 14052:
            case 1485:
            case 14053:
            case 14055:
            case 8088:
            case 11001:
            case 11002:
            case 11003:
            case 7014:
            case 11183:
            case 11184:
            case 11179:
            case 11762:
            case 11182:
            case 11181:
            case 10887:
            case 23092:
            case 23093:
            case 23094:
            case 21028:
            case 21029:
            case 21030:
            case 17391:
                Item toDissolveItem5 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem5.getId())) {
                    player.getInventory().delete(toDissolveItem5)
                            .add(TOKENS, 100);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 5211);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 100 + " @or2@tokens.");
                }
                return true;

            case 15888:
            case 15818:
            case 15924:
            case 16023:
            case 15935:
            case 17686:
            case 16272:
            case 12994:
            case 22127:
            case 22126:
            case 17596:
            case 22125:
            case 22122:
            case 22123:
            case 12610:
                Item toDissolveItem6 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem6.getId())) {
                    player.getInventory().delete(toDissolveItem6)
                            .add(TOKENS, 150);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 7902);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 150 + " @or2@tokens.");
                }
                return true;

            case 22135:
            case 15645:
            case 15646:
            case 15647:
                Item toDissolveItem8 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem8.getId())) {
                    player.getInventory().delete(toDissolveItem8)
                            .add(TOKENS, 2000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 10823);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 2000 + " @or2@tokens.");
                }
                return true;
            case 21023:
            case 21020:
            case 21021:
            case 21022:
            case 21024:
                Item toDissolveItem9 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem9.getId())) {
                    player.getInventory().delete(toDissolveItem9)
                            .add(TOKENS, 3000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 15987);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 3000 + " @or2@tokens.");
                }
                return true;
            case 5012:
            case 4684:
            case 4685:
            case 4686:
            case 9939:
            case 8274:
            case 8273:
                Item toDissolveItem10 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem10.getId())) {
                    player.getInventory().delete(toDissolveItem10)
                            .add(TOKENS, 4500);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 17123);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 4500 + " @or2@tokens.");
                }
                return true;
            case 17698:
            case 17700:
            case 17614:
            case 17616:
            case 17618:
            case 17606:
            case 17622:
            case 11195:
                Item toDissolveItem11 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem11.getId())) {
                    player.getInventory().delete(toDissolveItem11)
                            .add(TOKENS, 7500);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 20423);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 7500 + " @or2@tokens.");
                }
                return true;
            case 23066:
            case 23067:
            case 23061:
            case 23062:
            case 23063:
            case 23068:
            case 12612:
                Item toDissolveItem12 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem12.getId())) {
                    player.getInventory().delete(toDissolveItem12)
                            .add(TOKENS, 9000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 29566);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 9000 + " @or2@tokens.");
                }
                return true;
            case 14018:
            case 19160:
            case 19159:
            case 19158:
                Item toDissolveItem13 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem13.getId())) {
                    player.getInventory().delete(toDissolveItem13)
                            .add(TOKENS, 10000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 38122);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 10000 + " @or2@tokens.");
                }
                return true;
            case 20427:
            case 20260:
            case 20095:
                Item toDissolveItem14 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem14.getId())) {
                    player.getInventory().delete(toDissolveItem14)
                            .add(TOKENS, 15000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 47999);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 15000 + " @or2@tokens.");
                }
                return true;
            case 8136:
            case 8813:
            case 8814:
            case 8815:
            case 17283:
            case 16194:
            case 1857:
                Item toDissolveItem15 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem15.getId())) {
                    player.getInventory().delete(toDissolveItem15)
                            .add(TOKENS, 17500);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 53012);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 17500 + " @or2@tokens.");
                }
                return true;
            case 14188:
            case 14184:
            case 14178:
            case 14186:
            case 14180:
            case 14182:
                Item toDissolveItem16 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem16.getId())) {
                    player.getInventory().delete(toDissolveItem16)
                            .add(TOKENS, 20000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 63465);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 20000 + " @or2@tokens.");
                }
                return true;
            case 22143:
            case 22136:
            case 22137:
            case 22138:
            case 22141:
            case 22139:
            case 22142:
                Item toDissolveItem17 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem17.getId())) {
                    player.getInventory().delete(toDissolveItem17)
                            .add(TOKENS, 25000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 68912);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 25000 + " @or2@tokens.");
                }
                return true;
            case 13640:
            case 13964:
            case 21934:
            case 19918:
            case 19913:
            case 3107:
            case 15448:
                Item toDissolveItem18 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem18.getId())) {
                    player.getInventory().delete(toDissolveItem18)
                            .add(TOKENS, 30000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 72993);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 30000 + " @or2@tokens.");
                }
                return true;
            case 22148:
            case 22151:
            case 22145:
            case 22146:
            case 22147:
            case 22149:
            case 22150:
                Item toDissolveItem19 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem19.getId())) {
                    player.getInventory().delete(toDissolveItem19)
                            .add(TOKENS, 40000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 77123);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 40000 + " @or2@tokens.");
                }
                return true;
            case 17694:
            case 17696:
            case 14190:
            case 14192:
            case 14194:
            case 14200:
            case 14198:
            case 14196:
            case 12608:
                Item toDissolveItem20 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem20.getId())) {
                    player.getInventory().delete(toDissolveItem20)
                            .add(TOKENS, 45000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 82913);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 45000 + " @or2@tokens.");
                }
                return true;
            case 17644:
            case 22100:
            case 22101:
            case 22102:
            case 22105:
            case 22103:
            case 22104:
                Item toDissolveItem21 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem21.getId())) {
                    player.getInventory().delete(toDissolveItem21)
                            .add(TOKENS, 50000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 88713);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 50000 + " @or2@tokens.");
                }
                return true;
            case 14305:
            case 14307:
            case 14202:
            case 14204:
            case 14206:
            case 14303:
            case 14301:
                Item toDissolveItem22 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem22.getId())) {
                    player.getInventory().delete(toDissolveItem22)
                            .add(TOKENS, 60000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 92031);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 60000 + " @or2@tokens.");
                }
                return true;
            case 22155:
            case 22152:
            case 22153:
            case 22154:
            case 22158:
            case 22159:
            case 22160:
                Item toDissolveItem23 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem23.getId())) {
                    player.getInventory().delete(toDissolveItem23)
                            .add(TOKENS, 75000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 99813);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 75000 + " @or2@tokens.");
                }
                return true;
            case 22167:
            case 22163:
            case 22165:
            case 22164:
            case 22166:
            case 22161:
            case 22162:
                Item toDissolveItem24 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem24.getId())) {
                    player.getInventory().delete(toDissolveItem24)
                            .add(TOKENS, 85000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 101233);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 85000 + " @or2@tokens.");
                }
                return true;
            case 5730:
            case 23079:
            case 23080:
            case 23075:
            case 23076:
            case 23077:
                Item toDissolveItem25 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem25.getId())) {
                    player.getInventory().delete(toDissolveItem25)
                            .add(TOKENS, 90000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 104812);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 90000 + " @or2@tokens.");
                }
                return true;
            case 14319:
            case 14309:
            case 14311:
            case 14313:
            case 14321:
            case 14317:
            case 14315:
                Item toDissolveItem26 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem26.getId())) {
                    player.getInventory().delete(toDissolveItem26)
                            .add(TOKENS, 100000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 107283);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 100000 + " @or2@tokens.");
                }
                return true;
            case 22133:
            case 14325:
            case 14327:
            case 14331:
            case 14329:
            case 14323:
                Item toDissolveItem27 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem27.getId())) {
                    player.getInventory().delete(toDissolveItem27)
                            .add(TOKENS, 115000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 115767);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 115000 + " @or2@tokens.");
                }
                return true;
            case 14353:
            case 14349:
            case 14359:
            case 14363:
            case 14339:
            case 14347:
            case 14355:
            case 14341:
            case 14345:
            case 14343:
            case 14351:
            case 14361:
            case 14337:
            case 14357:
                Item toDissolveItem28 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem28.getId())) {
                    player.getInventory().delete(toDissolveItem28)
                            .add(TOKENS, 130000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 134812);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 130000 + " @or2@tokens.");
                }
                return true;
            case 8828:
            case 8829:
            case 8833:
            case 8830:
            case 8831:
                Item toDissolveItem29 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem29.getId())) {
                    player.getInventory().delete(toDissolveItem29)
                            .add(TOKENS, 145000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 164772);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 145000 + " @or2@tokens.");
                }
                return true;
            case 14369:
            case 14373:
            case 14371:
            case 14375:
            case 14365:
            case 14367:
                Item toDissolveItem30 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem30.getId())) {
                    player.getInventory().delete(toDissolveItem30)
                            .add(TOKENS, 160000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 175684);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 160000 + " @or2@tokens.");
                }
                return true;
            case 22072:
            case 22036:
            case 22037:
            case 22038:
            case 5594:
            case 6937:
            case 3905:
                Item toDissolveItem31 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem31.getId())) {
                    player.getInventory().delete(toDissolveItem31)
                            .add(TOKENS, 175000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 187443);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 175000 + " @or2@tokens.");
                }
                return true;
            case 20552:
            case 15008:
            case 15005:
            case 15006:
            case 15007:
            case 15100:
            case 15201:
            case 15200:
                Item toDissolveItem32 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem32.getId())) {
                    player.getInventory().delete(toDissolveItem32)
                            .add(TOKENS, 190000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 190772);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 190000 + " @or2@tokens.");
                }
                return true;
            case 14379:
            case 14381:
            case 14383:
            case 14385:
                Item toDissolveItem33 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem33.getId())) {
                    player.getInventory().delete(toDissolveItem33)
                            .add(TOKENS, 200000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 201734);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 200000 + " @or2@tokens.");
                }
                return true;
            case 17702:
            case 11763:
            case 11764:
            case 11765:
            case 11767:
            case 11766:
                Item toDissolveItem34 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem34.getId())) {
                    player.getInventory().delete(toDissolveItem34)
                            .add(TOKENS, 225000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 217645);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 225000 + " @or2@tokens.");
                }
                return true;
            case 7543:
            case 7544:
            case 9481:
            case 9482:
            case 9483:
            case 7545:
                Item toDissolveItem35 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem35.getId())) {
                    player.getInventory().delete(toDissolveItem35)
                            .add(TOKENS, 250000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 267433);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 250000 + " @or2@tokens.");
                }
                return true;
            case 16249:
            case 15832:
            case 9478:
            case 9479:
            case 9480:
            case 16265:
                Item toDissolveItem36 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem36.getId())) {
                    player.getInventory().delete(toDissolveItem36)
                            .add(TOKENS, 275000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 287432);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 275000 + " @or2@tokens.");
                }
                return true;
            case 8410:
            case 8411:
            case 8412:
            case 13323:
            case 13324:
            case 13325:
            case 1486:
            case 13327:
            case 13326:
                Item toDissolveItem37 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem37.getId())) {
                    player.getInventory().delete(toDissolveItem37)
                            .add(TOKENS, 300000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 309871);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 300000 + " @or2@tokens.");
                }
                return true;
            case 13333:
            case 13328:
            case 13329:
            case 13330:
            case 4369:
            case 13332:
            case 3318:
                Item toDissolveItem38 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem38.getId())) {
                    player.getInventory().delete(toDissolveItem38)
                            .add(TOKENS, 500000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 365772);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 500000 + " @or2@tokens.");
                }
                return true;

            case 4071:
            case 4072:
            case 4066:
            case 4067:
            case 4068:
            case 4069:
            case 4070:
                Item toDissolveItem40 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem40.getId())) {
                    player.getInventory().delete(toDissolveItem40)
                            .add(TOKENS, 2000000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 365772);
                    player.getSeasonPass().incrementExp(10000, false);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 2000000 + " @or2@tokens.");
                }
                return true;

            case 3472:
            case 4075:
            case 4077:
            case 3473:
            case 3470:
            case 4085:
            case 4083:
                Item toDissolveItem41 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem41.getId())) {
                    player.getInventory().delete(toDissolveItem41)
                            .add(TOKENS, 3000000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 365772);
                    player.getSeasonPass().incrementExp(17000, false);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 3000000 + " @or2@tokens.");
                }
                return true;

            case 12843:
                Item toDissolveItem39 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem39.getId())) {
                    player.getInventory().delete(toDissolveItem39)
                            .add(TOKENS, 5000000);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 573182);
                    player.getSeasonPass().incrementExp(22000, false);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 5000000 + " @or2@tokens.");
                }
                return true;

            case 14438:
            case 14440:
            case 14442:
            case 14444:
            case 14446:
                Item toDissolveItem42 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem42.getId())) {
                    player.getInventory().delete(toDissolveItem42)
                            .add(13650, 200);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 573182);
                    player.getSeasonPass().incrementExp(28000, false);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(13650).getName() + "@or2@ for@red@ " + 200 + " @or2@ Counter Tokens.");
                }
                return true;

            case 8100:
            case 8101:
            case 8102:
            case 8103:
            case 8104:
                Item toDissolveItem43 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem43.getId())) {
                    player.getInventory().delete(toDissolveItem43)
                            .add(13650, 300);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 573182);
                    player.getSeasonPass().incrementExp(30000, false);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(13650).getName() + "@or2@ for@red@ " + 300 + " @or2@ Counter Tokens.");
                }
                return true;

            case 8105:
            case 8106:
            case 8107:
            case 8108:
            case 8109:
            case 8110:
                Item toDissolveItem44 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem44.getId())) {
                    player.getInventory().delete(toDissolveItem44)
                            .add(13650, 500);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 573182);
                    player.getSeasonPass().incrementExp(35000, false);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(13650).getName() + "@or2@ for@red@ " + 500 + " @or2@ Counter Tokens.");
                }
                return true;

            case 2028:
            case 2030:
            case 2032:
            case 2034:
            case 2036:
            case 2038:
            case 2040:
            case 2042:
            case 2044:
            case 2046:
            case 2048:
            case 2050:
            case 2052:
            case 2054:
            case 2056:
            case 2058:
            case 2060:
            case 2062:
            case 2064:
            case 2066:
            case 2068:
            case 2070:
            case 2072:
            case 2074:
            case 2076:
            case 2078:
            case 2080:
            case 2082:
            case 2084:
            case 2086:
            case 2088:
            case 2090:
            case 2092:
            case 2094:
            case 2096:
            case 2098:
            case 2100:
            case 2102:
            case 2104:
            case 2106:
            case 2108:
            case 2110:
            case 2112:
            case 2114:
            case 2116:
            case 2118:
            case 2120:
            case 2122:
            case 2124:
            case 2126:
            case 2128:
            case 2130:
            case 2132:
            case 2134:
            case 2136:
            case 2138:
            case 2140:
            case 2142:
            case 2144:
            case 2146:
            case 2148:
            case 2150:
            case 2152:
            case 2154:
            case 2156:
            case 2158:
            case 2160:
            case 2162:
            case 2164:
            case 2166:
            case 2168:
            case 2170:
            case 2172:
            case 2174:
            case 2176:
            case 2178:
            case 2180:
            case 2182:
            case 2184:
            case 2186:
            case 2188:
            case 2190:
            case 2192:
            case 2194:
            case 2196:
            case 2198:
            case 2200:
            case 2202:
            case 2204:
            case 2206:
            case 2208:
            case 2210:
            case 2212:
            case 2214:
            case 2216:
            case 2218:
            case 2220:
            case 2222:
            case 2224:
            case 2226:
            case 2228:
            case 2230:
            case 2232:
            case 2234:
            case 2236:
            case 2238:
            case 2240:
            case 2242:
            case 2244:
            case 2246:
            case 2248:
            case 2250:
            case 2252:
            case 2254:
            case 2256:
            case 2258:
            case 2260:
            case 2262:
            case 2264:
            case 2266:
            case 2268:
            case 2270:
            case 2272:
            case 2274:
            case 2276:
            case 2278:
            case 2280:
            case 2282:
            case 2284:
            case 2286:
            case 2288:
            case 2290:
            case 2292:
            case 2294:
            case 2296:
            case 2298:
            case 2300:
            case 2302:
            case 2304:
            case 2306:
            case 2308:
            case 2310:
            case 2312:
            case 2314:
            case 2316:
            case 2318:
            case 2320:
            case 2322:
            case 2324:
            case 2326:
            case 2328:
            case 2330:
            case 2332:
            case 2334:
            case 2336:
            case 2338:
            case 2340:
            case 2342:
            case 2344:
            case 2346:
            case 2348:
            case 2350:
            case 2352:
            case 2354:
            case 2356:
            case 2358:
            case 2360:
            case 2362:
            case 2364:
            case 2366:
            case 2368:
            case 2370:
            case 2372:
            case 2374:
            case 2376:
            case 2378:
            case 2384:
            case 2386:
            case 2388:
            case 2390:
            case 2392:
            case 2394:
            case 2397:
            case 2398:
            case 2400:
            case 2402:
            case 2404:
            case 2406:
            case 2408:
            case 2410:
            case 2412:
            case 2414:
            case 2416:
            case 2418:
            case 2420:
            case 2422:
            case 2424:
            case 2426:
            case 2428:
            case 2430:
            case 2432:
            case 2434:
            case 2436:
            case 2438:
            case 2440:
            case 2442:
            case 2444:
            case 2446:
            case 2448:
            case 2450:
            case 2452:
            case 2454:
            case 2456:
            case 2458:
            case 2460:
            case 2462:
            case 2464:
            case 2466:
            case 2468:
            case 2470:
            case 2472:
            case 2474:
            case 2476:
            case 2478:
            case 2480:
            case 2482:
            case 2484:
            case 2486:
            case 2488:
            case 2490:
            case 2492:
            case 2494:
            case 2496:
            case 2498:
            case 2500:
            case 2502:
            case 2504:
            case 2506:
            case 2508:
            case 2510:
            case 2512:
            case 2514:
            case 2516:
            case 2518:
            case 2520:
            case 2522:
            case 2524:
            case 2526:
            case 2528:
            case 2530:
            case 2688:
            case 2532:
            case 2534:
            case 2536:
            case 2538:
            case 2540:
            case 2542:
            case 2544:
            case 2546:
            case 2548:
            case 2550:
            case 2552:
            case 2554:
            case 2556:
            case 2558:
            case 2560:
            case 2562:
            case 2564:
            case 2566:
            case 2568:
            case 2570:
            case 2572:
            case 2574:
            case 2576:
            case 2578:
            case 2580:
            case 2582:
            case 2584:
            case 2586:
            case 2588:
            case 2590:
            case 2592:
            case 2594:
            case 2596:
            case 2598:
            case 2600:
            case 2602:
            case 2604:
            case 2606:
            case 2608:
            case 2610:
            case 2612:
            case 2614:
            case 2616:
            case 2618:
            case 2620:
            case 2622:
            case 2624:
            case 2626:
            case 2628:
            case 2630:
            case 2632:
            case 2634:
            case 2636:
            case 2638:
            case 2640:
            case 2642:
            case 2644:
            case 2646:
            case 2648:
            case 2650:
            case 2652:
            case 2654:
            case 2656:
            case 2658:
            case 2660:
            case 2662:
            case 2664:
            case 2666:
            case 2668:
            case 2670:
            case 2672:
            case 2674:
            case 2676:
            case 2678:
            case 2680:
            case 2682:
            case 2684:
            case 2686:
                Item toDissolveItem45 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem45.getId())) {
                    player.getInventory().delete(toDissolveItem45)
                            .add(20503, 1);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 1000);
                    player.getSeasonPass().incrementExp(2500, false);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 1 + " @or2@token.");
                }
                return true;

            case 22229:
            case 22230:
            case 22231:
            case 22232:
            case 22233:
            case 22234:
            case 22235:
            case 22236:
            case 22237:
            case 22238:
            case 22239:
            case 22240:
                Item toDissolveItem46 = player.getInventory().get(slot);
                if (player.getInventory().contains(toDissolveItem46.getId())) {
                    player.getInventory().delete(toDissolveItem46)
                            .add(6640, 1);
                    player.performAnimation(new Animation(712));
                    player.getSkillManager().addExperience(Skill.CRAFTING, 1000000);
                    player.getSeasonPass().incrementExp(100000, false);
                    player.getPacketSender().sendMessage("@or2@You have dissolved @red@" + ItemDefinition.forId(id).getName() + "@or2@ for@red@ " + 1 + " @or2@Owner's Gemstone.");
                }
                return true;
        }
        return false;
    }
}
