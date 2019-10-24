import * as axios from "axios";

const instance = axios.create({
    withCredentials: true,
    baseURL: 'https://localhost:8080/',
    headers:     {
    }
});


export const DatabaseAPI = {
    getDatabaseInfo() {
        return instance.get(`getDatabaseInfo`)
            .then(response => {
                return response.data;
            });
    }
};




