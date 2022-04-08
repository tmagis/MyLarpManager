import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import * as fas from "@fortawesome/free-solid-svg-icons";
import {useContext, useEffect} from "react";
import TokenContext from "../context/TokenContext";
import Api from "../../api/api";
import "./UserMenu.css";
import {Link} from "react-router-dom";
import UserContext from "../context/UserContext";

export default function UserMenu() {

    const [token, setToken] = useContext(TokenContext);
    const [user, setUser] = useContext(UserContext);

    useEffect(() => {
        if(token) {
            Api.Auth.WhoAmI(token)
                .then(r => setUser(r.data))
                .catch(e => {
                    if(e.response && e.response.status === 403) {
                        setToken(null)
                    }
                })
        }
    }, [token]); // eslint-disable-line react-hooks/exhaustive-deps

    if(token) {
        return (
            <li className="nav-item dropdown with-arrow">
                <button className="btn btn-link nav-link" data-toggle="dropdown" id="user-dropdown-toggle">
                    {user && user.username}
                    <FontAwesomeIcon icon={fas.faAngleDown} className="ml-5"/>
                </button>
                <div className="dropdown-menu dropdown-menu-right" aria-labelledby="user-dropdown-toggle">
                    <a href="#" className="dropdown-item">Change password</a>
                    <Link to="/logout" className="dropdown-item">Logout</Link>
                </div>
            </li>
        )
    } else {
        return null;
    }

}