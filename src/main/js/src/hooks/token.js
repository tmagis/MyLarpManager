import {useState} from "react";

export default function useToken() {

    const [token, setToken] = useState(localStorage.getItem("warden_token"));

    const saveToken = userToken => {
        if(userToken) {
            localStorage.setItem("warden_token", userToken);
        } else {
            localStorage.removeItem("warden_token");
        }
        setToken(userToken);
    }

    return [token, saveToken];
}