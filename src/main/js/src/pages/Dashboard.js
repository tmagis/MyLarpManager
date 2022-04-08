import {Helmet} from "react-helmet-async";

export default function Dashboard() {

    return (
        <div className="content-wrapper">
            <Helmet>
                <title>Dashboard - Warden</title>
            </Helmet>
            This is the dashboard
        </div>
    );
}