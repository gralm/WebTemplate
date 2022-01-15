const USE_HTTPS: boolean = false;
const USE_LOCAL: boolean = true;

export const WS: string = USE_HTTPS ? "wss" : "ws";
export const HTTP: string = USE_HTTPS ? "https" : "http";
export const IP_ADDRESS: string = USE_LOCAL ? "localhost" : "myServer.com";
export const PORT: string = USE_HTTPS ? "443" : "8081";

export const PRINT_TO_CONSOLE_LOG = false;