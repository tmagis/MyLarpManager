export default function Input(props) {
    return (
        <div className="form-group">
            <label htmlFor={props.id} className={props.required ? "required" : ""}>{props.label}</label>
            <input id={props.id} type={props.type} className="form-control" name={props.name}/>
            {props.hint && <div className="form-text">
                {props.hint}
            </div>}
        </div>
    );
}