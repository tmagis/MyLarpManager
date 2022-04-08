import Input from "../components/auth/Input";
import * as fas from '@fortawesome/free-solid-svg-icons';
import {useContext, useState} from "react";
import {Navigate} from "react-router-dom";
import TokenContext from "../components/context/TokenContext";
import Api from "../api/api";
import {Helmet} from "react-helmet-async";
import FormErrors from "../components/common/FormErrors";

export default function Login() {
    const [username, setUsername] = useState();
    const [password, setPassword] = useState();
    const [token, setToken] = useContext(TokenContext);
    const [errors, setErrors] = useState({});
    const [disabled, setDisabled] = useState(false)

    const handleSubmit = e => {
        e.preventDefault();
        setDisabled(true);
        setErrors({})
        Api.Auth.Login({
            username,
            password
        }).then(response => {
            setToken(response.data.token)
        }).catch(e => {
            setToken(null)
            setErrors(e.response.data)
        }).finally(() => {
            setDisabled(false);
        })
    }

    if(token) {
        return <Navigate to="/" />
    }

    return (
        <div className="content-wrapper d-flex align-items-center">
            <Helmet>
                <title>Login - Warden</title>
            </Helmet>
            <div className="w-400 m-auto">
                <div className="card p-0">
                    <div className="px-card py-10 border-bottom">
                        <h2 className="card-title font-size-18 m-0">
                            Login
                        </h2>
                    </div>
                    <div className="content">
                        <form onSubmit={handleSubmit}>
                            <FormErrors errors={errors["globalErrors"]} />
                            <Input label="Your username" id="username" type="text" name="username" icon={fas.faUser} onChange={e => setUsername(e.target.value)} disabled={disabled} />
                            <Input label="Your password" id="password" type="password" name="password" icon={fas.faLock} onChange={e => setPassword(e.target.value)} disabled={disabled} />
                            <div className="text-right">
                                <input className="btn btn-primary" type="submit" value="Log in" disabled={disabled} />
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}