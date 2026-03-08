import RAPIER from "rapier";
import Config from "./Config.js";

class Physics {
    async init() {
        await RAPIER.init();

        const world = new RAPIER.World({x: 0.0, y: -9.81, z: 0.0});

        const camera_body = world.createRigidBody(
            RAPIER.RigidBodyDesc.dynamic()
                .lockRotations()
                .setCcdEnabled(true)
        );
        world.createCollider(RAPIER.ColliderDesc.capsule(0.55, 0.25), camera_body);

        this.world = world;
        this.camera_body = camera_body;
    }

    set_camera_from_cm(position) {
        this.camera_body.setTranslation(new RAPIER.Vector3(position[0] / 100, position[1] / 100, position[2] / 100));
    }

    heightfield_from(heights, bottom_left_m) {
        const heights1D = new Float32Array(Config.nearsight_size * Config.nearsight_size);
        for (let x = 0; x < Config.nearsight_size; x++) {
            for (let z = 0; z < Config.nearsight_size; z++) {
                heights1D[x * Config.nearsight_size + z] = heights[x][z];
            }
        }
        const heightfield_collider = this.world.createCollider(
            new RAPIER.ColliderDesc(
                new RAPIER.Heightfield(
                    Config.nearsight_size - 1,
                    Config.nearsight_size - 1,
                    heights1D,
                    new RAPIER.Vector3(100, 1, 100)
                )
            )
        );
        heightfield_collider.setTranslation(
            new RAPIER.Vector3(
                Config.nearsight_range + bottom_left_m[0],
                0,
                Config.nearsight_range + bottom_left_m[1]
            )
        );

        const cubes = [];
        for (let i = 0; i < Config.nearsight_range; i++) {
            for (let j = 0; j < Config.nearsight_range; j++) {
                const body = this.world.createRigidBody(RAPIER.RigidBodyDesc.dynamic());
                body.setTranslation(
                    new RAPIER.Vector3(
                        i + bottom_left_m[0],
                        0,
                        j + bottom_left_m[1],
                    )
                );
                this.world.createCollider(RAPIER.ColliderDesc.cuboid(0.5, 0.5, 0.5), body);
                cubes.push(body);
            }
        }
        this.cubes = cubes;
    }

    player_advance(camera) {
        const x_vel = -Physics.player_speed * Math.sin(camera.rotation.y);
        const z_vel = -Physics.player_speed * Math.cos(camera.rotation.y);
        const old_vel = this.camera_body.linvel();
        this.camera_body.setLinvel({x: x_vel, y: old_vel.y, z: z_vel}, true);
    }

    player_back(camera) {
        const x_vel = Physics.player_speed * Math.sin(camera.rotation.y);
        const z_vel = Physics.player_speed * Math.cos(camera.rotation.y);
        const old_vel = this.camera_body.linvel();
        this.camera_body.setLinvel({x: x_vel, y: old_vel.y, z: z_vel}, true);
    }

    player_left(camera) {
        const x_vel = -Physics.player_speed * Math.cos(camera.rotation.y);
        const z_vel = Physics.player_speed * Math.sin(camera.rotation.y);
        const old_vel = this.camera_body.linvel();
        this.camera_body.setLinvel({x: x_vel, y: old_vel.y, z: z_vel}, true);
    }

    player_right(camera) {
        const x_vel = Physics.player_speed * Math.cos(camera.rotation.y);
        const z_vel = -Physics.player_speed * Math.sin(camera.rotation.y);
        const old_vel = this.camera_body.linvel();
        this.camera_body.setLinvel({x: x_vel, y: old_vel.y, z: z_vel}, true);
    }

    player_jump() {
        const old_vel = this.camera_body.linvel();
        this.camera_body.setLinvel({x: old_vel.x, y: 10, z: old_vel.z}, true);
    }
}

Physics.player_speed = 5;

export default Physics;
