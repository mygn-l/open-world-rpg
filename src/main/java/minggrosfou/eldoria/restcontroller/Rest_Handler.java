package minggrosfou.eldoria.restcontroller;

import minggrosfou.eldoria.world.Local_World;
import minggrosfou.eldoria.entity.Entity;
import minggrosfou.eldoria.world.Earth;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/")
class Rest_Handler {
    Earth earth = new Earth();
    Entity entity = earth.entities.getFirst();
    Local_World local_world = new Local_World(earth, entity);

    @RequestMapping(value = "/get-map", method = RequestMethod.GET)
    public World_Map get_map() {
        return new World_Map(earth, 10);
    }

    @RequestMapping(value = "/get-farsight-map", method = RequestMethod.GET)
    public Farsight_Map get_farsight_map() {
        int[] farsight_bottom_left = Local_World.farsight_bottom_left_m(entity.world_cm_whole());
        return new Farsight_Map(local_world.farsight_heightmap_10m, farsight_bottom_left);
    }

    @RequestMapping(value = "/get-nearsight-map", method = RequestMethod.GET)
    public Nearsight_Map get_nearsight_map() {
        int[] nearsight_bottom_left = Local_World.nearsight_bottom_left_m(entity.world_cm_whole());
        return new Nearsight_Map(local_world.nearsight_heightmap_m, nearsight_bottom_left, entity.world_cm_y_up());
    }
}
