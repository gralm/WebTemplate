const USE_HTTPS: boolean = false;
const USE_LOCAL: boolean = true;
const ADDRESS: string|undefined = "myweb.se"

export const WS: string = USE_HTTPS ? "wss" : "ws";
export const HTTP: string = USE_HTTPS ? "https" : "http";
export const IP_ADDRESS: string = USE_LOCAL ? "localhost" : "100.100.100.100";
export const PORT: string = USE_HTTPS ? (USE_LOCAL ? "8443" : "443") : (USE_LOCAL ? "8081": "80");
export const ADDRESS_PORT: string = (!USE_LOCAL && ADDRESS) ? (ADDRESS) : (IP_ADDRESS + ":" + PORT);


export const PRINT_TO_CONSOLE_LOG = false;