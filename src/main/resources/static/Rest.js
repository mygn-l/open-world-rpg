class Rest{
    constructor() {}
    async get_farsight_map() {
        const response = await fetch("http://localhost:8080/get-farsight-map");
        return await response.json();
    }
    async get_nearsight_map() {
        const response = await fetch("http://localhost:8080/get-nearsight-map");
        return await response.json();
    }
}

export default Rest;
