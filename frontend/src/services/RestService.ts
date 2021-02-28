
export class RestService {
    url: string = "asdf";
    xhttp: XMLHttpRequest;

    constructor(url: string) {
        this.url = url;
        this.xhttp = new XMLHttpRequest();
        this.xhttp.onreadystatechange = function (this: XMLHttpRequest, ev: Event) {
            console.log(this.readyState + ", " +  this.status);
            if (this.readyState == 4 && this.status == 200) {
                console.log("Kom in hit");
                console.log(this.response)
                console.log(this.responseText)
            }
        }
    }

    post(body: string) {
        const url: string = "http://localhost:8080/asdf";
        this.xhttp.open("POST", url);
        this.xhttp.send(body);
    }

    get(body: string) {
        const url: string = "http://localhost:8080/greeting";
        console.log("Borde använda body: " + body);
        this.xhttp.open("GET", url);
        this.xhttp.send();
    }

    getUrl(): string {
        return this.url;
    }
}