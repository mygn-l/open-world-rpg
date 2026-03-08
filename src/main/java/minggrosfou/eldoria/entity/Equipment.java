package minggrosfou.eldoria.entity;

import minggrosfou.eldoria.item.Item;

import java.util.EnumMap;

public class Equipment {
    public enum Equip_Slot {
        MAIN_HAND,
        OFF_HAND,
        HEAD,
        FACE,
        EARRING,
        NECK,
        CHEST,
        UPPER_INNER,
        UPPER_OUTER,
        ARM_LEFT,
        ARM_RIGHT,
        WRIST_LEFT,
        WRIST_RIGHT,
        HAND_LEFT,
        HAND_RIGHT,
        RING_LEFT,
        RING_RIGHT,
        WAIST,
        BACK,
        LOWER_INNER,
        LOWER_OUTER,
        LEG_LEFT,
        LEG_RIGHT,
        ANKLE_LEFT,
        ANKLE_RIGHT,
        FOOT_LEFT,
        FOOT_RIGHT
    }

    private final EnumMap<Equip_Slot, Item> equipped = new EnumMap<>(Equip_Slot.class);

    Inventory inventory;

    public void equip(Item item, Equip_Slot slot) {
        if (!item.equippable) {
            return;
        }

        for (Equip_Slot allowed_slot : item.allowed_slots) {
            if (allowed_slot == slot) {
                unequip(slot);
                equipped.put(slot, item);
                return;
            }
        }
    }

    public void unequip(Equip_Slot slot) {
        Item current_item = equipped.get(slot);
        if (current_item != null) {
            inventory.add_item(current_item);
        }
        equipped.put(slot, null);
    }

    public Equipment(Inventory inventory) {
        this.inventory = inventory;
    }
}
