import React from 'react';
import {RestService} from "../services/RestService";
import {SocketService} from "../services/SocketService";
import {ADDRESS_PORT, HTTP} from "../services/Properties";
import {DbPost} from "../model/DbPosts";
import {DbTableComponent} from "./DbTable";
import {Print} from "./Print";
import {removeAll} from "../services/CookieService";

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
            restService: new RestService(),
            socketService: undefined,
            restMessage: "-",
            socketMessage: "-",
            socketOutputMessage: "send something to backend",
            dbPosts: []
        };
        this.rest = this.rest.bind(this);
        this.restCmd = this.restCmd.bind(this);
        this.testDB = this.testDB.bind(this);
        this.isActive = this.isActive.bind(this);
        this.sendSocketMessage = this.sendSocketMessage.bind(this);
        this.connectToWebsocket = this.connectToWebsocket.bind(this);
        this.disconnectToWebsocket = this.disconnectToWebsocket.bind(this);
        this.removeCookies = this.removeCookies.bind(this);
    }

    rest(): void {
        this.state.restService.post("postmessage", this.state.socketOutputMessage, (response: any) => {
            this.setState({restMessage: response})
        })
    }

    restCmd(): void  {
        console.log('restCmd');
        this.state.restService.post("api/hej", 'message', (response => {
            console.log('response: ' + response);
        }))
    }

    testDB(): void {
        this.state.restService.post("testdb", this.state.socketOutputMessage, (response: string) => {
            this.setState({dbPosts: JSON.parse(response)});
        })
    }

    removeCookies(): void {
        removeAll();
    }

    sendSocketMessage(): void {
        this.state.socketService?.sendMessage(this.state.socketOutputMessage);
    }

    connectToWebsocket(): void {
        let newSocketService: SocketService = new SocketService();
        this.setState({socketService: newSocketService});

        newSocketService.addListener(
            (message: string) => {
                console.log("Fick socket message: " + message);
                this.setState({socketMessage: message});
            }
        );
    }

    disconnectToWebsocket(): void {
        this.state?.socketService?.closeSocketConnection();
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
            <button onClick={this.restCmd}>Send POST with command</button>
            <button onClick={this.rest}>Send POST</button>
            <button onClick={this.testDB}>Update DB</button>
            <button onClick={this.removeCookies}>Remove All Cookies</button>
            <button onClick={this.connectToWebsocket}>Connect to websockets:</button>
            <button onClick={this.disconnectToWebsocket}>Disconnect to websockets:</button><br />
            <DbTableComponent dbPosts={this.state.dbPosts}></DbTableComponent>
            <Print></Print>
        </div>);
    }
}
