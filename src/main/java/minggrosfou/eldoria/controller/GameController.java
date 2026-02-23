package minggrosfou.eldoria.controller;

import minggrosfou.eldoria.World;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/")
class GameController {
    World world = new World();

    @RequestMapping(value = "/get-map", method = RequestMethod.GET)
    public int[][] get_map() {
        return world.km_heightmap;
    }

    @RequestMapping(value = "/get-biome", method = RequestMethod.GET)
    public World.Biome[][] get_biome() {
        return world.biomes;
    }
}
