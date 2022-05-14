import axios from "axios";

const AuthApi = {
    WhoAmI: function(token) {
        return axios.get("/api/v1/auth/whoAmI", {
            headers: {
                Authorization: "Bearer " + token
            }
        });
    },
    Login: async function(credentials) {
        return axios.post("/api/v1/auth/login", credentials);
    },
    Renew: async function(token) {
        return axios.get("/api/v1/auth/renew", {
            headers: {
                Authorization: "Bearer " + token
            }
        });
    }
};

export default AuthApi;