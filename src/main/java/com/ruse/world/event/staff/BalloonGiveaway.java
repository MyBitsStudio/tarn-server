//package com.ruse.world.event.staff;
//
//import com.ruse.model.GameObject;
//import com.ruse.model.Position;
//import com.ruse.util.Misc;
//import com.ruse.world.World;
//import com.ruse.world.entity.impl.player.Player;
//import com.ruse.world.event.Event;
//
//import javax.xml.stream.Location;
//
//public class BalloonGiveaway extends Event {
//
//    public static int[] balloons = { 115, 116, 117, 118, 119, 120, 121, 122 };
//
//    public BalloonGiveaway(Player player, Location loc) {
//        super(player, loc);
//    }
//
//    @Override
//    public void start() {
//        Position pos = getHost().getPosition();
//        World.sendNewsMessage("@yel@ [EVENT] Staff has spawned a balloon event! Get them now!");
//        for(int x = -5; x < 5; x++){
//            for(int y = -5; y < 5; y++){
//                World.register(new GameObject(balloons[Misc.random(balloons.length - 1)], pos.add(x, y), 10, 3));
//            }
//        }
//    }
//
//    @Override
//    public void stop() {
//        World.sendNewsMessage("@yel@ [EVENT] Balloon event has ended!");
//    }
//
//    @Override
//    public String key() {
//        return "balloon";
//    }
//
//    @Override
//    public void moveTo(Player player) {
//
//    }
//}
