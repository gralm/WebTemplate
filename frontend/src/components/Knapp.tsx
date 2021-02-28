import React from 'react';
import {RestService} from "../services/RestService";
import {SocketService} from "../services/SocketService";

export interface IProps {
    car: string
}
export interface IState {
    car: string
}

export class Knapp extends React.Component<IProps, IState>  {
    restService: RestService
    socketService: SocketService | undefined

    constructor(props: IProps) {
        super(props);
        this.state = {car: props.car};
        console.log("Kör konstruktorn")
        this.restService = new RestService("asdf");
        this.rest = this.rest.bind(this);
        this.socket = this.socket.bind(this);
        this.socketService = undefined;
    }

    rest(): void {
        console.log("Klickar här 2");
        this.restService.post("asdf");
    }

    socket(): void {
        console.log("Klickar Socke t");
        this.socketService = new SocketService("url");
    }

    render() {
        return (<div>
            <h2 onClick={this.rest}>Hi, I am a Car!{this.props.car}</h2>
            <h2 onClick={this.socket}>Hi, I am a Socket!</h2>
        </div>);
    }
}
