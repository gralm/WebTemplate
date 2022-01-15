export interface SoMessage {
    type: SoType;
    payload?: string;
}

// Corresponds to SiType.java
export enum SoType {
    MESSAGE = "MESSAGE",
    CONNECT_USER = "CONNECT_USER",
    DISCONNECT_USER = "DISCONNECT_USER",
}