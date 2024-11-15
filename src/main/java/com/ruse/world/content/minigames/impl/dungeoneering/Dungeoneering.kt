package com.ruse.world.content.minigames.impl.dungeoneering

import com.ruse.GameSettings
import com.ruse.model.*
import com.ruse.util.Misc
import com.ruse.world.World
import com.ruse.world.content.dailytasks_new.DailyTask
import com.ruse.world.content.minigames.impl.dungeoneering.Dungeoneering.Constants.KILLS_REQUIRED
import com.ruse.world.content.minigames.impl.dungeoneering.Dungeoneering.Constants.TOKENS
import com.ruse.world.entity.impl.GroundItemManager
import com.ruse.world.entity.impl.npc.NPC
import com.ruse.world.entity.impl.npc.NPCMovementCoordinator
import com.ruse.world.entity.impl.player.Player
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

class Dungeoneering(val group: DungeoneeringParty) {


    //var mapInstance: MinigameInstance? = null
    var height: Int = -1;
    var minionKills: Int = 0
    var bossDead: Boolean = false
    var bossSpawned: Boolean = false
    var partyDeaths = ConcurrentHashMap<String, Int>()

    /**
     * Initializes the map instance and moves the players into it
     */
    fun startDungeon() {
        // Create the dungeon instance
        //mapInstance = DungeoneeringInstance()
       // mapInstance!!.initialise()
        height = group.owner.index * 4

        // Setup anyone in the lobby & our CC
        group.players.forEach {
            it.regionInstance = RegionInstance(it, RegionInstance.RegionInstanceType.DUNGEONEERING);


        }



        //for (i in 0 until KILLS_REQUIRED) {
            val npc = NPC(Constants.MINION_MOB, Position(2276 + Misc.getRandom(19), 5001 + Misc.getRandom(19), height))
            npc.respawn = false
            npc.movementCoordinator.coordinator = NPCMovementCoordinator.Coordinator(true, 3)
            //npc.constitution = (npc.definition.hitpoints * group.players.size)
            World.register(group.owner, npc)
        //}

        group.enteredDungeon(true)
    }



    private fun giveRewards() {
        val baseXp = 10000
        group.players.forEach {
            val exp = baseXp * it.dungeoneeringFloor + (baseXp + it.dungeoneeringPrestige)
            val tokens = exp / 250


           // it.skillManager.addExperience(Skill.DUNGEONEERING, exp)
            if (!it.inventory.isFull) {
                it.inventory.add(TOKENS, tokens)
            } else {
                GroundItemManager.spawnGroundItem(it,
                        GroundItem(Item(TOKENS, tokens), Constants.LOBBY.copy(),
                                it.toString(), false, 150, false, -1)
                )
            }
            it.moveTo(Constants.LOBBY.copy())
        }
        messagePlayers("You have completed the dungeon and receive tokens.")

    }



    private fun messagePlayers(message: String) {
        group.players.forEach { it.sendMessage(message) }
    }

    companion object {

        fun leaveDungeon(p: Player) {
           // p.mapInstance.remove(p, true)
            p.controllerManager.removeControllerWithoutCheck()
        }
        fun leaveParty(p: Player) {

            if (p.minigameAttributes.dungeoneeringAttributes.party != null) {

            }

            if (p.minigameAttributes.dungeoneeringAttributes.party != null) {
                p.minigameAttributes.dungeoneeringAttributes.party.remove(p, false, true)
            }

            DungeoneeringParty.clearInterface(p)
            p.controllerManager.removeControllerWithoutCheck()

            p.moveTo(Constants.LOBBY.copy())
            p.regionInstance = null

        }

        fun leaveLobby(p: Player) {

            // Only if we dont have a dungeoneering controller, because that means
            // we're in a dg instance, its just not Locations.DUNGEONEERING
            //if(p.controllerManager.controller == null) {
            p.packetSender.sendTabInterface(GameSettings.QUESTS_TAB, 111000) //
            p.packetSender.sendDungeoneeringTabIcon(false)
            p.packetSender.sendTab(GameSettings.QUESTS_TAB)
            // }

            if (p.playerInteractingOption != PlayerInteractingOption.NONE) {
                p.packetSender.sendInteractionOption("null", 2, true)
            }
            if (p.minigameAttributes.dungeoneeringAttributes.party != null) {
                p.minigameAttributes.dungeoneeringAttributes.party.remove(p, false, true)
            }
            p.regionInstance = null
        }
        fun ready(group: DungeoneeringParty) : Boolean {
            var ready = true
            group.players.forEach {

            }
            return ready
        }

        fun checkInv(player: Player, invite: Player): Boolean {
            if (invite == null) {
                player.packetSender.sendMessage("That player is currently not online.")
                return false
            }
            if (invite.minigameAttributes.dungeoneeringAttributes.party != null) {
                player.packetSender.sendMessage("That player is already in a party.")
                return false
            }
            if (player.minigameAttributes.dungeoneeringAttributes.party == null) {
                player.packetSender.sendMessage("You're not in a party!")
                return false
            }
            if (player.minigameAttributes.dungeoneeringAttributes.party.players.contains(invite)) {
                player.packetSender.sendMessage("That player is already in your party.")
                return false
            }
            if (player.minigameAttributes.dungeoneeringAttributes.party.owner !== player) {
                player.packetSender.sendMessage("Only the party leader can invite other players.")
                return false
            }
            if (player.minigameAttributes.dungeoneeringAttributes.party.players.size >= 5) {
                player.packetSender.sendMessage("Your party is full.")
                return false
            }
            if (player.busy()) {
                player.packetSender.sendMessage("You're busy and can't invite anyone.")
                return false
            }
            if (invite.busy()) {
                player.packetSender.sendMessage(invite.username + " is too busy to get your invite.")
                return false
            }
            return true
        }
    }

    object Constants {
        const val NUMBER_FLOORS = 50// How many floors before prestige
        val LOBBY = Position(2251, 5040, 0)
        private val DUNGEON = Position(0, 0, 0)
        const val MINION_MOB = 9846
        const val CRYSTAL_OBJECT = 10014
        const val TOKENS = 12333
        const val KILLS_REQUIRED = 15// The number of minions to spawn
    }
}