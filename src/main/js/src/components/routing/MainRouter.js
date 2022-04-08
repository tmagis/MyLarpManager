import {Routes, Route} from "react-router-dom";
import Dashboard from "../../pages/Dashboard";
import Login from "../../pages/Login";
import Private from "./Private";
import Logout from "../../pages/Logout";

export default function MainRouter() {
    return (
        <Routes>
            <Route index element={<Private><Dashboard/></Private>} />
            <Route path="/login" element={<Login/>} />
            <Route path="/logout" element={<Private><Logout/></Private>} />
            <Route path="/users" element={<Private>Users</Private>} />
            <Route path="/eyes" element={<Private>Eyes</Private>} />
            <Route path="/recordings" element={<Private>Recordings</Private>} />
            <Route path="/timeline" element={<Private>Timeline</Private>} />
            <Route path="/settings" element={<Private>Settings</Private>} />
        </Routes>
    );
}