import {IP_ADDRESS, PORT} from "./Properties";

export class SocketService {
    ws: WebSocket;
    listeners: ((message: string) => void)[] = []

    constructor() {
        this.ws = new WebSocket('ws://' + IP_ADDRESS + ':' + PORT + '/user');

        this.ws.onmessage = (ev: MessageEvent): void => {
            this.listeners.forEach(listener => {
                listener(ev.data);
            });
        }

        // onerror: ((this: WebSocket, ev: Event) => any) | null;
        this.ws.onerror = (ev: Event): void => {
            console.log("onerror: " + JSON.stringify(ev))
        }

        // onclose: ((this: WebSocket, ev: CloseEvent) => any) | null;
        this.ws.onclose = (ev: CloseEvent): void => {
            console.log("onclose: " + JSON.stringify(ev))
        }

        // onopen: ((this: WebSocket, ev: Event) => any) | null;
        this.ws.onopen = (ev: Event): void => {
            console.log("onopen: " + JSON.stringify(ev))
        }
    }

    addListener(listener: (message: string) => void) {
        this.listeners.push(listener);
    }

    sendMessage(message: string): void {
        this.ws.send(message);
    }

    isActive(): boolean {
        return this.ws !== undefined
    }

}