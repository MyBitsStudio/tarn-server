package com.ruse.world.content;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;

import java.security.SecureRandom;

public class WheelOfFortune {
 
    private final int INTERFACE_ID = 21350;
    private final int WHEEL_INTERFACE_ID = 21352;
    private final int MODEL_COMPONENT_ID = 21377;
    private final int[] REWARDS = {22239, 22238, 22237, 22240, 22236, 19624, 22234, 22233, 22232, 22235};
 
 
    private Player player;
    //unused for now
    private int segments = 10;
    private final SecureRandom secureRandom = new SecureRandom();
 
    private WheelOfFortuneGame game = null;
    public WheelOfFortune(Player player) {
        this.player = player;
    }
 
    public void init(int[] items) {
 
    }
 
    public void open() {
        player.getPacketSender().sendInterface(INTERFACE_ID);
        player.getPacketSender().updateInterfaceVisibility(21370, false);
        player.getPacketSender().updateInterfaceVisibility(21362, false);
        //initGame();
    }
 
    public void start() {
        if (game != null) {
            player.sendMessage("@red@The wheel is already spinning, wait for it to finish before spinning again");
            return;
        }
        initGame();
 
    }
 
    private void initGame() {
        //unused for now
        //  List<Integer> left = Arrays.stream(REWARDS).boxed().collect(Collectors.toList());
        /*int[] result = Stream.iterate(0, Integer::intValue)
                .limit(segments)
                .mapToInt(i -> left.remove(random.nextInt(left.size())))
                .toArray();*/
        if (player.getInventory().contains(23002)) {
            if (player.getInventory().isFull()){
                player.sendMessage("Your inventory is full.");
                return;
            }
            player.getInventory().delete(23002, 1);
            game = new WheelOfFortuneGame(REWARDS);
            player.getPacketSender().initWheelOfFortune(WHEEL_INTERFACE_ID, game.getWinningIndex(), game.getItems());
        }else{
            player.sendMessage("You need an Owner's chest to do this.");
        }
    }
 
    public void onFinish(int index) {
        if (index != game.getWinningIndex()) {
            player.sendMessage("Wrong index :C");
            return;
        }
        player.getPacketSender().sendInterfaceItemModel(MODEL_COMPONENT_ID, game.getReward().getId());
        player.getPacketSender().updateInterfaceVisibility(21370, true);
        player.getPacketSender().updateInterfaceVisibility(21362, true);
        player.getInventory().add(game.getReward());

        String message = "@red@" + player.getUsername() + " <col=ff812f>has just received @red@" + game.getReward().getDefinition().getName() + " <col=ff812f>from an @red@Owner's Chest";
        World.sendNewsMessage(message);

        game = null;
    }
 
}