package com.ruse.world.content.newspinner.boxes;

import com.ruse.model.Item;

import java.util.ArrayList;

public class DragonballBox implements MysteryBox {

    private ArrayList<Item> common_items = new ArrayList<Item>() {
        {
            add(new Item(23171, 1));
            add(new Item(23172, 1));
            add(new Item(23173, 1));
            add(new Item(995, 10000000));
        }
    };

    private ArrayList<Item> uncommon_items = new ArrayList<Item>() {
        {
                    add(new Item(9481, 1));
                    add(new Item(9481, 1));
                    add(new Item(9483, 1));
                    add(new Item(7543, 1));
                    add(new Item(7544, 1));
                    add(new Item(7545, 1));
                    add(new Item(9478, 1));
                    add(new Item(9479, 1));
                     add(new Item(9480, 1));
                    add(new Item(16249, 1));
            add(new Item(15832 , 1));
            add(new Item(16265, 1));
        }
    };
    private ArrayList<Item> rare_items = new ArrayList<Item>() {
        {
            add(new Item(11763, 1));
            add(new Item(11764, 1));
            add(new Item(11765, 1));
            add(new Item(17702, 1));
            add(new Item(11767, 1));
            add(new Item(11766, 1));
            add(new Item(13323, 1));
            add(new Item(13324, 1));
            add(new Item(13325, 1));
            add(new Item(13327, 1));
            add(new Item(13326  , 1));
        }
    };

    private ArrayList<Item> super_rare_items = new ArrayList<Item>() {
        {
                   add(new Item(8410, 1));
                    add(new Item(8411, 1));
                    add(new Item(8412, 1));
                    add(new Item(1486, 1));
        }
    };

    @Override
    public String getName() {
        return "Dragonball Saga Box";
    }

    @Override
    public int getId() {
        return 18768;
    }

    @Override
    public ArrayList<Item> getCommon_items() {
        return common_items;
    }

    @Override
    public ArrayList<Item> getUncommon_items() {
        return uncommon_items;
    }

    @Override
    public ArrayList<Item> getRare_items() {
        return rare_items;
    }

    @Override
    public ArrayList<Item> getSuper_rare_items() {
        return super_rare_items;
    }
}
