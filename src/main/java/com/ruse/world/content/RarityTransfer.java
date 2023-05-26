package com.ruse.world.content;

import com.ruse.model.Item;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.world.content.dialogue.Dialogue;
import com.ruse.world.content.dialogue.DialogueExpression;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.content.dialogue.DialogueType;
import com.ruse.world.entity.impl.player.Player;

public class RarityTransfer {
    private final Player p;
    private Item transferedItem;
    private int transferedItemSlot = -1;
    private Item itemToTransferTo;
    private int itemToTransferToSlot = -1;

    public RarityTransfer(Player p) {
        this.p = p;
    }

    public void transfer() {
        if(transferedItem != null && itemToTransferTo != null && transferedItemSlot != -1 && itemToTransferToSlot != -1) {
            boolean hasTransferedItem = p.getInventory().get(transferedItemSlot).equals(transferedItem);
            boolean hasItemToTransferTo = p.getInventory().get(itemToTransferToSlot).equals(itemToTransferTo);
            Item copy = itemToTransferTo.copy();
            Item copy2 = transferedItem.copy();

            if (!p.getInventory().contains(8788)) {
                p.getPacketSender().sendInterfaceRemoval();
                p.sendMessage("<img=3245>@red@ @blu@[Rarity Transfer] @red@You need a @blu@Transfer Crystal @red@to transfer rarity.");
                return;
            } else if (hasTransferedItem && hasItemToTransferTo) {
                if (copy.getId() == 995
                        || copy.getId() == 10835
                        || copy.getId() == 23203
                        || copy.getId() == 6
                        || copy.getId() == 8
                        || copy.getId() == 10
                        || copy.getId() == 2023
                        || copy.getId() == 14488
                        || copy.getId() == 19984
                        || copy.getId() == 19985
                        || copy.getId() == 19986
                        || copy.getId() == 20400
                        || copy.getId() == 19989
                        || copy.getId() == 19988
                        || copy.getId() == 19992
                        || copy.getId() == 19991
                        || copy.getId() == 14487
                        || copy.getId() == 20086
                        || copy.getId() == 20087
                        || copy.getId() == 20088
                        || copy.getId() == 20089
                        || copy.getId() == 20091
                        || copy.getId() == 20093
                        || copy.getId() == 20092
                        || copy.getId() == 18011
                        || copy.getId() == 17999
                        || copy.getId() == 18001
                        || copy.getId() == 18003
                        || copy.getId() == 18005
                        || copy.getId() == 18009
                        || copy.getId() == 14490
                        || copy.getId() == 14492
                        || copy.getId() == 23126
                        || copy.getId() == 23123
                        || copy.getId() == 23120
                        || copy.getId() == 23165
                        || copy.getId() == 23166
                        || copy.getId() == 23167
                        || copy.getId() == 23168
                        || copy.getId() == 23169
                        || copy.getId() == 23170
                        || copy.getId() == 23057
                        || copy.getId() == 15003
                        || copy.getId() == 15002
                        || copy.getId() == 3578
                        || copy.getId() == 23058
                        || copy.getId() == 15004
                        || copy.getId() == 20489
                        || copy.getId() == 10946
                        || copy.getId() == 4278
                        || copy.getId() == 10947
                        || copy.getId() == 13019
                        || copy.getId() == 4442
                        || copy.getId() == 20491
                        || copy.getId() == 23059
                        || copy.getId() == 22110
                        || copy.getId() == 19481
                        || copy.getId() == 19482
                        || copy.getId() == 23081
                        || copy.getId() == 19483
                        || copy.getId() == 19484
                        || copy.getId() == 19485
                        || copy.getId() == 19486
                        || copy.getId() == 19487
                        || copy.getId() == 19488
                        || copy.getId() == 19489
                        || copy.getId() == 19490
                        || copy.getId() == 19491
                        || copy.getId() == 19492
                        || copy.getId() == 19493
                        || copy.getId() == 19494
                        || copy.getId() == 19495
                        || copy.getId() == 20582
                        || copy.getId() == 20583
                        || copy.getId() == 20584
                        || copy.getId() == 20585
                        || copy.getId() == 20586
                        || copy.getId() == 20587
                        || copy.getId() == 20588
                        || copy.getId() == 20589
                        || copy.getId() == 20590
                        || copy.getId() == 20602
                        || copy.getId() == 20603
                        || copy.getId() == 20604
                        || copy.getId() == 20605
                        || copy.getId() == 13774
                        || copy.getId() == 4073
                        || copy.getId() == 13775
                        || copy.getId() == 15357
                        || copy.getId() == 15355
                        || copy.getId() == 21816
                        || copy.getId() == 21815
                        || copy.getId() == 21814
                        || copy.getId() == 2736
                        || copy.getId() == 23020
                        || copy.getId() == 23060
                        || copy.getId() == 8788
                        || copy.getId() == 2734
                        || copy.getId() == 11137
                        || copy.getId() == 23174
                        || copy.getId() == 9084
                        || copy.getId() == 19624
                        || copy.getId() == 23002
                        || copy.getId() == 6640
                ) {
                    p.getPacketSender().sendInterfaceRemoval();
                    p.sendMessage("<img=3245>@red@ @blu@[Rarity Transfer] You cannot transfer rarities to this item.");
                    return;
                }
                if (copy2.getEffect().equals(ItemEffect.NOTHING)) {
                    p.getPacketSender().sendInterfaceRemoval();
                    p.sendMessage("<img=3245>@red@ @blu@[Rarity Transfer] This item does not have a rarity assigned.");
                    return;
                }
                p.getInventory().delete(8788, 1);
                p.getInventory().delete(transferedItem)
                        .delete(itemToTransferTo)
                        .add(new Item(copy.getId(), 1, copy2.getEffect(), copy2.getBonus()));
            }
        }

        transferedItem = null;
        transferedItemSlot = -1;
        itemToTransferTo = null;
        itemToTransferToSlot = -1;

        p.getPacketSender().sendInterfaceRemoval();
    }

    public void setItemsAndStartDialogue(Item transferedItem, int transferedItemSlot, Item itemToTransferTo, int itemToTransferToSlot) {
        this.transferedItem = transferedItem;
        this.transferedItemSlot = transferedItemSlot;
        this.itemToTransferTo = itemToTransferTo;
        this.itemToTransferToSlot = itemToTransferToSlot;
        DialogueManager.start(p, new TransferDialogue(p));
    }

    public static class TransferDialogue extends Dialogue {
        private final Player player;

        public TransferDialogue(Player player) {
            this.player = player;
        }

        @Override
        public DialogueType type() {
            return DialogueType.OPTION;
        }

        @Override
        public DialogueExpression animation() {
            return null;
        }

        @Override
        public String[] dialogue() {
            return new String[] {"Transfer rarity and effects from @red@" + player.getRarityTransfer().transferedItem.getDefinition().getName() + "@bla@ to @red@" + player.getRarityTransfer().itemToTransferTo.getDefinition().getName(), "No thanks, i don't want to loose my @red@"+ player.getRarityTransfer().transferedItem.getDefinition().getName() +".."};
        }

        @Override
        public void specialAction() {
            player.setDialogueActionId(901);
        }
    }
}
