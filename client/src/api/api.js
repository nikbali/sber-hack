import * as axios from "axios";

const instance = axios.create({
    baseURL: 'http://localhost:8082/',
    headers:     {
    }
});


export const DatabaseAPI = {

    getDatabaseInfo() {
        return instance.get(`getDatabaseInfo`)
            .then(response => {
                debugger;
                return response.data;
            });
    }
};




