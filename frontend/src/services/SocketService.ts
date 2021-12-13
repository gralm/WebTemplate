import {IP_ADDRESS, PORT, WS} from "./Properties";
import {print} from "./PrintFile";

export class SocketService {
    ws: WebSocket;
    listeners: ((message: string) => void)[] = []
    active: boolean = false

    constructor() {
        const address: string = WS + '://' + IP_ADDRESS + ':' + PORT + '/user';
        print('connecting: "' + address + '"')
        this.ws = new WebSocket(address);

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
        }
    }

    addListener(listener: (message: string) => void) {
        this.listeners.push(listener);
    }

    sendMessage(message: string): void {
        this.ws.send(message);
    }

    isActive(): boolean {
        return this.active;
    }
}