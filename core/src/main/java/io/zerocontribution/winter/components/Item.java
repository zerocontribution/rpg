package io.zerocontribution.winter.components;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Item {
    public int id;
    public String name;
    public String description;
    public ItemType type;
    public Sprite icon;
    public String texturePath;
    public String iconPath;

    public Item() {}
    public Item(ItemType type, int id) {
        this.type = type;
        this.id = id;
    }

    public enum ItemType {
        GENERAL,
        WEAPON,
        HEAD_ARMOR,
        ACCESSORY,
        AUGMENT,
        CONSUMABLE
    }

}
