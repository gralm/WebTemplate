import React from "react";
import {prints} from "../services/PrintFile";

export interface IProps {
}

export interface IState {
}

export class Print extends React.Component<IProps, IState> {

    constructor(props: IProps) {
        super(props);
        this.state = {};
    }

    render() {
        return (<div>
            <p>Logout</p>
            {prints.map((value, index) => <p key={index}>{index}: {value}</p>)}
        </div>);
    }
}