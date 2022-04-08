import {NavLink} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";

export default function SideBarLink(props) {
    return (
        <NavLink to={props.to} className="sidebar-link sidebar-link-with-icon">
                    <span className="sidebar-icon">
                        <FontAwesomeIcon icon={props.icon} />
                    </span>
            {props.text}
        </NavLink>
    )
}