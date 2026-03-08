import * as THREE from "three";
import Config from "./Config.js";

class Renderer {
    constructor(append_to) {
        const scene = new THREE.Scene();

        const renderer = new THREE.WebGLRenderer({antialias: true});
        renderer.setSize(Renderer.canvas_width, Renderer.canvas_height);
        append_to.appendChild(renderer.domElement);

        const camera = new THREE.PerspectiveCamera(Renderer.fov, Renderer.aspect, Renderer.near, Renderer.far);
        camera.rotation.order = "YXZ";

        const light = new THREE.DirectionalLight(0xffffff, 0.6);
        light.position.set(1, 2, -1);
        scene.add(light);

        scene.add(new THREE.AmbientLight(0xffffff, 0.4));

        const cubes = [];
        for (let i = 0; i < Config.nearsight_range; i++) {
            for (let j = 0; j < Config.nearsight_range; j++) {
                const geometry = new THREE.BoxGeometry(1, 1, 1);
                const material = new THREE.MeshBasicMaterial({color: 0x00ff00});
                const cube = new THREE.Mesh(geometry, material);
                scene.add(cube);
                cubes.push(cube);
            }
        }

        this.scene = scene;
        this.renderer = renderer;
        this.camera = camera;
        this.cubes = cubes;
    }

    add_farsight(heights, bottom_left_m) {
        //farsight heightmap is in 10m stride flat coordinate, but meter precision vertically (height)
        const farsight_vertices = [];
        const farsight_indices = [];
        for (let x = 0; x < Config.farsight_size; x++) {
            for (let z = 0; z < Config.farsight_size; z++) {
                farsight_vertices.push(x * 10 + bottom_left_m[0], heights[x][z], z * 10 + bottom_left_m[1]);
            }
        }
        for (let x = 0; x < Config.farsight_size - 1; x++) {
            for (let z = 0; z < Config.farsight_size - 1; z++) {
                const i = x * Config.farsight_size + z;

                farsight_indices.push(i, i + 1, i + Config.farsight_size);
                farsight_indices.push(i + 1, i + 1 + Config.farsight_size, i + Config.farsight_size);
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute("position", new THREE.Float32BufferAttribute(farsight_vertices, 3));
        geometry.setIndex(farsight_indices);
        geometry.computeVertexNormals();

        const material = new THREE.MeshStandardMaterial({flatShading: false});

        const mesh = new THREE.Mesh(geometry, material);

        this.scene.add(mesh);
    }

    add_nearsight(heights, bottom_left_m) {
        const nearsight_vertices = [];
        const nearsight_indices = [];
        for (let x = 0; x < Config.nearsight_size; x++) {
            for (let z = 0; z < Config.nearsight_size; z++) {
                nearsight_vertices.push(x + bottom_left_m[0], heights[x][z], z + bottom_left_m[1]);
            }
        }
        for (let x = 0; x < Config.nearsight_size - 1; x++) {
            for (let z = 0; z < Config.nearsight_size - 1; z++) {
                const i = x * Config.nearsight_size + z;

                nearsight_indices.push(i, i + 1, i + Config.nearsight_size);
                nearsight_indices.push(i + 1, i + 1 + Config.nearsight_size, i + Config.nearsight_size);
            }
        }

        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute("position", new THREE.Float32BufferAttribute(nearsight_vertices, 3));
        geometry.setIndex(nearsight_indices);
        geometry.computeVertexNormals();

        const material = new THREE.MeshStandardMaterial({flatShading: false});

        const mesh = new THREE.Mesh(geometry, material);

        this.scene.add(mesh);
    }

    render() {
        this.renderer.render(this.scene, this.camera);
    }
}

Renderer.canvas_width = 1000;
Renderer.canvas_height = 700;

Renderer.fov = 70;
Renderer.aspect = Renderer.canvas_width / Renderer.canvas_height;
Renderer.near = 0.1;
Renderer.far = 100000;

export default Renderer;
