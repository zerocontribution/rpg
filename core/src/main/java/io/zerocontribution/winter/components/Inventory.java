package io.zerocontribution.winter.components;

import com.artemis.Component;

import java.util.ArrayList;

public class Inventory extends Component {
    private ArrayList<ItemSlot> slots = new ArrayList<ItemSlot>();
    public int credits = 0;

    public Inventory(int slotCount) {
        for (int i = 0; i < slotCount; i++) {
            slots.add(new ItemSlot(i));
        }
    }

    public ItemSlot[] getSlots() {
        return slots.toArray(new ItemSlot[slots.size()]);
    }

    public boolean hasItem(Item item) {
        for (ItemSlot slot : slots) {
            if (!slot.isEmpty() && slot.getItem().id == item.id) {
                return true;
            }
        }
        return false;
    }

    public boolean addItem(Item item) {
        for (ItemSlot slot : slots) {
            if (slot.isEmpty()) {
                slot.setItem(item);
                return true;
            }
        }
        return false;
    }

    public boolean removeItem(Item item) {
        for (ItemSlot slot : slots) {
            if (!slot.isEmpty() && slot.getItem().id == item.id) {
                slot.clear();
                return true;
            }
        }
        return false;
    }

    public ItemSlot getEmptySlot() {
        for (ItemSlot slot : slots) {
            if (slot.isEmpty()) {
                return slot;
            }
        }
        return null;
    }

    public boolean isFull() {
        return getEmptySlot() == null;
    }

    public class ItemSlot {
        private int index;
        Item item;

        ItemSlot(int index) {
            this.index = index;
        }

        public boolean isEmpty() {
            return item == null;
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            this.item = item;
        }

        public void clear() {
            item = null;
        }
    }
}
