import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import * as fas from '@fortawesome/free-solid-svg-icons';
import {Link} from "react-router-dom";
import UserMenu from "./UserMenu";
import {useContext} from "react";
import TokenContext from "../context/TokenContext";

const halfmoon = require("halfmoon");

export default function NavBar() {

    const [token] = useContext(TokenContext);

    return (
        <nav className="navbar">
            {token &&
            <div className="navbar-content">
                <button className="btn" type="button" onClick={() => {
                    halfmoon.toggleSidebar()
                }}>
                    <FontAwesomeIcon icon={fas.faBars}/>
                </button>
            </div>
            }
            <Link to="/" className="navbar-brand">
                <img src="/img/logo.png" alt="Logo"/>
                Warden
            </Link>
            <span className="navbar-text ml-5">
                <span className="badge text-monospace">v0.0.1-dev</span>
            </span>
            <ul className="navbar-nav ml-auto">
                <UserMenu />
            </ul>
        </nav>
    );
}
