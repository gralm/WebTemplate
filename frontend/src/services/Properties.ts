const USE_HTTPS: boolean = true;

export const WS: string = USE_HTTPS ? "wss" : "ws";
export const HTTP: string = USE_HTTPS ? "https" : "http";
export const IP_ADDRESS: string = false ? "localhost" : "myServer.com";
export const PORT: string = USE_HTTPS ? "443" : "8080";

export const PRINT_TO_CONSOLE_LOG = false;