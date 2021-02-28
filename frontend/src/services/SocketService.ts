
export class SocketService {
    ws: WebSocket;

    constructor(url: string) {
        //this.ws = new WebSocket('ws://localhost:8080/user');
        // this.ws = new WebSocket('ws://localhost:8080/user', 'echo-protocol');
        console.log('Före')
        this.ws = new WebSocket('ws://localhost:8080/user');
        console.log('Efter')
        //  ((this: WebSocket, ev: MessageEvent) => any) | null;
        this.ws.onmessage = (ev: MessageEvent): void => {
            console.log("onmessage")
            console.log("data.data = " + JSON.stringify(ev.data))
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


}