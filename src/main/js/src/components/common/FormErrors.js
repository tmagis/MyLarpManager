export default function FormErrors(props) {
    if(props.errors) {
        if(props.errors.length > 1) {
            return (
                <div className="invalid-feedback">
                    <ul>
                        {props.errors.map(error => {
                            return (
                                <li>{error}</li>
                            )
                        })}
                    </ul>
                </div>
            )
        } else {
            return (
                <div className="invalid-feedback">
                    {props.errors[0]}
                </div>
            )
        }
    } else {
        return null;
    }
}