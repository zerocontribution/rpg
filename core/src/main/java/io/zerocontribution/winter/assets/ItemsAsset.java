package io.zerocontribution.winter.assets;

import io.zerocontribution.winter.components.Item;

import java.util.ArrayList;

public class ItemsAsset {
    private ArrayList<Item> items;

    public Item get(int id) {
        for (Item item : items) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }

    public int size() {
        return items.size();
    }

}
