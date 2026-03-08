package minggrosfou.eldoria.item;

import minggrosfou.eldoria.entity.Equipment;

public abstract class Item {
    public String name;
    public String description;

    public boolean equippable;
    public Equipment.Equip_Slot[] allowed_slots;

    public double weight;
    public double volume;
}
