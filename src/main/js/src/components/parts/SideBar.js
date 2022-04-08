import * as fas from '@fortawesome/free-solid-svg-icons';
import SideBarLink from "./SideBarLink";

export default function SideBar() {
    return (
        <div className="sidebar">
            <div className="sidebar-menu">
                <h5 className="sidebar-title">Monitoring</h5>
                <div className="sidebar-divider" />
                <SideBarLink to="/" text="Dashboard" icon={fas.faColumns} />
                <SideBarLink to="/recordings" text="Recordings" icon={fas.faFilm} />
                <SideBarLink to="/timeline" text="Timeline" icon={fas.faClock} />
                <br/>
                <h5 className="sidebar-title">Configuration</h5>
                <div className="sidebar-divider" />
                <SideBarLink to="/eyes" text="Eyes" icon={fas.faEye} />
                <SideBarLink to="/users" text="Users" icon={fas.faUsers} />
                <SideBarLink to="/settings" text="Settings" icon={fas.faCogs} />
            </div>
        </div>
    );
}
