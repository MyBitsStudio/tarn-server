package com.ruse.world.packages.dialogue;

import com.ruse.model.definitions.NpcDefinition;
import com.ruse.world.content.PetUpgrading;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@Getter
@Setter
public abstract class Dialogue {

    private final int[] NPC_DIALOGUE_ID = { 4885, 4890, 4896, 4903 };

    private final int[] PLAYER_DIALOGUE_ID = { 971, 976, 982, 989 };

    public static final int[] OPTION_DIALOGUE_ID = { 13760, 2461, 2471, 2482, 2494 };
    protected final int FIRST_OPTION_OF_FIVE = 2494;
    protected final int SECOND_OPTION_OF_FIVE = 2495;
    protected final int THIRD_OPTION_OF_FIVE = 2496;
    protected final int FOURTH_OPTION_OF_FIVE = 2497;
    protected final int FIFTH_OPTION_OF_FIVE = 2498;
    protected final int FIRST_OPTION_OF_FOUR = 2482;
    protected final int SECOND_OPTION_OF_FOUR = 2483;
    protected final int THIRD_OPTION_OF_FOUR = 2484;
    protected final int FOURTH_OPTION_OF_FOUR = 2485;
    protected final int FIRST_OPTION_OF_THREE = 2471;
    protected final int SECOND_OPTION_OF_THREE = 2472;
    protected final int THIRD_OPTION_OF_THREE = 2473;
    protected final int FIRST_OPTION_OF_TWO = 2461;
    protected final int SECOND_OPTION_OF_TWO = 2462;
    protected HashMap<Integer, String> options = new HashMap<>();

    private int stage = 0, npcId = -1, option = -1;

    private final Player player;

    public Dialogue(Player player){
        this.player = player;
    }

    public abstract DialogueType type();

    public abstract DialogueExpression animation();

    public abstract String[] items();

    public abstract void next(int stage);

    public abstract int id();

    public abstract void onClose();

    public abstract boolean handleOption(int option);

    public void start(){
        start(-1);
    }

    public void start(int npcId){
        if (player.isBanking() || player.isShopping() || player.getInterfaceId() > 0 && player.getInterfaceId() != 50 && player.getInterfaceId() != 49510)
            player.getPacketSender().sendInterfaceRemoval();
        setNpcId(npcId);
        if (player.getInterfaceId() != 50)
            player.setInterfaceId(50);
        next(stage++);
    }

    public void end(){
        setStage(-1);
        setNpcId(-1);
        player.getPacketSender().sendInterfaceRemoval();
        onClose();
    }

    public void nextStage(){
        if(stage == -1){
            end();
            return;
        }
        next(stage++);
    }

    protected void sendNpcChat(String @NotNull ... lines){
        int startDialogueChildId = NPC_DIALOGUE_ID[lines.length - 1];
        int headChildId = startDialogueChildId - 2;
        player.getPacketSender().sendNpcHeadOnInterface(getNpcId(), headChildId);
        player.getPacketSender().sendInterfaceAnimation(headChildId, animation().getAnimation());
        player.getPacketSender().sendString(startDialogueChildId - 1,
                NpcDefinition.forId(getNpcId()) != null
                        ? NpcDefinition.forId(getNpcId()).getName().replaceAll("_", " ")
                        : "");
        for (int i = 0; i < lines.length; i++) {
            player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
        }
        player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
    }

    protected void sendPlayerChat(String @NotNull ... lines){
        int startDialogueChildId = PLAYER_DIALOGUE_ID[lines.length - 1];
        int headChildId = startDialogueChildId - 2;
        player.getPacketSender().sendPlayerHeadOnInterface(headChildId);
        player.getPacketSender().sendInterfaceAnimation(headChildId, animation().getAnimation());
        player.getPacketSender().sendString(startDialogueChildId - 1, player.getUsername());
        for (int i = 0; i < lines.length; i++) {
            player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
        }
        player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
    }

    protected void sendItemChat(String @NotNull ... lines){
        int startDialogueChildId = PLAYER_DIALOGUE_ID[lines.length - 1];
        int headChildId = startDialogueChildId - 2;
        player.getPacketSender().sendInterfaceModel(headChildId, Integer.parseInt(items()[0]),
                Integer.parseInt(items()[1]));
        player.getPacketSender().sendString(startDialogueChildId - 1, items()[2]);
        for (int i = 0; i < lines.length; i++) {
            player.getPacketSender().sendString(startDialogueChildId + i, lines[i]);
        }
        player.getPacketSender().sendChatboxInterface(startDialogueChildId - 3);
    }

    protected void sendRegularStatement(String @NotNull ... lines){
        player.getPacketSender().sendString(357, lines[0]);
        player.getPacketSender().sendString(358, "Click here to continue");
        player.getPacketSender().sendChatboxInterface(356);
    }

    protected void sendOption(String @NotNull ... lines){
        int firstChildId = OPTION_DIALOGUE_ID[lines.length - 2];
        player.getPacketSender().sendString(firstChildId - 1, lines[0]);
        for (int i = 0; i < lines.length - 1; i++) {
            if (lines[i].equalsIgnoreCase("[petupgrading]") &&
                    player.getInteractingItem() != null && PetUpgrading.forId(player.getInteractingItem().getId()) != null){
                player.getPacketSender().sendString(firstChildId + i, PetUpgrading.forId(player.getInteractingItem().getId()).getMessage());
            } else {
                player.getPacketSender().sendString(firstChildId + i, lines[i + 1]);
            }
            options.put(firstChildId+i, lines[i+1]);
        }
        player.getPacketSender().sendChatboxInterface(firstChildId - 2);
    }
}
