package minggrosfou.eldoria.controller;

import minggrosfou.eldoria.Local_World;
import minggrosfou.eldoria.Player;
import minggrosfou.eldoria.World;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/")
class GameController {
    World world = new World();
    Player player = world.players.getFirst();
    Local_World local_world = new Local_World(world, player);

    @RequestMapping(value = "/get-map", method = RequestMethod.GET)
    public World_Map get_map() {
        return new World_Map(world, 10);
    }

    @RequestMapping(value = "/get-farsight-map", method = RequestMethod.GET)
    public int[][] get_farsight_map() {
        local_world.generate_if_needed(player);
        return local_world.farsight_heightmap_m;
    }

    @RequestMapping(value = "/get-nearsight-map", method = RequestMethod.GET)
    public int[][] get_nearsight_map() {
        local_world.generate_if_needed(player);
        return local_world.nearsight_heightmap_m;
    }
}
