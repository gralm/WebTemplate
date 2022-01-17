

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

export function removeAll(): void {
    let cs = document.cookie.split(';');
    console.log(JSON.stringify(cs));
    document.cookie = "";
}

export function deleteCookies() {
    const allCookies: string[] = document.cookie.split(';');

    // The "expire" attribute of every cookie is
    // Set to "Thu, 01 Jan 1970 00:00:00 GMT"
    for (var i = 0; i < allCookies.length; i++) {
        document.cookie = allCookies[i] + "=;expires=" + new Date(0).toUTCString();
    }
}

export function getUserCookie(): string|undefined {
    return getCookie("uuid_name");
}