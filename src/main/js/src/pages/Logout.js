import {useContext, useEffect} from "react";
import TokenContext from "../components/context/TokenContext";

export default function Logout() {

    const [, setToken] = useContext(TokenContext);

    useEffect(() => {
        setToken(null);
    }, []); // eslint-disable-line react-hooks/exhaustive-deps

    return null;
}