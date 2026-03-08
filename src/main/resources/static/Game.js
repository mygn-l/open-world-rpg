import Socket from "./Socket.js";
import Renderer from "./Renderer.js";
import Rest from "./Rest.js";
import Physics from "./Physics.js";
import Config from "./Config.js";

class Game {
    constructor() {
        const socket = new Socket("ws://localhost:8080/ws");
        const rest = new Rest();
        const renderer = new Renderer(document.body);
        const physics = new Physics(renderer);

        rest.get_farsight_map().then(function (result) {
            const heights = result.heights;
            const bottom_left_m = result.farsight_bottom_left_m;
            renderer.add_farsight(heights, bottom_left_m);
            renderer.render();
        });

        physics.init()
            .then(function () {
                loop();
                return rest.get_nearsight_map();
            })
            .then(function (result) {
                const heights = result.heights;
                const bottom_left_m = result.nearsight_bottom_left_m;
                const player_world_cm = result.player_world_cm;
                renderer.add_nearsight(heights, bottom_left_m);
                physics.heightfield_from(heights, bottom_left_m);
                physics.set_camera_from_cm(player_world_cm);
                console.log(physics.camera_body.translation());
                renderer.render();
            });

        const active_keys = {
            a: false,
            w: false,
            d: false,
            s: false,
            " ": false
        };

        window.addEventListener("keyup", function (e) {
            e.preventDefault();
            active_keys[e.key] = false;
        });

        window.addEventListener("keydown", function (e) {
            e.preventDefault();
            active_keys[e.key] = true;

            switch (e.key) {
                case "ArrowLeft":
                    renderer.camera.rotation.y += 0.2;
                    break;
                case "ArrowRight":
                    renderer.camera.rotation.y -= 0.2;
                    break;
                case "ArrowDown":
                    renderer.camera.rotation.x -= 0.2;
                    break;
                case "ArrowUp":
                    renderer.camera.rotation.x += 0.2;
                    break;
            }
        });

        const loop = function () {
            if (active_keys.a) {
                physics.player_left(renderer.camera);
            }
            if (active_keys.w) {
                physics.player_advance(renderer.camera);
            }
            if (active_keys.d) {
                physics.player_right(renderer.camera);
            }
            if (active_keys.s) {
                physics.player_back(renderer.camera);
            }
            if (active_keys[" "]) {
                physics.player_jump();
                console.log("wtf")
            }

            physics.world.step();

            renderer.camera.position.x = physics.camera_body.translation().x;
            renderer.camera.position.y = physics.camera_body.translation().y;
            renderer.camera.position.z = physics.camera_body.translation().z;

            try {
                for (let i = 0; i < Config.nearsight_range; i++) {
                    for (let j = 0; j < Config.nearsight_range; j++) {
                        renderer.cubes[i * Config.nearsight_range + j].position.x = physics.cubes[i * Config.nearsight_range + j].translation().x;
                        renderer.cubes[i * Config.nearsight_range + j].position.y = physics.cubes[i * Config.nearsight_range + j].translation().y;
                        renderer.cubes[i * Config.nearsight_range + j].position.z = physics.cubes[i * Config.nearsight_range + j].translation().z;
                    }
                }
            } catch {}

            renderer.render();

            requestAnimationFrame(loop);
        };
    }
}

new Game();
