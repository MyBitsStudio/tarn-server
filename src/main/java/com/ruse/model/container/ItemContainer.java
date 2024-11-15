package com.ruse.model.container;

import com.google.common.collect.Iterables;
import com.ruse.model.GroundItem;
import com.ruse.model.Item;
import com.ruse.model.container.impl.*;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.GroundItemManager;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.misc.ItemIdentifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a container which contains items.
 *
 * @author relex lawl
 */

public abstract class ItemContainer {

    /**
     * Player who owns the item container.
     */
    private Player player;
    /**
     * The items located in the container.
     */
    private Item[] items = new Item[capacity()];

    /**
     * ItemContainer constructor to create a new instance and to define the player.
     *
     * @param player Player who owns the item container.
     */
    public ItemContainer(Player player) {
        this.player = player;
        for (int i = 0; i < capacity(); i++) {
            items[i] = new Item(-1, 0);
        }
    }

    public ItemContainer(Player player, boolean ignored){
        this.player = player;
        for (int i = 0; i < capacity(); i++) {
            items[i] = new Item(-1, 0);
        }
    }

    /**
     * ItemContainer constructor to create a new instance and to define the player.
     *
     * @param player Player who owns the item container.
     */
    public ItemContainer(Player player, int capacity) {
        this.player = player;
        items = new Item[capacity];
        for (int i = 0; i < capacity; i++) {
            items[i] = new Item(-1, 0);
        }
    }

    /**
     * The amount of items the container can hold, such as 28 for inventory.
     */
    public abstract int capacity();

    /**
     * The container's type enum, see enum for sendInformation.
     */
    public abstract StackType stackType();

    /**
     * The refresh method to send the container's interface on addition or deletion
     * of an item.
     */
    public abstract ItemContainer refreshItems();

    /**
     * The full method which contains the content a player will receive upon
     * container being full, such as a message when inventory is full.
     */
    public abstract ItemContainer full();

    /**
     * Gets the owner's player instance.
     *
     * @return player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player viewing the container, used for containers such as Shops.
     */
    public ItemContainer setPlayer(Player player) {
        this.player = player;
        return this;
    }

    /**
     * Gets the items in the container.
     *
     * @return items.
     */
    public Item[] getItems() {
        if (this instanceof GroupIronmanBank) {
            if (items.length != capacity()) {
                Item[] newItems = new Item[capacity()];

                for (int i = 0; i < capacity(); i++) {
                    newItems[i] = new Item(-1, 0);
                }
                System.arraycopy(items, 0, newItems, 0, 100);
                setItems(newItems);
            }
        }
        return items;
    }

    /**
     * Sets all the items in the container.
     *
     * @param items The item array to which set the container to hold.
     */
    public ItemContainer setItems(Item[] items) {

        this.items = items;
        return this;
    }

    public Item[] getCopiedItems() {
        Item[] it = new Item[items.length];
        for (int i = 0; i < it.length; i++) {
            it[i] = items[i].copy();
        }
        return it;
    }

    /**
     * Gets the valid items in the container,
     *
     * @return items in a list format.
     */
    public List<Item> getValidItems() {
        List<Item> items = new ArrayList<>();
        for (Item item : this.items) {
            if (item != null && item.getId() > 0) {
                if (item.getAmount() > 0 || (this instanceof Bank) && (item.getAmount() == 0 || item.getAmount() == -1)) {
                    items.add(item);
                }
            }

        }
        return items;
    }

    public Item[] getValidItemsArray() {
        List<Item> items = getValidItems();
        Item[] array = new Item[items.size()];
        for (int i = 0; i < items.size(); i++) {
            array[i] = items.get(i);
        }
        return array;
    }

    /**
     * Sets the item in said slot.
     *
     * @param slot Slot to set item for.
     * @param item Item that will occupy the slot.
     */
    public ItemContainer setItem(int slot, Item item) {
        items[slot] = item;
        return this;
    }

    /**
     * Checks if the slot contains an item.
     *
     * @param slot The container slot to check.
     * @return items[slot] != null.
     */
    public boolean isSlotOccupied(int slot) {
        return items[slot] != null && items[slot].getId() > 0 && items[slot].getAmount() > 0;
    }

    /**
     * Swaps two item slots.
     *
     * @param fromSlot From slot.
     * @param toSlot   To slot.
     */
    public ItemContainer swap(int fromSlot, int toSlot) {
        Item temporaryItem = getItems()[fromSlot];
        if (temporaryItem == null || temporaryItem.getId() <= 0)
            return this;
        setItem(fromSlot, getItems()[toSlot]);
        setItem(toSlot, temporaryItem);
        return this;
    }


    public ItemContainer shiftSwap(int fromSlot, int toSlot) {
        Item temporaryItem = getItems()[fromSlot];
        if (temporaryItem == null || temporaryItem.getId() <= 0)
            return this;
        int tempFrom = fromSlot;

        for (int tempTo = toSlot; tempFrom != tempTo; ) {
            if (tempFrom > tempTo) {
                swap(tempFrom, tempFrom - 1);
                tempFrom--;
            } else if (tempFrom < tempTo) {
                swap(tempFrom, tempFrom + 1);
                tempFrom++;
            }
        }

        return this;
    }

    /**
     * Gets the amount of free slots the container has.
     *
     * @return Total amount of free slots in container.
     */
    public int getFreeSlots() {
        int space = 0;
        for (Item item : items) {
            if (item.getId() == -1) {
                space++;
            }
        }
        return space;
    }

    /**
     * Checks if the container is out of available slots.
     *
     * @return No free slot available.
     */
    public boolean isFull() {
        return getEmptySlot() == -1;
    }

    /**
     * Checks if container contains a certain item id.
     *
     * @param id The item id to check for in container.
     * @return Container contains item with the specified id.
     */
    public boolean contains(int id) {
        for (Item items : this.items) {
            if (items != null && items.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(int id, int amount) {
        for (Item items : this.items) {
            if (items != null && items.getId() == id && items.getAmount() >= amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this container has a set of certain items.
     *
     * @param item the item to check in this container for.
     * @return true if this container has the item.
     */
    public boolean contains(Item[] item) {
        if (item.length == 0) {
            return false;
        }

        for (Item nextItem : item) {
            if (nextItem == null) {
                continue;
            }

            if (!contains(nextItem.getId(), nextItem.getAmount())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the next empty slot for an item to equip.
     *
     * @return The next empty slot index.
     */
    public int getEmptySlot() {
        for (int i = 0; i < capacity(); i++) {
            if (items[i].getId() <= 0 || !(this instanceof Bank) && items[i].getAmount() <= 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the first slot found for an item with said id.
     *
     * @param id The id to loop through items to find.
     * @return The slot index the item is located in.
     */
    public int getSlot(int id) {
        for (int i = 0; i < capacity(); i++) {
            if (items[i].getId() > 0 && items[i].getId() == id) {
                if (items[i].getAmount() > 0 || (this instanceof Bank) && items[i].getAmount() == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int slotOf(Item item) {
        return getSlot(item);
    }

    public int getSlot(Item item) {
        for (int i = 0; i < capacity(); i++) {
            if (items[i].getId() > 0 && items[i].getId() == item.getId()) {
                if (items[i].getAmount() > 0 || ((this instanceof Bank) && items[i].getAmount() <= 1)) {
                    if(Objects.equals(items[i].getPerk(), item.getPerk()) && Objects.equals(items[i].getBonus(), item.getBonus()))
                        return i;
                }
            }
        }
        return -1;
    }

    /**
     * Gets the total amount of items in the container with the specified id.
     *
     * @param id The id of the item to search for.
     * @return The total amount of items in the container with said id.
     */
    public int getAmount(int id) {
        int totalAmount = 0;
        for (Item item : items) {
            if(Objects.equals(item.getUid(), "-1"))
                continue;
            if (item.getId() == id) {
                totalAmount += item.getAmount();
            }
        }
        return totalAmount;
    }

    public int getAmount(Item it) {
        int totalAmount = 0;
        for (Item item : items) {
            if(Objects.equals(item.getUid(), "-1"))
                continue;
            if (item.getId() == it.getId() && Objects.equals(item.getPerk(), it.getPerk())
                    && Objects.equals(item.getBonus(), it.getBonus())){
                totalAmount += item.getAmount();
            }
        }
        return totalAmount;
    }

    /**
     * Gets the total amount of items in the container in the specified slot
     *
     * @param slot The slot of the item to search for.
     * @return The total amount of items in the container with said slot.
     */
    public int getAmountForSlot(int slot) {
        return items[slot].getAmount();
    }

    /**
     * Resets items in the container.
     *
     * @return The ItemContainer instance.
     */
    public ItemContainer resetItems() {
        for (int i = 0; i < capacity(); i++) {
            items[i] = new Item(-1, 0);
        }
        return this;
    }

    /**
     * Gets an item by their slot index.
     *
     * @param slot Slot to check for item.
     * @return Item in said slot.
     */
    public Item forSlot(int slot) {
        if((slot >= 0) && (slot < items.length)) {
            return items[slot];
        }
        return null;
    }

    /**
     * Switches an item from one item container to another.
     *
     * @param to   The item container to put item on.
     * @param item The item to put from one container to another.
     * @param slot The slot of the item to switch from one container to another.
     * @param sort This flag checks whether or not to sort items, such as for bank.
     * @return The ItemContainer instance.
     */
    public ItemContainer switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
        if (getItems()[slot].getId() != item.getId()) {
            return this;
        }
        if (to.getFreeSlots() <= 0 && !(to.contains(item.getId()) && item.getDefinition().isStackable())) {
            to.full();
            return this;
        }
        to.add(item, refresh);
        if (sort && getAmount(item) <= 0)
            sortItems();
        if (refresh) {
            refreshItems();
            to.refreshItems();
        }
        return this;
    }

    /*
     * Checks if container is full
     */
    public boolean full(int itemId) {
        return this.getFreeSlots() <= 0 && !(this.contains(itemId) && new Item(itemId).getDefinition().isStackable());
    }

    /**
     * Switches an item from one item container to another. THIS METHOD IS MAINLY
     * USED FOR SELLING ITEMS TO SHOPS
     *
     * @param from   The item container to get item
     * @param to     The item container to put item on.
     * @param itemId The item's id to put from one container to another.
     * @param amount The amount of the item to put from one container to another.
     * @return The ItemContainer instance.
     */

    public ItemContainer switchItem(ItemContainer from, ItemContainer to, int itemId, int amount) {
        if (full(itemId)) {
            to.full();
            return this;
        }
        Item item = getPlayer().getInventory().getItems()[from.getSlot(itemId)];
        if (item == null || item.getId() <= 0 || item.getAmount() <= 0)
            return this;
        if (item.getDefinition().isStackable() && amount > 1) { // Item is stackable
            item.setAmount(from.getAmount(item));
        }
        from.delete(item);
        to.add(item);
        return this;
    }

    public void addItems(Item[] items, boolean refresh) {
        if (items == null)
            return;
        for (Item item : items) {
            if (item.getId() > 0 && (item.getAmount() > 0 || (item.getAmount() == 0 && (this instanceof Bank)))) {
                this.add(item, refresh);
            }
        }
    }

    /**
     * Sorts this item container's array of items to leave no empty spaces.
     *
     * @return The ItemContainer instance.
     */
    public ItemContainer sortItems() {
        for (int k = 0; k < capacity(); k++) {
            if (getItems()[k] == null)
                continue;
            for (int i = 0; i < (capacity() - 1); i++) {
                if (getItems()[i] == null || getItems()[i].getId() <= 0) {
                    swap((i + 1), i);
                }
            }
        }
        return this;
    }

    /**
     * Adds an item to the item container.
     *
     * @param item The item to add.
     * @return The ItemContainer instance.
     */
    public ItemContainer add(Item item) {
        return add(item, true);
    }

    /**
     * Adds an item to the item container.
     *
     * @param id     The id of the item.
     * @param amount The amount of the item.
     * @return The ItemContainer instance.
     */
    public ItemContainer add(int id, int amount) {
        return add(new Item(id, amount));
    }

    public ItemContainer addDropIfFull(int id, int amount) {
        boolean stackable = false;
        ItemDefinition def = ItemDefinition.forId(id);
        if (def != null) {
            stackable = def.isStackable();
        }
        if (!isFull() || (isFull() && contains(id) && stackable)) {
            return add(new Item(id, amount));
        } else {
            GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(id, amount),
                    player.getPosition(), player.getUsername(), false, 120, false, 120));
            return null;
        }
    }

    public static int[] unstackables = new int[] {
        23150, 23151, 23152, 23153, 23154, 23155, 23156, 23157, 23158, 23159, 23160, 25000, 25001, 25002,
            25003, 25004, 25005, 25006
    };

    /**
     * Adds an item to the item container.
     *
     * @param item    The item to add.
     * @param refresh If <code>true</code> the item container interface will be
     *                refreshed.
     * @return The ItemContainer instance.
     */
    public ItemContainer add(Item item, boolean refresh) {
        if (item == null || item.getId() <= 0 || (item.getAmount() <= 0 && !(this instanceof Bank))) {
            //System.out.println("item is null or id is less than 0 or amount is less than 0");
            return this;
        }

        if (item.getId() == ItemDefinition.COIN_ID && this instanceof Inventory) {
            if (getAmount(item.getId()) + item.getAmount() >= Integer.MAX_VALUE
                    || getAmount(item.getId()) + item.getAmount() <= -1) {
                if (item.getAmount() >= 1_000) {
                    Item newItem = new Item(ItemDefinition.TOKEN_ID, item.getAmount() / 1_000);
                    if (isFull()) {
                        getPlayer().depositItemBank(newItem);
                    } else {
                        add(newItem);
                    }
                    getPlayer().getPacketSender().sendMessage("The coins that you could not hold in your inventory have been converted into tokens.");
                    return this;
                } else {
                    int amount = getAmount(item.getId()) / 1_000;
                    Item newItem = new Item(ItemDefinition.TOKEN_ID, amount);
                    delete(item.getId(), amount * 1_000);
                    if (isFull()) {
                        getPlayer().depositItemBank(newItem);
                    } else {
                        add(newItem);
                    }
                    getPlayer().getPacketSender().sendMessage("The coins that you had have been converted into tokens.");
                }
            }
        }
        if (ItemDefinition.forId(item.getId()).isStackable() || stackType() == StackType.STACKS
        && Arrays.stream(unstackables).noneMatch(i -> i == item.getId())) {

            int slot = getSlot(item);
            if (slot == -1)
                slot = getEmptySlot();
            if (slot == -1) {
                if (getPlayer() != null) {
                    if (!getPlayer().getRank().isAdmin()) {
                        GroundItemManager.spawnGroundItem(player, new GroundItem(item, player.getPosition().copy(),
                                player.getUsername(), player.getHostAddress(), false, 120,
                                player.getPosition().getZ() >= 0 && player.getPosition().getZ() < 4,
                                60));
                        getPlayer().getPacketSender().sendMessage("The item which you couldn't hold has been placed beneath you.");
                        if (refresh)
                            refreshItems();
                    }
                }
                return this;
            }
            //System.out.println("here");
            items[slot].setId(item.getId());
            items[slot].setAmount(items[slot].getAmount() + item.getAmount());
            items[slot].setUid(item.getUid());
            items[slot].setPerk(item.getPerk());
            items[slot].setBonus(item.getBonus());
        } else {
            int amount = item.getAmount();
            while (amount > 0) {
                int slot = getEmptySlot();
                if (slot == -1) {
                    if (!getPlayer().getRank().isAdmin()) {
                        GroundItemManager.spawnGroundItem(player, new GroundItem(Item.getNoted(item.getId(), amount),
                                player.getPosition().copy(), player.getUsername(), false, 120,
                                player.getPosition().getZ() >= 0 && player.getPosition().getZ() < 4,
                                60));
                        getPlayer().getPacketSender().sendMessage("The item(s) which you couldn't hold have been placed beneath you.");
                        if (refresh)
                            refreshItems();
                        return this;
                    }
                } else {
                    //System.out.println("adding "+item.getId()+" to slot "+slot+" with amount "+amount+" and uid "+item.getUid()+" and perk "+item.getPerk()+" and bonus "+item.getBonus());
                    items[slot].setId(item.getId());
                    items[slot].setAmount(1);
                    items[slot].setUid(item.getUid());
                    items[slot].setPerk(item.getPerk());
                    items[slot].setBonus(item.getBonus());
                }
                amount--;
            }
        }
        if (refresh)
            refreshItems();
        return this;
    }

    public boolean isBank() {
        return this instanceof Bank || this instanceof GroupIronmanBank;
    }

    /**
     * Deletes an item from the item container.
     *
     * @param item The item to delete.
     * @return The ItemContainer instance.
     */
    public ItemContainer delete(Item item) {
        return delete(item, getSlot(item), true, null);
    }

    public ItemContainer delete(Item item, ItemContainer to) {
        return delete(item, getSlot(item), true, to);
    }

    /**
     * Deletes an item from the item container.
     *
     * @param item The item to delete.
     * @param slot The slot of the item (used to delete the item from said slot, not
     *             the first one found).
     * @return The ItemContainer instance.
     */
    public ItemContainer delete(Item item, int slot) {
        return delete(item, slot, true);
    }

    /**
     * Deletes an item from the item container.
     *
     * @param id     The id of the item to delete.
     * @param amount The amount of the item to delete.
     * @return The ItemContainer instance.
     */
    public ItemContainer delete(int id, int amount) {
        return delete(id, amount, true);
    }

    /**
     * Deletes an item from the item container.
     *
     * @param id      The id of the item to delete.
     * @param amount  The amount of the item to delete.
     * @param refresh If <code>true</code> the item container interface will
     *                refresh.
     * @return The ItemContainer instance.
     */
    public ItemContainer delete(int id, int amount, boolean refresh) {
        return delete(new Item(id, amount), getSlot(id), refresh);
    }

    /**
     * Deletes an item from the item container.
     *
     * @param item    The item to delete.
     * @param slot    The slot of the item to delete.
     * @param refresh If <code>true</code> the item container interface will
     *                refresh.
     * @return The ItemContainer instance.
     */
    public ItemContainer delete(Item item, int slot, boolean refresh) {
        return delete(item, slot, refresh, null);
    }


    /**
     * Deletes an item from the item container.
     *
     * @param item        The item to delete.
     * @param slot        The slot of the item to delete.
     * @param refresh     If <code>true</code> the item container interface will
     *                    refresh.
     * @param toContainer To check if other container has enough space to continue
     *                    deleting said amount from this container.
     * @return The ItemContainer instance.
     */
    public ItemContainer delete(Item item, int slot, boolean refresh, ItemContainer toContainer) {
        if (item == null || slot < 0) {
            //System.out.println("nulllled");
            return this;
        }
        boolean leavePlaceHolder = (toContainer instanceof Inventory && this instanceof Bank && getPlayer().isPlaceholders());
        if (item.getAmount() > getAmount(item)) {
            item.setAmount(getAmount(item));
        }
        if (item.getDefinition().isStackable() || stackType() == StackType.STACKS && Arrays.stream(unstackables).noneMatch(i -> i == item.getId())) {
            if (toContainer != null && !item.getDefinition().isStackable() && item.getAmount() > toContainer.getFreeSlots() && !(this instanceof Bank)  && !(this instanceof GroupIronmanBank))
                item.setAmount(toContainer.getFreeSlots());
            items[slot].setAmount(items[slot].getAmount() - item.getAmount());
            //System.out.println("deleting 5 from slot "+slot+" with amount "+item.getAmount()+" and uid "+item.getUid()+" and perk "+item.getPerk()+" and bonus "+item.getBonus());
            if (items[slot].getAmount() < 1) {
                items[slot].setAmount(0);
                if (!leavePlaceHolder) {
                    items[slot].setId(-1);
                    items[slot].setUid("stale");
                    items[slot].setPerk("none");
                    items[slot].setBonus("none");
                } else {
                    items[slot].setUid("-1");
                }
            }
        } else {
            int amount = item.getAmount();
            while (amount > 0) {
                if (slot == -1 || (toContainer != null && toContainer.isFull()))
                    break;
                //System.out.println("deleting by slot "+slot+" with amount "+item.getAmount()+" and uid "+item.getUid()+" and perk "+item.getPerk()+" and bonus "+item.getBonus());
                if (!leavePlaceHolder) {
                    items[slot].setId(-1);
                    items[slot].setUid("stale");

                } else {
                    items[slot].setUid("-1");
                }
                items[slot].setPerk("none");
                items[slot].setBonus("none");
                items[slot].setAmount(0);
                slot = getSlot(item);
                amount--;
            }
        }
        if (refresh)
            refreshItems();
        return this;
    }

    /**
     * Gets an item id by its index.
     *
     * @return the item id on this index.
     */
    public Item getById(int id) {
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public Item getById(int id, int amount) {
        for (Item item : items) {
            if (item == null) {
                continue;
            }
            if (item.getId() == id) {
                if(item.getAmount() > 1){
                    if(item.getAmount() <= amount)
                        return item;
                } else
                 return item;

            }
        }
        return null;
    }


    public boolean containsAll(int... ids) {
        return Arrays.stream(ids).allMatch(this::contains);
    }

    public boolean containsAll(Item... items) {
        return Arrays.stream(items).filter(Objects::nonNull).allMatch(item -> getAmount(item.getId()) >= item.getAmount());
    }

    public boolean containsAny(int... ids) {
        return Arrays.stream(ids).anyMatch(this::contains);
    }

    public void set(int slot, Item item) {
        items[slot] = item;
    }

    public Item get(int slot) {
        return items[slot];
    }

    public boolean isSlotFree(int slot) {
        return items[slot] == null || items[slot].getId() == -1;
    }

    public Item[] toSafeArray() {
        return Iterables.toArray(Arrays.stream(items).filter(Objects::nonNull).collect(Collectors.toList()),
                Item.class);
    }

    public void moveItems(ItemContainer to, boolean refreshOrig, boolean refreshTo) {

        for (Item it : getValidItems()) {
            if (to.getFreeSlots() <= 0 && !(to.contains(it.getId()) && it.getDefinition().isStackable())) {
                break;
            }
            to.add(it, false);
            delete(it.getId(), it.getAmount(), false);
        }

        if (refreshOrig) {
            refreshItems();
        }
        if (refreshTo) {
            to.refreshItems();
        }
    }

    /**
     * Adds a set of items into the inventory.
     *
     * @param item the set of items to add.
     */
    public void addItemSet(Item[] item) {
        for (Item addItem : item) {
            if (addItem == null) {
                continue;
            }
            add(addItem);
        }
    }

    /**
     * Deletes a set of items from the inventory.
     *
     * @param item the set of items to delete.
     */
    public void deleteItemSet(Item[] item) {
        for (Item deleteItem : item) {
            if (deleteItem == null) {
                continue;
            }

            delete(deleteItem);
        }
    }

    public void clear() {
        for (int i = 0; i < items.length; i++) {
            items[i] = new Item(-1, 0);
        }
        refreshItems();
    }

    public boolean canHold(Item item) {
        int inventorySpacesNeeded = item.getAmount();
        if (item.getDefinition().isStackable() || item.getDefinition().isNoted())
            inventorySpacesNeeded = 1;
        if (contains(item.getId()))
            inventorySpacesNeeded = 0;

        if (getFreeSlots() >= inventorySpacesNeeded)
            return true;

        return false;
    }
}
