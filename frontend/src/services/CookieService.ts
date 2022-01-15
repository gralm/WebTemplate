

export function getCookie(name: string): string|undefined {
    let ca = document.cookie.split(';');
    for(let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name + "=") == 0) {
            return c.substring(name.length+1, c.length);
        }
    }
    return undefined;
}


export function getUserCookie(): string|undefined {
    return getCookie("uuid_name");
}