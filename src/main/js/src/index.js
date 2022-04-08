import React from 'react';
import ReactDOM from 'react-dom';
import App from './components/App';
import "halfmoon/css/halfmoon-variables.min.css";
import {HashRouter} from "react-router-dom";
import {HelmetProvider} from "react-helmet-async";

const halfmoon = require("halfmoon");

ReactDOM.render(
    <React.StrictMode>
        <HelmetProvider>
            <HashRouter>
                <App/>
            </HashRouter>
        </HelmetProvider>
    </React.StrictMode>,
    document.getElementById('root')
);
halfmoon.onDOMContentLoaded();
