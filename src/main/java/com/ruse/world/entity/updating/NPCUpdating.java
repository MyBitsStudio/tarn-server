package com.ruse.world.entity.updating;

import com.ruse.model.*;
import com.ruse.net.packet.ByteOrder;
import com.ruse.net.packet.Packet.PacketType;
import com.ruse.net.packet.PacketBuilder;
import com.ruse.net.packet.PacketBuilder.AccessType;
import com.ruse.net.packet.ValueType;
import com.ruse.world.World;
import com.ruse.world.entity.Entity;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

/**
 * Represents a player's npc updating task, which loops through all local npcs
 * and updates their masks according to their current attributes.
 *
 * @author Relex lawl
 */

public class NPCUpdating {

	/**
	 * Handles the actual npc updating for the associated player.
	 *
	 * @return The NPCUpdating instance.
	 */
	public static void update(@NotNull Player player) {
		PacketBuilder update = new PacketBuilder();
		PacketBuilder packet = new PacketBuilder(65, PacketType.SHORT);
		packet.initializeAccess(AccessType.BIT);
		packet.putBits(8, player.getLocalNpcs().size());
		for (Iterator<NPC> npcIterator = player.getLocalNpcs().iterator(); npcIterator.hasNext(); ) {
			NPC npc = npcIterator.next();
			if (World.getNpcs().get(npc.getIndex()) != null && npc.isVisible()
					&& player.getPosition().isWithinDistance(npc.getPosition())
					&& player.getPosition().getZ() == npc.getPosition().getZ()
					&& !npc.isNeedsPlacement()) {
				if(npc.isSummoningNpc()){
					if(player.getPSettings().getBooleanValue("hidden-players")){
						player.getNpcFacesUpdated().remove(npc);
						npcIterator.remove();
						packet.putBits(1, 1);
						packet.putBits(2, 3);
						continue;
					}
				}
				updateMovement(npc, packet);
				if (npc.getUpdateFlag().isUpdateRequired()) {
					appendUpdates(npc, update);
				}
			} else  {
				player.getNpcFacesUpdated().remove(npc);
				npcIterator.remove();
				packet.putBits(1, 1);
				packet.putBits(2, 3);
			}
		}
		int number = 0;
		for (NPC npc : World.getNpcs()) {
			if (player.getLocalNpcs().size() >= 255 || number >= 24) // Originally 255
				break;
			if (npc == null || player.getLocalNpcs().contains(npc) || !npc.isVisible() || npc.isNeedsPlacement())
				continue;
			if (npc.getPosition().isWithinDistance(player.getPosition())) {
					if(npc.isSummoningNpc()){
						if(player.getPSettings().getBooleanValue("hidden-players")){
							continue;
						}
					}
					player.getLocalNpcs().add(npc);
					number++;
					addNPC(player, npc, packet);
					if (npc.getUpdateFlag().isUpdateRequired()) {
						appendUpdates(npc, update);
					}
			}
		}
		if (update.buffer().writerIndex() > 0) {
			packet.putBits(14, 16383);
			packet.initializeAccess(AccessType.BYTE);
			packet.writeBuffer(update.buffer());
		} else {
			packet.initializeAccess(AccessType.BYTE);
		}
		player.getSession().queueMessage(packet);
	}

	/**
	 * Adds an npc to the associated player's client.
	 *
	 * @param npc     The npc to add.
	 * @param builder The packet builder to write sendInformation on.
	 * @return The NPCUpdating instance.
	 */
	private static void addNPC(@NotNull Player player, @NotNull NPC npc, @NotNull PacketBuilder builder) {
		builder.putBits(14, npc.getIndex());
		builder.putBits(5, npc.getPosition().getY() - player.getPosition().getY());
		builder.putBits(5, npc.getPosition().getX() - player.getPosition().getX());
		builder.putBits(1, 0);
		builder.putBits(18, npc.getId());
		builder.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
	}

	/**
	 * Updates the npc's movement queue.
	 *
	 * @param npc     The npc who's movement is updated.
	 * @param builder The packet builder to write sendInformation on.
	 * @return The NPCUpdating instance.
	 */
	private static void updateMovement(@NotNull NPC npc, PacketBuilder out) {
		if (npc.getSecondaryDirection().toInteger() == -1) {
			if (npc.getPrimaryDirection().toInteger() == -1) {
				if (npc.getUpdateFlag().isUpdateRequired()) {
					out.putBits(1, 1);
					out.putBits(2, 0);
				} else {
					out.putBits(1, 0);
				}
			} else {
				out.putBits(1, 1);
				out.putBits(2, 1);
				out.putBits(3, npc.getPrimaryDirection().toInteger());
				out.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
			}
		} else {
			out.putBits(1, 1);
			out.putBits(2, 2);
			out.putBits(3, npc.getPrimaryDirection().toInteger());
			out.putBits(3, npc.getSecondaryDirection().toInteger());
			out.putBits(1, npc.getUpdateFlag().isUpdateRequired() ? 1 : 0);
		}
	}

	/**
	 * Appends a mask update for {@code npc}.
	 *
	 * @param npc     The npc to update masks for.
	 * @param builder The packet builder to write sendInformation on.
	 * @return The NPCUpdating instance.
	 */
	private static void appendUpdates(@NotNull NPC npc, PacketBuilder block) {
		int mask = 0;
		UpdateFlag flag = npc.getUpdateFlag();
		if (flag.flagged(Flag.ANIMATION) && npc.getAnimation() != null) {
			mask |= 0x10;
		}
		if (flag.flagged(Flag.GRAPHIC) && npc.getGraphic() != null) {
			mask |= 0x80;
		}
		if (flag.flagged(Flag.SINGLE_HIT)) {
			mask |= 0x8;
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			mask |= 0x20;
		}
		if (flag.flagged(Flag.FORCED_CHAT) && npc.getForcedChat().length() > 0) {
			mask |= 0x1;
		}
		if (flag.flagged(Flag.DOUBLE_HIT)) {
			mask |= 0x40;
		}
		if (flag.flagged(Flag.TRANSFORM) && npc.getTransformationId() != -1) {
			mask |= 0x2;
		}
		if (flag.flagged(Flag.FACE_POSITION) && npc.getPositionToFace() != null) {
			mask |= 0x4;
		}
		block.put(mask);
		if (flag.flagged(Flag.ANIMATION) && npc.getAnimation() != null) {
			updateAnimation(block, npc);
		}
		if (flag.flagged(Flag.SINGLE_HIT)) {
			updateSingleHit(block, npc);
		}
		if (flag.flagged(Flag.GRAPHIC) && npc.getGraphic() != null) {
			updateGraphics(block, npc);
		}
		if (flag.flagged(Flag.ENTITY_INTERACTION)) {
			Entity entity = npc.getInteractingEntity();
			block.putShort(entity == null ? -1 : entity.getIndex() + (entity instanceof Player ? 32768 : 0));
		}
		if (flag.flagged(Flag.FORCED_CHAT) && npc.getForcedChat().length() > 0) {
			block.putString(npc.getForcedChat());
		}
		if (flag.flagged(Flag.DOUBLE_HIT)) {
			updateDoubleHit(block, npc);
		}
		if (flag.flagged(Flag.TRANSFORM) && npc.getTransformationId() != -1) {
			block.putShort(npc.getTransformationId(), ValueType.A, ByteOrder.LITTLE);
		}
		if (flag.flagged(Flag.FACE_POSITION) && npc.getPositionToFace() != null) {
			final Position position = npc.getPositionToFace();
			int x = position == null ? 0 : position.getX();
			int y = position == null ? 0 : position.getY();
			block.putShort(x * 2 + 1, ByteOrder.LITTLE);
			block.putShort(y * 2 + 1, ByteOrder.LITTLE);
		}
	}

	/**
	 * Updates {@code npc}'s current animation and displays it for all local
	 * players.
	 *
	 * @param builder The packet builder to write sendInformation on.
	 * @param npc     The npc to update animation for.
	 * @return The NPCUpdating instance.
	 */
	private static void updateAnimation(@NotNull PacketBuilder builder, @NotNull NPC npc) {
		builder.putShort(npc.getAnimation().getId(), ByteOrder.LITTLE);
		builder.put(npc.getAnimation().getDelay());
	}

	/**
	 * Updates {@code npc}'s current graphics and displays it for all local players.
	 *
	 * @param builder The packet builder to write sendInformation on.
	 * @param npc     The npc to update graphics for.
	 * @return The NPCUpdating instance.
	 */
	private static void updateGraphics(@NotNull PacketBuilder builder, @NotNull NPC npc) {
		builder.putShort(npc.getGraphic().getId());
		builder.putInt(((npc.getGraphic().getHeight().ordinal() * 50) << 16) + (npc.getGraphic().getDelay() & 0xffff));
	}

	/**
	 * Updates the npc's single hit.
	 *
	 * @param builder The packet builder to write sendInformation on.
	 * @param npc     The npc to update the single hit for.
	 * @return The NPCUpdating instance.
	 */
	private static void updateSingleHit(@NotNull PacketBuilder builder, @NotNull NPC npc) {
		builder.putLong(npc.getPrimaryHit().getDamage());
		builder.put(npc.getPrimaryHit().getHitmask().ordinal());
		builder.put(npc.getPrimaryHit().getCombatIcon().ordinal() - 1);
		builder.putLong(npc.getConstitution());
		builder.putLong(npc.getDefaultConstitution());
	}

	/**
	 * Updates the npc's double hit.
	 *
	 * @param builder The packet builder to write sendInformation on.
	 * @param npc     The npc to update the double hit for.
	 * @return The NPCUpdating instance.
	 */
	private static void updateDoubleHit(@NotNull PacketBuilder builder, @NotNull NPC npc) {
		builder.putLong(npc.getSecondaryHit().getDamage());
		builder.put(npc.getSecondaryHit().getHitmask().ordinal(), ValueType.S);
		builder.put(npc.getSecondaryHit().getCombatIcon().ordinal() - 1);
		builder.putLong(npc.getConstitution());
		builder.putLong(npc.getDefaultConstitution());
	}

	/**
	 * Resets all the npc's flags that should be reset after a tick's update
	 *
	 * @param npc The npc to reset flags for.
	 */
	public static void resetFlags(@NotNull NPC npc) {
		npc.getUpdateFlag().reset();
		npc.setTeleporting(false).setForcedChat("");
		npc.setNeedsPlacement(false);
		npc.setPrimaryDirection(Direction.NONE);
		npc.setSecondaryDirection(Direction.NONE);
	}
}
