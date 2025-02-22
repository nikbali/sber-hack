import * as axios from "axios";

const instance = axios.create({
    baseURL: 'http://172.30.13.86:8082/',
    timeout: 50000,
    headers:     {
    }
});


export const DatabaseAPI = {

    getDatabaseInfo : () => {
        return instance.get(`getDatabaseInfo`)
            .then(response => {
                return response.data;
            });
    },

    getLastOperations : () => {
        return instance.get(`getLastOperations`)
            .then(response => {
                return response.data;
            });
    },

    start : () => {
        return instance.get(`start`)
            .then(response => {
                return response.data;
            });
    },

    end : () => {
        return instance.get(`end`)
            .then(response => {
                return response.data;
            });
    },

    download : () => {
        return instance.get(`download`)
            .then(response => {
                return response.data;
            });
    }
};




