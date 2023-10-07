package com.ruse.world.content.planetsystem;

import com.ruse.model.Item;
import com.ruse.world.content.planetsystem.jobsite.AbstractJobSite;
import com.ruse.world.content.planetsystem.worker.Worker;
import com.ruse.world.content.planetsystem.worker.WorkerType;
import com.ruse.world.entity.impl.player.Player;

import java.util.List;
import java.util.Random;

public class PlanetManager {
    private static final int AMOUNT_OF_KINGDOMS = 6;
    private static final int INTERFACE_ID = 167500;
    private static final int WORKER_OVERLAY_INTERFACE_ID = 167513;

    private final Player player;

    private Planet selectedPlanet;

    private final Planet[] planets = new Planet[AMOUNT_OF_KINGDOMS];

    public PlanetManager(Player player) {
        this.player = player;
    }

    public boolean handleButtonClick(int id) {
        if(selectEmptyWorkerSlot(id)) return true;
        switch (id) {
            case 167516 -> {
                player.getPacketSender().removeOverlay();
                return true;
            }
            case 167503 -> {
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            }
        }
        if(id >= 167506 && id <= 167512) {
            openPlanet(planets[id - 167506]);
            return true;
        }
        return false;
    }

    private boolean selectEmptyWorkerSlot(int id) {
        if(id >= 167520 && id <= 167529) {
            if(selectedPlanet != null) {
                int slot = id - 167520;
                if(selectedPlanet.getJobSite().getWorkers().get(slot) == null) {
                    WorkerType temp = WorkerType.VALUES.get(new Random().nextInt(WorkerType.VALUES.size()));
                    Worker w = new Worker(temp);
                    AbstractJobSite jobSite = selectedPlanet.getJobSite();
                    jobSite.getWorkers().add(slot, w);
                    sendWorkerData(w, jobSite.getName(), jobSite.getWorkLength(),  slot);
                }
            }
            return true;
        }
        return false;
    }

    private void openPlanet(Planet planet) {
        if(planet == null) {
            player.getPacketSender().sendMessage("This planet is undiscovered.");
            return;
        }
        selectedPlanet = planet;
        AbstractJobSite jobSite = selectedPlanet.getJobSite();;
        List<Worker> workers = jobSite.getWorkers();
        for(int i = 0; i < workers.size(); i++) {
            Worker worker = workers.get(i);
            if(worker == null) {
                player.getPacketSender().sendMessage("es#"+i);
            } else {
                sendWorkerData(worker, jobSite.getName(), jobSite.getWorkLength(),  i);
            }
        }
        player.getPacketSender().sendString(167517, planet.getName())
                        .sendInterfaceOverlay(INTERFACE_ID, WORKER_OVERLAY_INTERFACE_ID);
    }

    private void sendWorkerData(Worker worker, String siteName, int jobLength, int slot) {
        WorkerType wt = worker.getWorkerType();
        long ticksLeft = worker.getTicksLeft();
        boolean hasJob = ticksLeft == 0;
        player.getPacketSender().sendMessage("os#"+slot)
                .sendItemOnInterface(167553+slot, new Item(wt.getIcon()))
                .sendString(167530+slot, "Lv."+worker.getLevel()+"@or2@ " + wt.getCleanName())
                .sendString(167613+slot, hasJob ? (convertToMinSeconds(ticksLeft) + " ["+siteName+"]") : "Idle")
                .updateProgressBar(167573+slot, hasJob ? (int) getPercentage(jobLength, ticksLeft) : 0)
                .updateProgressBar(167543+slot, hasJob ? (int) getPercentage(wt.getMaxHungerLevel(), worker.getHungerLevel()) : 0)
                .sendString(167563+slot, hasJob ? worker.getHungerLevel() + "/" + wt.getMaxHungerLevel() : wt.getMaxHungerLevel() + "/" + wt.getMaxHungerLevel());
    }

    private static String convertToMinSeconds(long ticks) {
        return "";
    }

    private static double getPercentage(long num1, long num2) {
        return 0.0;
    }

}
