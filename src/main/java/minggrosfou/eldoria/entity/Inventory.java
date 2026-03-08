package minggrosfou.eldoria.entity;

import minggrosfou.eldoria.item.Item;

import java.util.ArrayList;

public class Inventory {
    public static double inventory_volume = 10d;

    ArrayList<Item> items = new ArrayList<>();

    public Item remove_item(int index) {
        return items.remove(index);
    }

    public double current_volume() {
        double sum = 0;
        for (Item item : items) {
            sum += item.volume;
        }
        return sum;
    }

    public void add_item(Item item) {
        if (current_volume() + item.volume < inventory_volume) {
            items.addLast(item);
        }
    }
}
