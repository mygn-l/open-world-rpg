import * as THREE from "three";

const scene = new THREE.Scene();

(async function () {
    let response = await fetch("http://localhost:8080/get-farsight-map");
    const farsight_heights = await response.json();

    response = await fetch("http://localhost:8080/get-nearsight-map");
    const nearsight_heights = await response.json();

    const farsight_size = 598;
    const farsight_vertices = [];
    const farsight_indices = [];
    for (let z = 0; z <= farsight_size; z++) {
        for (let x = 0; x <= farsight_size; x++) {
            farsight_vertices.push(x * 10, farsight_heights[x][z], z * 10);
        }
    }
    for (let z = 0; z <= farsight_size - 1; z++) {
        for (let x = 0; x <= farsight_size - 1; x++) {
            const i = x + z * (farsight_size + 1);

            farsight_indices.push(i + 1, i, i + farsight_size + 1);
            farsight_indices.push(i + 1, i + farsight_size + 1, i + farsight_size + 2);
        }
    }

    const nearsight_size = 98;
    const nearsight_vertices = [];
    const nearsight_indices = [];
    for (let z = 0; z <= nearsight_size; z++) {
        for (let x = 0; x <= nearsight_size; x++) {
            nearsight_vertices.push(x + 2950, nearsight_heights[x][z], z + 2950);
        }
    }
    for (let z = 0; z <= nearsight_size - 1; z++) {
        for (let x = 0; x <= nearsight_size - 1; x++) {
            const i = x + z * (nearsight_size + 1);

            nearsight_indices.push(i + 1, i, i + nearsight_size + 1);
            nearsight_indices.push(i + 1, i + nearsight_size + 1, i + nearsight_size + 2);
        }
    }

    {
        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute("position", new THREE.Float32BufferAttribute(farsight_vertices, 3));
        geometry.setIndex(farsight_indices);
        geometry.computeVertexNormals();
        const material = new THREE.MeshStandardMaterial({
            flatShading: false,
        });
        const terrain = new THREE.Mesh(geometry, material);
        scene.add(terrain);
    }
    {
        const geometry = new THREE.BufferGeometry();
        geometry.setAttribute("position", new THREE.Float32BufferAttribute(nearsight_vertices, 3));
        geometry.setIndex(nearsight_indices);
        geometry.computeVertexNormals();
        const material = new THREE.MeshStandardMaterial({
            flatShading: false,
        });
        const terrain = new THREE.Mesh(geometry, material);
        scene.add(terrain);
    }

    render();
})();

const renderer = new THREE.WebGLRenderer({antialias: true});
renderer.setSize(1000, 700);
document.body.appendChild(renderer.domElement);

const fov = 70;
const aspect = 1000 / 700;
const near = 0.1;
const far = 10000;
const camera = new THREE.PerspectiveCamera(fov, aspect, near, far);
camera.position.y = 5;
camera.position.x = 0;
camera.position.z = 0;
camera.rotation.order = "YXZ";
camera.lookAt(200, 0, 200);

const light = new THREE.DirectionalLight(0xffffff, 1);
light.position.set(5, 10, -5);
scene.add(light);

scene.add(new THREE.AmbientLight(0xffffff, 0.4));

const render = function () {
    renderer.render(scene, camera);
};

window.addEventListener("keydown", function (e) {
    e.preventDefault();
    switch (e.key) {
        case "w":
            camera.position.z += 200;
            break;
        case "s":
            camera.position.z -= 200;
            break;
        case "a":
            camera.position.x += 200;
            break;
        case "d":
            camera.position.x -= 200;
            break;
        case "q":
            camera.position.y += 200;
            break;
        case "e":
            camera.position.y -= 200;
            break;
        case "ArrowLeft":
            camera.rotation.y += 0.2;
            break;
        case "ArrowRight":
            camera.rotation.y -= 0.2;
            break;
        case "ArrowDown":
            camera.rotation.x -= 0.2;
            break;
        case "ArrowUp":
            camera.rotation.x += 0.2;
            break;
    }
    requestAnimationFrame(render);
});

const socket = new WebSocket("ws://localhost:8080/ws");

socket.onmessage = (event) => {
    console.log("Server:", event.data);
};

socket.onopen = () => {
    socket.send(JSON.stringify({
        player: "Hero",
        skill: "TeleportAlly",
        target: "Mage"
    }));
};
