import {IP_ADDRESS, PORT, WS} from "./Properties";
import {print} from "./PrintFile";
import {getUserCookie} from "./CookieService";
import {SoMessage, SoType} from "../model/SoMessage";

export class SocketService {
    ws: WebSocket|undefined;
    listeners: ((message: string) => void)[] = []
    active: boolean = false

    constructor() {
        const address: string = WS + '://' + IP_ADDRESS + ':' + PORT + '/user';
        print('connecting: "' + address + '"')
        try {
            this.ws = new WebSocket(address);
        } catch (e) {
            console.log(e);
            console.log(JSON.stringify(e));
            return;
        }

        this.ws.onmessage = (ev: MessageEvent): void => {
            this.listeners.forEach(listener => {
                listener(ev.data);
            });
        }

        // onerror: ((this: WebSocket, ev: Event) => any) | null;
        this.ws.onerror = (ev: Event): void => {
            print("onerror: " + JSON.stringify(ev))
        }

        // onclose: ((this: WebSocket, ev: CloseEvent) => any) | null;
        this.ws.onclose = (ev: CloseEvent): void => {
            print("onclose: " + JSON.stringify(ev))
            this.active = false;
        }

        // onopen: ((this: WebSocket, ev: Event) => any) | null;
        this.ws.onopen = (ev: Event): void => {
            print("onopen: " + JSON.stringify(ev))
            this.active = true;
            print("update backend with cookie");
            this.sendMessage2({
                type: SoType.CONNECT_USER
            })
            print("updated");
        }
    }

    addListener(listener: (message: string) => void) {
        this.listeners.push(listener);
    }

    closeSocketConnection() {
        // TODO
        print("closeing socket connection");
        this.sendMessage2({
            type: SoType.DISCONNECT_USER,
        });
    }

    // Deprecated
    sendMessage(message: string): void {
        this.sendMessage2({
            type: SoType.MESSAGE,
            payload: message
        })
    }

    sendMessage2(soMessage: SoMessage): void {
        print('matchings: ' + getUserCookie())
        const jsonMessage = {
            type: soMessage.type,
            uuid: getUserCookie(),
            payload: soMessage.payload
        }
        print('jsonMessage: ' + JSON.stringify(jsonMessage))
        if (this.ws) {
            this.ws.send(JSON.stringify(jsonMessage));
        }
    }

    isActive(): boolean {
        return this.active;
    }
}