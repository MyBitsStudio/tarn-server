package com.ruse.world.packages.tower;

import com.ruse.model.GameObject;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.Instance;
import com.ruse.world.packages.tower.npcs.TowerBoss;
import com.ruse.world.packages.tower.npcs.TowerMinions;
import com.ruse.world.packages.tower.props.Tower;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

public class TowerLevel extends Instance {

    private int amount;
    @Getter@Setter
    private TowerProgress tower;

    @Getter@Setter
    private Player owner;

    private final long started;

    public TowerLevel(Locations.Location location, Player owner, TowerProgress progress) {
        super(location);
        setOwner(owner);
        setTower(progress);
        started = System.currentTimeMillis();
    }

    @Override
    public void start(){
        int tier = tower.getTier(), level = tower.getLevel();
        Tower tower = Tower.get(tier, level);
        if(tower == null)
            return;

        moveTo(getOwner(), tower.getPosition().setZ(getOwner().getIndex() * 4));
        add(getOwner());

        getOwner().sendMessage("You have entered the tower. You have 10 minutes to clear.");

        spawnObjects();

        spawnNPCs(tower);
    }

    private void spawnObjects(){
        World.register(new GameObject(16686, new Position(3016, 2864, getOwner().getIndex() * 4)));
    }

    private void spawnNPCs(@NotNull Tower tower){
        amount = tower.getNpcIds().length;
        TowerBoss boss = new TowerBoss(tower.getNpcIds()[0], tower.getNpcPositions()[0].setZ(getOwner().getIndex() * 4));
        boss.buff(tower.getBuffs());
        boss.setInstance(this);
        boss.setSpawnedFor(getOwner());
        add(boss);
        TowerMinions[] minions = new TowerMinions[amount - 1];
        if(tower.getNpcIds().length > 1){
            for(int i = 1; i < tower.getNpcIds().length; i++){
                minions[i - 1] = new TowerMinions(tower.getNpcIds()[i], tower.getNpcPositions()[i].setZ(getOwner().getIndex() * 4));
                minions[i - 1].setInstance(this);
                minions[i - 1].buff(tower.getBuffs());
                minions[i - 1].setSpawnedFor(getOwner());
                add(minions[i - 1]);
            }
        }
    }

    @Override
    public void process(){
        super.process();
        if(System.currentTimeMillis() > started + (1000 * 60 * 10)){
            getOwner().sendMessage("Your timer has expired.");
            this.destroy();
        }
    }

    public void check(Player player){
        amount--;
        if(amount <= 0){
            player.getTower().progress();
        }
    }

}
