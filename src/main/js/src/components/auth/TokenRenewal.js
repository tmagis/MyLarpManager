import {useContext, useEffect} from "react";
import TokenContext from "../context/TokenContext";
import Api from "../../api/api";
import ReactInterval from "react-interval";

export default function TokenRenewal() {

    const [token, setToken] = useContext(TokenContext)

    const renewToken = () => {
        if(token) {
            Api.Auth.Renew(token)
                .then(r => setToken(r.data.token))
                .catch(e => {
                    if(e.response && e.response.status === 403) {
                        setToken(null)
                    }
                })
        }
    }

    useEffect(renewToken, []) // eslint-disable-line react-hooks/exhaustive-deps

    return <ReactInterval timeout={1000 * 60 * 10} callback={renewToken} enabled={!!token} />;
}