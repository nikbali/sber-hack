import * as axios from "axios";

const instance = axios.create({
    baseURL: 'http://172.30.13.86:8082/',
    timeout: 10000,
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

    getLastTransactions : () => {
        return instance.get(`getLastTransactions`)
            .then(response => {
                debugger;
                return response.data;
            });
    }
};




