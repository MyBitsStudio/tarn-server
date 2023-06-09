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
                if(ItemEffect.hasNoEffect(copy.getId()) || ItemEffect.hasNoEffect(copy2.getId())){
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
