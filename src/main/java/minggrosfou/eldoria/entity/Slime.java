package minggrosfou.eldoria.entity;

import minggrosfou.eldoria.item.currency.Copper;

public class Slime extends Entity{
    public Slime() {
        super("Slime", Race.SLIME);

        backstory = "Regular slime";

        level = 1;

        inventory.add_item(new Copper());
    }
}
