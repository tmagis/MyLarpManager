import TokenContext from "./context/TokenContext";
import MainPage from "./parts/MainPage";
import useToken from "../hooks/token";
import TokenRenewal from "./auth/TokenRenewal";
import {useState} from "react";
import UserContext from "./context/UserContext";

export default function App() {

    const [token, setToken] = useToken();
    const [user, setUser] = useState();

    return (
        <TokenContext.Provider value={[token, setToken]} >
            <UserContext.Provider value={[user, setUser]} >
                <MainPage />
                <TokenRenewal />
            </UserContext.Provider>
        </TokenContext.Provider>
    );
}
