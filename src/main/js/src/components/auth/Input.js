import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';

export default function Input(props) {
    return (
        <div className="form-group input-group">
            <div className="input-group-prepend">
                <span className="input-group-text">
                    <FontAwesomeIcon icon={props.icon} />
                </span>
            </div>
            <label className="sr-only" htmlFor={props.id}>{props.label}</label>
            <input id={props.id} name={props.name} type={props.type} className="form-control" placeholder={props.label} required="required" onChange={props.onChange} disabled={props.disabled} autoComplete="off" />
        </div>
    );
}