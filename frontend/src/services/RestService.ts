import {HTTP, PORT} from "./Properties";
import {print} from "./PrintFile";

export class RestService {
    basicUrl: string;

    constructor(url: string) {
        this.basicUrl = url;
    }

    post(url: string, body: any, responseMethod: (response: any) => void): void {
        const fullUrl: string = this.basicUrl + url;
        print("Sending to url: " + fullUrl);
        print("HTTP = " + HTTP);
        print("PORT = " + PORT);
        let xhttp: XMLHttpRequest = new XMLHttpRequest();
        xhttp.onreadystatechange = function (this: XMLHttpRequest, ev: Event) {
            if (this.readyState == 4 && this.status == 200) {
                responseMethod(this.response);
            } else if (this.status != 200) {
                print("Recieved bad response, status = " + this.status + ", readyState = " + this.readyState);
            }
        }
        xhttp.withCredentials = true;
        xhttp.open("POST", fullUrl);
        // xhttp.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xhttp.send(body);
    }

    get(body: string) {
        const url: string = this.basicUrl + "/greeting";
    }

    getUrl(): string {
        return "this.url";
    }
}