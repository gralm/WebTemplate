import React from 'react';
import {RestService} from "../services/RestService";
import {SocketService} from "../services/SocketService";
import {IP_ADDRESS} from "../services/Properties";
import {DbPost} from "../model/DbPosts";
import {DbTableComponent} from "./DbTable";

export interface IProps {
    car: string
}
export interface IState {
    socketService: SocketService | undefined;
    restService: RestService;
    restMessage: string;
    socketMessage: string;
    socketOutputMessage: string;
    dbPosts: DbPost[]
}

export class FirstComponent extends React.Component<IProps, IState>  {

    inputElement = null;

    constructor(props: IProps) {
        super(props);
        this.state = {
            restService: new RestService("http://" + IP_ADDRESS + ":8080/"),
            socketService: undefined,
            restMessage: "-",
            socketMessage: "-",
            socketOutputMessage: "send something to backend",
            dbPosts: []
        };
        this.rest = this.rest.bind(this);
        this.testDB = this.testDB.bind(this);
        this.isActive = this.isActive.bind(this);
        this.sendSocketMessage = this.sendSocketMessage.bind(this);
        this.connectToWebsocket = this.connectToWebsocket.bind(this);
    }

    rest(): void {
        this.state.restService.post("postmessage", this.state.socketOutputMessage, (response: any) => {
            this.setState({restMessage: response})
        })
    }

    testDB(): void {
        this.state.restService.post("testdb", this.state.socketOutputMessage, (response: string) => {
            this.setState({dbPosts: JSON.parse(response)})
        })
    }

    sendSocketMessage(): void {
        this.state.socketService?.sendMessage(this.state.socketOutputMessage);
    }

    connectToWebsocket(): void {
        let newSocketService: SocketService = new SocketService();
        this.setState({socketService: newSocketService});

        newSocketService.addListener(
            (message: string) => this.setState({socketMessage: message})
        );
    }

    isActive(): boolean {
        return this.state.socketService !== undefined && this.state.socketService.isActive();
    }

    render() {
        return (<div>
            <table>
                <thead></thead>
                <tbody>
                    <tr>
                        <td>Active: </td>
                        <td className={this.isActive() ? "Active": "InActive"}>###</td>
                    </tr>
                    <tr>
                        <td>Post message: </td>
                        <td>{this.state.restMessage}</td>
                    </tr>
                    <tr>
                        <td>Websockets message: </td>
                        <td>{this.state.socketMessage}</td>
                    </tr>
                    <tr>
                        <td><button onClick={this.sendSocketMessage}>Send socket message:</button></td>

                        <td><input
                            value={this.state.socketOutputMessage}
                            onChange={event => this.setState({socketOutputMessage: event.target.value})}
                        /></td>
                    </tr>
                </tbody>
            </table>
            <button onClick={this.rest}>Send POST</button>
            <button onClick={this.testDB}>Update DB</button>
            <button onClick={this.connectToWebsocket}>Connect to websockets:</button><br />
            <DbTableComponent dbPosts={this.state.dbPosts}></DbTableComponent>
        </div>);
    }
}
