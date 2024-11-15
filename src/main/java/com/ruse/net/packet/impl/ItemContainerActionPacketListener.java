package com.ruse.net.packet.impl;

import com.ruse.model.Item;
import com.ruse.model.container.impl.*;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.input.EnterAmount;
import com.ruse.model.input.impl.*;
import com.ruse.net.packet.Packet;
import com.ruse.net.packet.PacketListener;
import com.ruse.util.Misc;
import com.ruse.world.content.*;
import com.ruse.world.packages.tradingpost.TradingPost;
import com.ruse.world.packages.forge.shop.ForgeShopHandler;
import com.ruse.world.content.grandexchange.GrandExchange;
import com.ruse.world.content.grandexchange.GrandExchangeOffer;
import com.ruse.world.content.minigames.impl.Dueling;
import com.ruse.world.content.skill.impl.crafting.Jewelry;
import com.ruse.world.content.skill.impl.smithing.EquipmentMaking;
import com.ruse.world.content.skill.impl.smithing.SmithingData;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.shops.ShopHandler;

import java.util.Optional;

public class ItemContainerActionPacketListener implements PacketListener {

	/**
	 * Manages an item's first action.
	 *
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void firstAction(Player player, Packet packet) {
		int interfaceId = packet.readShortA();
		int slot = packet.readShortA();
		int id = packet.readShortA();
		Item item = new Item(id, 1);
		//System.out.println(id + ", " + slot + ", " + interfaceId);

		if (player.getRank().isDeveloper()) {
			player.getPacketSender().sendMessage("firstAction itemContainer. IF: " + interfaceId + " slot: " + slot + ", id: " + id);
		}

		if(interfaceId == 31444)
			if(player.getEquipment().handleContainer(slot, 1, id))
				return;

		if(ShopHandler.handleShop(player, interfaceId, 1, slot, id))
			return;

		if(player.getStarterShop().handleShop(interfaceId, 1, slot, id))
			return;


        switch (interfaceId) {
            case TradingPost.ITEM_CONTAINER_ID ->
                    player.getPacketSender().sendMessage(TradingPost.getAverageValue(item));
            case -15971 -> ForgeShopHandler.purchase(player, id, 1);
            case -15995 -> player.getForge().addItem(player.getInventory().forSlot(slot));
            case -15997 -> player.getForge().removeItem(slot);
            case 19420 -> player.loadUpgradeInterface().setData(player.loadUpgradeInterface().getCategory()[slot]);
            case 31510 -> player.getEventBossManager().removeNpcDropReward(id, 1);
            case 2900 -> player.getEventBossManager().addNpcDropReward(id, 1, slot);
            case -16815 -> player.getUimBank().withdraw(id, 1, slot);
            //case -3327 -> player.getUpgradeInterface().handleItemAction(slot);
            case 32621 -> {
            }
            //player.getPlayerOwnedShopManager().handleBuy(slot, id, -1);
            case -31915 -> {
            }
            //player.getPlayerOwnedShopManager().handleWithdraw(slot, id, -1);
            case -28382 -> player.sendMessage("@red@ POS Adding is disabled. Please withdraw your items.");

            // done.
            case GrandExchange.COLLECT_ITEM_PURCHASE_INTERFACE ->
                    GrandExchange.collectItem(player, id, slot, GrandExchangeOffer.OfferType.BUYING);
            case GrandExchange.COLLECT_ITEM_SALE_INTERFACE ->
                    GrandExchange.collectItem(player, id, slot, GrandExchangeOffer.OfferType.SELLING);
            case Trading.INTERFACE_ID -> {
                if (player.getTrading().inTrade()) {
                    player.getTrading().tradeItem(id, 1, slot);
                } else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
                    player.getDueling().stakeItem(id, 1, slot);
                }
                if (player.getGambling().inGamble()) {
                    player.getGambling().gambleItem(id, 1, slot);
                }
                //player.getUimBank().deposit(id, 1);
                player.getUimBank().deposit(item);
            }
            case -8365 -> {
                if (player.getGambling().inGamble()) {
                    player.getGambling().removeGambledItem(id, 1);
                }
            }
            case Trading.INTERFACE_REMOVAL_ID -> {
                if (player.getTrading().inTrade())
                    player.getTrading().removeTradedItem(id, 1);
            }
            // player.getUimBank().withd
            case Dueling.INTERFACE_REMOVAL_ID -> {
                if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
                    player.getDueling().removeStakedItem(id, 1);
                    return;
                }
            }
//			case Equipment.INVENTORY_INTERFACE_ID:
//				Equipment equipment;
//				if(player.isSecondaryEquipment()) {
//					equipment = player.getSecondaryEquipment();
//				} else {
//					equipment = player.getEquipment();
//				}
//				slot = player.getEquipment().convertSlotFromClient(slot);
//				item = slot < 0 ? null : equipment.getItems()[slot];
//				assert item != null;
//				item = new Item(item.getId(), item.getAmount());
//				if (item.getId() != id)
//					return;
//				if (player.getLocation() == Location.DUEL_ARENA) {
//					if (player.getDueling().selectedDuelRules[DuelRule.LOCK_WEAPON.ordinal()]) {
//						if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT
//								|| item.getDefinition().isTwoHanded()) {
//							player.getPacketSender().sendMessage("Weapons have been locked during this duel!");
//							return;
//						}
//					}
//				}
//				boolean stackItem = item.getDefinition().isStackable() && player.getInventory().getAmount(item.getId()) > 0;
//				int inventorySlot = player.getInventory().getEmptySlot();
//				if (inventorySlot == -1) {
//					player.getInventory().full();
//				} else {
//					Item itemReplacement = new Item(-1, 0);
//					equipment.setItem(slot, itemReplacement);
//					if (!stackItem)
//						player.getInventory().setItem(inventorySlot, item);
//					else
//						player.getInventory().add(item.getId(), item.getAmount());
//					BonusManager.update(player);
//					if (item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
//						WeaponInterfaces.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));
//						WeaponAnimations.update(player);
//						if (player.getAutocastSpell() != null || player.isAutocast()) {
//							Autocasting.resetAutocast(player, true);
//							player.getPacketSender().sendMessage("Autocast spell cleared.");
//						}
//						player.setSpecialActivated(false);
//						CombatSpecial.updateBar(player);
//						if (player.hasStaffOfLightEffect()) {
//							player.setStaffOfLightEffect(-1);
//							player.getPacketSender()
//									.sendMessage("You feel the spirit of the Staff of Light begin to fade away...");
//						}
//					}
//					equipment.refreshItems();
//					player.getInventory().refreshItems();
//					player.getUpdateFlag().flag(Flag.APPEARANCE);
//				}
//				break;
            case Bank.INTERFACE_ID -> {
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    break;
                item = player.getBank(player.getCurrentBankTab()).forSlot(slot).copy().setAmount(1);
                player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), item, slot, true, true);
                player.getBank(player.getCurrentBankTab()).open();
            }
            case GroupIronmanBank.INTERFACE_ID -> {
                if (!player.isBanking() || player.getInterfaceId() != 106000)
                    break;
                item = new Item(item.getId(), item.getAmount(), item.getUid());
                player.getGroupIronmanBank().switchItem(player, player.getInventory(), item, slot, true, true);
                player.getGroupIronmanBank().open(player);
            }
            case Bank.INVENTORY_INTERFACE_ID -> {
                item = player.getInventory().forSlot(slot).copy().setAmount(1);
                if (player.isBanking() && player.getInterfaceId() == 106000 && player.getInventory().contains(item.getId())) {
                    player.getInventory().switchItem(player.getGroupIronmanBank(), item, slot, false, true);
                    player.getGroupIronmanBank().refreshItems(player);
                    return;
                }
                if (!player.isBanking() || !player.getInventory().contains(item.getId()) || player.getInterfaceId() != 5292)
                    return;
                if (player.getBank(player.getCurrentBankTab()).getFreeSlots() <= 0 && !(player.getBank(player.getCurrentBankTab()).contains(item.getId()))) {
                    player.setCurrentBankTab(player.getCurrentBankTab() + 1);
                    if (player.getCurrentBankTab() > 8) {
                        player.setCurrentBankTab(8);
                        if (player.getBank(player.getCurrentBankTab()).isFull()) {
                            player.sendMessage("Your whole bank is full.");
                            return;
                        }
                    }
                    player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
                    player.getPacketSender().sendMessage("Your item has been added to another tab because this tab is full.");
                } else {
                    player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
                    player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
                }
            }
            case 3823 -> {
                Optional<Integer> price = Optional.ofNullable(ShopHandler.junkPrices.get(item.getId()));
                if (price.isPresent()) {
                    player.sendMessage("You sell the item to the store for " + Misc.insertCommasToNumber(price.get()) + " coins.");
                } else {
                    player.sendMessage("You can't sell this item to this shop.");
                }
            }
            case BeastOfBurden.INTERFACE_ID -> {
                if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
                        && player.getSummoning().getBeastOfBurden() != null) {
                    if (item.getDefinition().isStackable()) {
                        player.getPacketSender().sendMessage("You cannot store stackable items.");
                        return;
                    }
                    player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), item, slot, false, true);
                }
            }
            case PriceChecker.INTERFACE_PC_ID -> {
                if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
                    player.getInventory().switchItem(player.getPriceChecker(), item, slot, false, true);
                }
            }
            case 4233 -> Jewelry.jewelryMaking(player, "RING", id, 1);
            case 4239 -> Jewelry.jewelryMaking(player, "NECKLACE", id, 1);
            case 4245 -> Jewelry.jewelryMaking(player, "AMULET", id, 1);
            // smithing interface row 1
            // row 2
            // row 3
            // row 4
            case 1119, 1120, 1121, 1122, 1123 -> { // row 5
                int barsRequired = SmithingData.getBarAmount(item);
                Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
                int x = 1;
                if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
                    x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
                EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
                        new Item(item.getId(), SmithingData.getItemAmount(item)), x);
            }
        }

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), item,
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 1),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's second action.
	 *
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void secondAction(Player player, Packet packet) {
		int interfaceId = packet.readShortA();
		int id = packet.readShortA();
		int slot = packet.readShortA();
		//System.out.println(id + ", " + slot + ", " + interfaceId );
		Item item = new Item(id, 1);
		if (player.getRank().isDeveloper()) {
			player.getPacketSender().sendMessage("secondAction itemContainer. IF: " + interfaceId + " slot: " + slot + ", id: " + id);
		}
		if(interfaceId == 31444)
			if(player.getEquipment().handleContainer(slot, 2, id))
				return;

		if(ShopHandler.handleShop(player, interfaceId, 2, slot, id))
			return;

		if(player.getStarterShop().handleShop(interfaceId, 2, slot, id))
			return;

		switch (interfaceId) {
			case TradingPost.ITEM_CONTAINER_ID -> player.getTradingPost().selectItemToAdd(player.getInventory().get(slot));
			case -15971 -> ForgeShopHandler.purchase(player, id, 5);
			case 31510 -> player.getEventBossManager().removeNpcDropReward(id, 5);
			case 2900 -> player.getEventBossManager().addNpcDropReward(id, 5, slot);
			case -16815 -> player.getUimBank().withdraw(id, player.getUIMBank().getAmount(id), slot);
			case -28382 -> player.sendMessage("@red@ POS Adding is disabled. Please withdraw your items.");

			//player.getPlayerOwnedShopManager().handleStore(slot, id, 5);
			case Trading.INTERFACE_ID -> {
				if (player.getTrading().inTrade()) {
					player.getTrading().tradeItem(id, 5, slot);
				} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
					player.getDueling().stakeItem(id, 5, slot);
				}
				if (player.getGambling().inGamble()) {
					player.getGambling().gambleItem(id, 5, slot);
				}
			}
			case -8365 -> {
				if (player.getGambling().inGamble())
					player.getGambling().removeGambledItem(id, 5);
			}
			case Trading.INTERFACE_REMOVAL_ID -> {
				if (player.getTrading().inTrade())
					player.getTrading().removeTradedItem(id, 5);
			}
			case Dueling.INTERFACE_REMOVAL_ID -> {
				if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
					player.getDueling().removeStakedItem(id, 5);
					return;
				}
			}
			case Bank.INTERFACE_ID -> {
				if (!player.isBanking() || item.getId() != id || player.getInterfaceId() != 5292)
					return;
				item = player.getBank(player.getCurrentBankTab()).forSlot(slot).copy().setAmount(5);
				player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), item, slot, true, true);
				player.getBank(player.getCurrentBankTab()).open();
			}
			case GroupIronmanBank.INTERFACE_ID -> {
				if (!player.isBanking() || player.getInterfaceId() != 106000)
					break;
				item = new Item(item.getId(), 5);
				player.getGroupIronmanBank().switchItem(player, player.getInventory(), item, slot, true, true);
				player.getGroupIronmanBank().open(player);
			}
			case Bank.INVENTORY_INTERFACE_ID -> {
				item = player.getInventory().forSlot(slot).copy().setAmount(5);
				if (player.isBanking() && player.getInterfaceId() == 106000 && player.getInventory().contains(item.getId())) {
					player.getInventory().switchItem(player.getGroupIronmanBank(), item, slot, false, true);
					player.getGroupIronmanBank().refreshItems(player);
					return;
				}
				if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
						|| player.getInterfaceId() != 5292)
					return;
				player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
				player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
			}
//			case Shop.ITEM_CHILD_ID, DonatorShop.ITEM_CHILD_ID_CLICK, PetShop.ITEM_CHILD_ID_CLICK -> {
//				if (player.getShop() == null)
//					return;
//				item = player.getShop().forSlot(slot).copy().setAmount(1).copy();
//				player.getShop().setPlayer(player).switchItem(player.getInventory(), item, slot, false, true);
//			}
			case 3823 -> {
				if (player.isShopping()) {
					ShopHandler.sell(player, slot, 1);
				} else {
					System.out.println("Not shopping.");
				}
			}
			case BeastOfBurden.INTERFACE_ID -> {
				if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
						&& player.getSummoning().getBeastOfBurden() != null) {
					if (item.getDefinition().isStackable()) {
						player.getPacketSender().sendMessage("You cannot store stackable items.");
						return;
					}
					player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), new Item(id, 5), slot, false,
							true);
				}
			}
			case PriceChecker.INTERFACE_PC_ID -> {
				if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
					player.getInventory().switchItem(player.getPriceChecker(), new Item(id, 5), slot, false, true);
				}
			}
			case 4233 -> Jewelry.jewelryMaking(player, "RING", id, 5);
			case 4239 -> Jewelry.jewelryMaking(player, "NECKLACE", id, 5);
			case 4245 -> Jewelry.jewelryMaking(player, "AMULET", id, 5);
			// smithing interface row 1
			// row 2
			// row 3
			// row 4
			case 1119, 1120, 1121, 1122, 1123 -> { // row 5
				int barsRequired = SmithingData.getBarAmount(item);
				Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
				int x = 5;
				if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
					x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
				EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
						new Item(item.getId(), SmithingData.getItemAmount(item)), x);
			}
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 5),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 5),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's third action.
	 *
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void thirdAction(Player player, Packet packet) {
		int interfaceId = packet.readShortA();
		int id = packet.readShortA();
		int slot = packet.readShortA();
		Item item1 = new Item(id, 1);
		//System.out.println("thirdAction itemContainer. IF: " + interfaceId + " slot: " + slot + ", id: " + id);
		if (player.getRank().isDeveloper()) {
			player.getPacketSender()
					.sendMessage("thirdAction itemContainer. IF: " + interfaceId + " slot: " + slot + ", id: " + id);
		}

		if(interfaceId == 31444)
			if(player.getEquipment().handleContainer(slot, 3, id))
				return;

		if(ShopHandler.handleShop(player, interfaceId, 3, slot, id))
			return;

        switch (interfaceId) {
            case TradingPost.ITEM_CONTAINER_ID -> player.getTradingPost().selectItemToAdd(player.getInventory().forSlot(slot).copy().setAmount(5));
            case -15971 -> ForgeShopHandler.purchase(player, id, 10);
            case 31510 -> player.getEventBossManager().removeNpcDropReward(id, 10);
            case 2900 -> player.getEventBossManager().addNpcDropReward(id, 10, slot);
            case 32621 -> {
            }
            //player.getPlayerOwnedShopManager().handleBuy(slot, id, 1);
            case -31915 -> {
            }
            //player.getPlayerOwnedShopManager().handleWithdraw(slot, id, 1);
            case -28382 -> player.sendMessage("@red@ POS Adding is disabled. Please withdraw your items.");

            //player.getPlayerOwnedShopManager().handleStore(slot, id, 10);
            //			case Equipment.INVENTORY_INTERFACE_ID:
//				if (!player.getEquipment().contains(id))
//					return;
//				if(id >= 23069 && id <= 23074) {
//					if(slot == 4) {
//						DialogueManager.start(player, new YesNoDialogue(player, "Would you like to teleport to your slayer task", "for the price of 250k?", 668));
//					}
//				}
//
//				switch (id) {
//					case 2550 -> {
//						int recoilcharges = (ItemDegrading.maxRecoilCharges - player.getRecoilCharges());
//						player.getPacketSender().sendMessage("You have " + recoilcharges + " recoil "
//								+ (recoilcharges == 1 ? "charge" : "charges") + " remaining.");
//					}
//					case 2568 -> {
//						int forgingcharges = (ItemDegrading.maxForgingCharges - player.getForgingCharges());
//						player.getPacketSender().sendMessage("You have " + forgingcharges + " forging "
//								+ (forgingcharges == 1 ? "charge" : "charges") + " remaining.");
//					} // glory start
//					// glory end
//					// cb brace start
//					// cb brace end
//					// duel start
//					// duel end
//					// games start
//					// games end
//					// digsite start
//					case 1712, 1710, 1708, 1706, 11118, 11120, 11122, 11124, 2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566, 3853, 3855, 3857, 3859, 3861, 3863, 3865, 3867, 11194, 11193, 11192, 11191, 11190 -> // digsite start
//						// String jewelName = JewelryTeleports.stripName(id);
//						// JewelryTeleports.handleDialogue(player, id,
//						// JewelryTeleports.jewelIndex(jewelName));
//							player.getPacketSender().sendMessage("You cannot operate this item while wearing it.");
//					case 10362 -> JewelryTeleporting.rub(player, id);
//
//					// case 13738:
//					case 13740, 13742 -> {
//						// case 13744:
//						if (player.isSpiritDebug()) {
//							player.getPacketSender()
//									.sendMessage("You toggle your Spirit Shield to not display specific messages.");
//							player.setSpiritDebug(false);
//						} else if (player.isSpiritDebug() == false) {
//							player.getPacketSender().sendMessage("You toggle your Spirit Shield to display specific messages.");
//							player.setSpiritDebug(true);
//						}
//					}
//					case 4566 -> player.performAnimation(new Animation(451));
//					case 1704 -> player.getPacketSender().sendMessage("Your amulet has run out of charges.");
//					case 11126 -> player.getPacketSender().sendMessage("Your bracelet has run out of charges.");
//					case 9759, 9760 -> {
//						if (player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
//							player.getPacketSender().sendMessage("You cannot configure this right now.");
//							return;
//						}
//						player.getPacketSender().sendInterfaceRemoval();
//						player.setBonecrushEffect(!player.getBonecrushEffect());
//						player.getPacketSender()
//								.sendMessage("<img=5> You have " + (player.getBonecrushEffect() ? "activated" : "disabled")
//										+ " your cape's Bonecrusher effect.");
//					}
//					case 18508, 18509 -> {
//						if (player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
//							player.getPacketSender().sendMessage("You cannot configure this right now.");
//							return;
//						}
//						player.getPacketSender().sendInterfaceRemoval();
//						DialogueManager.start(player, 101);
//						player.setDialogueActionId(60);
//					}
//					case 22052, 14019, 14022, 20081 -> {
//						if (player.getCombatBuilder().isAttacking() || player.getCombatBuilder().isBeingAttacked()) {
//							player.getPacketSender().sendMessage("You cannot configure this right now.");
//							return;
//						}
//						player.getPacketSender().sendInterfaceRemoval();
//						DialogueManager.start(player, 202);
//						player.setDialogueActionId(202);
//					}
//					case 11283 -> {
//						if (player.getDfsCharges() > 0) {
//							if (player.getCombatBuilder().isAttacking()) {
//								CombatFactory.handleDragonFireShield(player, player.getCombatBuilder().getVictim());
//							} else {
//								player.getPacketSender().sendMessage("You can only use this in combat.");
//							}
//						} else {
//							player.getPacketSender().sendMessage("Your shield doesn't have enough power yet. It has "
//									+ player.getDfsCharges() + "/20 dragon-fire charges.");
//						}
//					}
//				}
//				break;
            case Trading.INTERFACE_ID -> {
                if (player.getTrading().inTrade()) {
                    player.getTrading().tradeItem(id, 10, slot);
                } else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
                    player.getDueling().stakeItem(id, 10, slot);
                }
                if (player.getGambling().inGamble()) {
                    player.getGambling().gambleItem(id, 10, slot);
                }
            }
            case -8365 -> {
                if (player.getGambling().inGamble())
                    player.getGambling().removeGambledItem(id, 10);
            }
            case Trading.INTERFACE_REMOVAL_ID -> {
                if (player.getTrading().inTrade())
                    player.getTrading().removeTradedItem(id, 10);
            }
            case Dueling.INTERFACE_REMOVAL_ID -> {
                if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
                    player.getDueling().removeStakedItem(id, 10);
                    return;
                }
            }
            case Bank.INTERFACE_ID -> {
                if (!player.isBanking() || player.getInterfaceId() != 5292)
                    return;
                player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), player.getBank(player.getCurrentBankTab()).forSlot(slot).copy().setAmount(10), slot, true, true);
                player.getBank(player.getCurrentBankTab()).open();
            }
            case GroupIronmanBank.INTERFACE_ID -> {
                if (!player.isBanking() || player.getInterfaceId() != 106000)
                    break;
                player.getGroupIronmanBank().switchItem(player, player.getInventory(), new Item(id, 10), slot, true, true);
                player.getGroupIronmanBank().open(player);
            }
            case Bank.INVENTORY_INTERFACE_ID -> {
                Item item = player.getInventory().forSlot(slot).copy().setAmount(10).copy();
                if (player.isBanking() && player.getInterfaceId() == 106000 && player.getInventory().contains(item.getId())) {
                    player.getInventory().switchItem(player.getGroupIronmanBank(), item, slot, false, true);
                    player.getGroupIronmanBank().refreshItems(player);
                    return;
                }
                if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId())
                        || player.getInterfaceId() != 5292)
                    return;
                player.setCurrentBankTab(Bank.getTabForItem(player, item));
                player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
            }
            case BeastOfBurden.INTERFACE_ID -> {
                if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
                        && player.getSummoning().getBeastOfBurden() != null) {
                    Item storeItem = new Item(id, 10);
                    if (storeItem.getDefinition().isStackable()) {
                        player.getPacketSender().sendMessage("You cannot store stackable items.");
                        return;
                    }
                    player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), storeItem, slot, false, true);
                }
            }
            case PriceChecker.INTERFACE_PC_ID -> {
                if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
                    player.getInventory().switchItem(player.getPriceChecker(), new Item(id, 10), slot, false, true);
                }
            }
            case 4233 -> Jewelry.jewelryMaking(player, "RING", id, 10);
            case 4239 -> Jewelry.jewelryMaking(player, "NECKLACE", id, 10);
            case 4245 -> Jewelry.jewelryMaking(player, "AMULET", id, 10);
            // smithing interface row 1
            // row 2
            // row 3
            // row 4
            case 1119, 1120, 1121, 1122, 1123 -> { // row 5
                int barsRequired = SmithingData.getBarAmount(item1);
                Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
                int x = 10;
                if (x > (player.getInventory().getAmount(bar.getId()) / barsRequired))
                    x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
                EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
                        new Item(item1.getId(), SmithingData.getItemAmount(item1)), x);
            }
        }

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 10),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(), new Item(id, 10),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's fourth action.
	 *
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void fourthAction(Player player, Packet packet) {
		int slot = packet.readShortA();
		int interfaceId = packet.readShortA();
		int id = packet.readShortA();
		if (player.getRank().isDeveloper()) {
			player.getPacketSender()
					.sendMessage("fourthAction itemContainer. IF: " + interfaceId + " slot: " + slot + ", id: " + id);
		}

		if(interfaceId == 31444)
			if(player.getEquipment().handleContainer(slot, 4, id))
				return;

		if(ShopHandler.handleShop(player, interfaceId, 4, slot, id))
			return;

        switch (interfaceId) {
            case TradingPost.ITEM_CONTAINER_ID ->
                    player.getTradingPost().selectItemToAdd(player.getInventory().forSlot(slot).copy().setAmount(10));
            case -15971 -> ForgeShopHandler.purchaseX(player, id);
            case 31510 -> {
                player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove from the NPC Drops?");
                player.setInputHandling(new EnterAmount() {
                    public void handleAmount(Player player, int amount) {
                        player.getEventBossManager().removeNpcDropReward(id, amount);
                    }
                });
            }
            case 2900 -> player.getEventBossManager().addNpcDropReward(id, player.getInventory().getAmount(id), slot);
            // smithing interface row 1
            // row 2
            // row 3
            // row 4
            case 1119, 1120, 1121, 1122, 1123 -> { // row 5
                Item item111 = new Item(id);
                int barsRequired = SmithingData.getBarAmount(item111);
                Item bar = new Item(player.getSelectedSkillingItem(), barsRequired);
                int x = (player.getInventory().getAmount(bar.getId()) / barsRequired);
                EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(), barsRequired),
                        new Item(item111.getId(), SmithingData.getItemAmount(item111)), x);
            }
            case Trading.INTERFACE_ID -> {
                if (player.getTrading().inTrade()) {
                    player.getTrading().tradeItem(id, player.getInventory().getAmount(id), slot);
                } else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
                    player.getDueling().stakeItem(id, player.getInventory().getAmount(id), slot);
                }
                if (player.getGambling().inGamble()) {
                    player.getGambling().gambleItem(id, player.getInventory().getAmount(id), slot);
                }
                Item item2 = new Item(id, player.getInventory().getAmount(id));
                // System.out.println("CALLED HERE for amount: " + player.getInventory().getAmount(id));
                //player.getUimBank().deposit(id, player.getInventory().getAmount(id));
                player.getUimBank().deposit(item2);
            }
            case -8365 -> {
                if (player.getGambling().inGamble())
                    player.getGambling().removeGambledItem(id, player.getInventory().getAmount(id));
            }
            case 32621 -> {
            }
//				player.setInputHandling(new Input() {
//
//					@Override
//					public void handleAmount(Player player, int amount) {
//						player.getPlayerOwnedShopManager().handleBuy(slot, id, amount);
//					}
//
//				});
//				player.getPacketSender().sendEnterAmountPrompt("How many would you like to buy?:");
            case -28382 -> player.sendMessage("@red@ POS Adding is disabled.");

            //player.getPlayerOwnedShopManager().handleStore(slot, id, Integer.MAX_VALUE);
            case -31915 -> {
            }
//				player.setInputHandling(new Input() {
//
//					@Override
//					public void handleAmount(Player player, int amount) {
//						player.getPlayerOwnedShopManager().handleWithdraw(slot, id, amount);
//					}
//
//				});
//				player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?:");
            case Trading.INTERFACE_REMOVAL_ID -> {
                if (player.getTrading().inTrade()) {
                    for (Item item : player.getTrading().offeredItems) {
                        if (item != null && item.getId() == id) {
                            player.getTrading().removeTradedItem(id, item.getAmount());
                            if (ItemDefinition.forId(id) != null && ItemDefinition.forId(id).isStackable())
                                break;
                        }
                    }
                }
            }
            case Dueling.INTERFACE_REMOVAL_ID -> {
                if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
                    for (Item item : player.getDueling().stakedItems) {
                        if (item != null && item.getId() == id) {
                            player.getDueling().removeStakedItem(id, item.getAmount());
                            if (ItemDefinition.forId(id) != null && ItemDefinition.forId(id).isStackable())
                                break;
                        }
                    }
                }
            }
            case Bank.INTERFACE_ID -> {
                if (!player.isBanking() || player.getBank(Bank.getTabForItem(player, id)).getAmount(id) <= 0 || player.getInterfaceId() != 5292)
                    return;
                Item items = player.getBank(Bank.getTabForItem(player, id)).forSlot(slot).copy();
                player.getBank(player.getCurrentBankTab()).switchItem(player.getInventory(), items, slot, true, true);
                player.getBank(player.getCurrentBankTab()).open();
            }
            case GroupIronmanBank.INTERFACE_ID -> {
                if (!player.isBanking() || player.getInterfaceId() != 106000)
                    break;
                player.getGroupIronmanBank().switchItem(player, player.getInventory(), new Item(id, player.getGroupIronmanBank().getAmount(id)), slot, true, true);
                player.getGroupIronmanBank().open(player);
            }
            case Bank.INVENTORY_INTERFACE_ID -> {
                Item item = player.getInventory().forSlot(slot).copy().setAmount(player.getInventory().getAmount(id));
                if (player.isBanking() && player.getInterfaceId() == 106000 && player.getInventory().contains(item.getId())) {
                    player.getInventory().switchItem(player.getGroupIronmanBank(), item, slot, false, true);
                    player.getGroupIronmanBank().refreshItems(player);
                    return;
                }
                player.setCurrentBankTab(Bank.getTabForItem(player, item.getId()));
                player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
            }
            case 3823 -> {
                if (player.isShopping()) {
                    ShopHandler.sell(player, slot, 10);
                } else {
                    System.out.println("Not shopping.");
                }
            }
            case BeastOfBurden.INTERFACE_ID -> {
                if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
                        && player.getSummoning().getBeastOfBurden() != null) {
                    Item storeItem = new Item(id, 29);
                    if (storeItem.getDefinition().isStackable()) {
                        player.getPacketSender().sendMessage("You cannot store stackable items.");
                        return;
                    }
                    player.getInventory().switchItem(player.getSummoning().getBeastOfBurden(), storeItem, slot, false,
                            true);
                }
            }
            case PriceChecker.INTERFACE_PC_ID -> {
                if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
                    player.getInventory().switchItem(player.getPriceChecker(),
                            new Item(id, player.getInventory().getAmount(id)), slot, false, true);
                }
            }
        }

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.getSummoning().getBeastOfBurden().switchItem(player.getInventory(), new Item(id, 29),
						BeastOfBurden.beastOfBurdenSlot(interfaceId), false, true);
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.getPriceChecker().switchItem(player.getInventory(),
						new Item(id, player.getPriceChecker().getAmount(id)),
						PriceChecker.priceCheckerSlot(interfaceId), false, true);
			}
		}
	}

	/**
	 * Manages an item's fifth action.
	 *
	 * @param player The player clicking the item.
	 * @param packet The packet to read values from.
	 */
	private static void fifthAction(Player player, Packet packet) {
		int slot = packet.readShortA();
		int interfaceId = packet.readShortA();
		int id = packet.readShortA();
		if (player.getRank().isDeveloper()) {
			player.getPacketSender().sendMessage("fifthAction itemContainer. IF: " + interfaceId + " slot: " + slot + ", id: " + id);
		}
		if(interfaceId == 31444)
			if(player.getEquipment().handleContainer(slot, 5, id))
				return;

		if(ShopHandler.handleShop(player, interfaceId, 5, slot, id))
			return;

		switch (interfaceId) {
			case TradingPost.ITEM_CONTAINER_ID -> player.getPacketSender().sendMessage("X value here");
			case 31510 -> player.getEventBossManager().removeNpcDropReward(id, player.getInventory().getAmount(id));
			case 2900 -> {
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to add to the NPC Drops?");
				player.setInputHandling(new EnterAmount() {
					public void handleAmount(Player player, int amount) {
						player.getEventBossManager().addNpcDropReward(id, amount, slot);
					}
				});
			} // smithing interface row 1
			// row 2
			// row 3
			// row 4
			case 1119, 1120, 1121, 1122, 1123 -> { // row 5
				// System.out.println(player.getInterfaceId() + " is interfaceid");
				if (player.getInterfaceId() == 994) {
					player.setInputHandling(new EnterAmountToMakeSmithing(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to smith?");
				}
			}

			/*
			 * Item item111 = new Item(id); int barsRequired =
			 * SmithingData.getBarAmount(item111); Item bar = new
			 * Item(player.getSelectedSkillingItem(), barsRequired); int x =
			 * (player.getInventory().getAmount(bar.getId()) / barsRequired);
			 * EquipmentMaking.smithItem(player, new Item(player.getSelectedSkillingItem(),
			 * barsRequired), new Item(item111.getId(),
			 * SmithingData.getItemAmount(item111)), x);
			 */
			case Trading.INTERFACE_ID -> {
				if (player.getTrading().inTrade()) {
					player.setInputHandling(new EnterAmountToTrade(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to trade?");
				} else if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
					player.setInputHandling(new EnterAmountToStake(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to stake?");
				} else if (player.getGambling().inGamble()) {
					player.setInputHandling(new EnterAmountToGamble(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to gamble?");
				}
			}
			case -8365 -> {
				if (player.getGambling().inGamble()) {
					player.setInputHandling(new EnterAmountToRemoveGamble(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
				}
			}
			case Trading.INTERFACE_REMOVAL_ID -> {
				if (player.getTrading().inTrade()) {
					player.setInputHandling(new EnterAmountToRemoveFromTrade(id));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
				}
			}
			case Dueling.INTERFACE_REMOVAL_ID -> {
				if (Dueling.checkDuel(player, 1) || Dueling.checkDuel(player, 2)) {
					player.setInputHandling(new EnterAmountToRemoveFromStake(id));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
				}
			} // BANK X
			case Bank.INVENTORY_INTERFACE_ID, 12 -> {
				if (player.isBanking()) {
					Item item = player.getInventory().forSlot(slot).copy().setAmount(player.getInventory().getAmount(id));
					if (item.getId() == 6500) {
						if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId()) || player.getInterfaceId() != 5292)
							return;
						player.setCurrentBankTab(Bank.getTabForItem(player, item));
						player.getInventory().switchItem(player.getBank(player.getCurrentBankTab()), item, slot, false, true);
						return;
					} else {
						player.setInputHandling(new EnterAmountToBank(id, slot));
						player.getPacketSender().sendEnterAmountPrompt("How many would you like to bank?");
					}
				}
			}
			case Bank.INTERFACE_ID, GroupIronmanBank.INTERFACE_ID, 11 -> {
				if (player.isBanking()) {
					/*Item item = player.getBank(player.getCurrentBankTab()).forSlot(slot).copy().setAmount(player.getBank(player.getCurrentBankTab()).getAmount(id));
					if(item.getId() == 6500) {
						if (!player.isBanking() || item.getId() != id || !player.getInventory().contains(item.getId()) || player.getInterfaceId() != 5292)
							return;*/
					player.setInputHandling(new EnterAmountToRemoveFromBank(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to withdraw?");
					/*} else {
						if (player.getBank(Bank.getTabForItem(player, id)).getAmount(id) == 1) {
							player.getPacketSender()
									.sendMessage("You only have 1 " + ItemDefinition.forId(id).getName() + "!");
						}
						player.getBank(player.getCurrentBankTab()).open(player, false);
					}*/
				}
			}
			case 30929 -> {
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to buy?");
				player.setInputHandling(new EnterAmount() {
					public void handleAmount(Player player, int amount) {
						ShopHandler.buy(player, id, amount);
					}
				});
			}
			case 3823 -> {
				if (player.isBanking())
					return;
				if (player.isShopping()) {
					player.setInputHandling(new EnterAmountToSellToShop(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to sell?");
				} else {
					System.out.println("Not shopping.");
				}
			}
			case PriceChecker.INTERFACE_PC_ID -> {
				if (player.getInterfaceId() == PriceChecker.INTERFACE_ID && player.getPriceChecker().isOpen()) {
					player.setInputHandling(new EnterAmountToPriceCheck(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to pricecheck?");
				}
			}
			case BeastOfBurden.INTERFACE_ID -> {
				if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
						&& player.getSummoning().getBeastOfBurden() != null) {
					Item storeItem = new Item(id, 10);
					if (storeItem.getDefinition().isStackable()) {
						player.getPacketSender().sendMessage("You cannot store stackable items.");
						return;
					}
					player.setInputHandling(new EnterAmountToStore(id, slot));
					player.getPacketSender().sendEnterAmountPrompt("How many would you like to store?");
				}
			}
		}

		if (BeastOfBurden.beastOfBurdenSlot(interfaceId) >= 0) {
			if (player.getInterfaceId() == BeastOfBurden.INTERFACE_ID
					&& player.getSummoning().getBeastOfBurden() != null) {
				player.setInputHandling(new EnterAmountToRemoveFromBob(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
		} else if (PriceChecker.priceCheckerSlot(interfaceId) >= 0) {
			if (player.getPriceChecker().isOpen()) {
				player.setInputHandling(new EnterAmountToRemoveFromPriceCheck(id, slot));
				player.getPacketSender().sendEnterAmountPrompt("How many would you like to remove?");
			}
		}
	}

	private static void sixthAction(Player player, Packet packet) {
		int interfaceId = packet.readShortA();
		int slot = packet.readShortA();
		int id = packet.readShortA();
		if (player.getRank().isDeveloper()) {
			player.getPacketSender().sendMessage("sixthAction itemContainer. IF: " + interfaceId + " slot: " + slot + ", id: " + id);
		}
		if(interfaceId == 31444)
			if(player.getEquipment().handleContainer(slot, 6, id))
				return;
	}

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		switch (packet.getOpcode()) {
			case FIRST_ITEM_ACTION_OPCODE -> firstAction(player, packet);
			case SECOND_ITEM_ACTION_OPCODE -> secondAction(player, packet);
			case THIRD_ITEM_ACTION_OPCODE -> thirdAction(player, packet);
			case FOURTH_ITEM_ACTION_OPCODE -> fourthAction(player, packet);
			case FIFTH_ITEM_ACTION_OPCODE -> fifthAction(player, packet);
			case SIXTH_ITEM_ACTION_OPCODE -> sixthAction(player, packet);
		}
	}

	public static final int FIRST_ITEM_ACTION_OPCODE = 145;
	public static final int SECOND_ITEM_ACTION_OPCODE = 117;
	public static final int THIRD_ITEM_ACTION_OPCODE = 43;
	public static final int FOURTH_ITEM_ACTION_OPCODE = 129;
	public static final int FIFTH_ITEM_ACTION_OPCODE = 135;
	public static final int SIXTH_ITEM_ACTION_OPCODE = 138;
}
