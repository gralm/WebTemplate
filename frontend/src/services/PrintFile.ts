import {PRINT_TO_CONSOLE_LOG} from "./Properties";

export let prints: string[] = []

export function print(value: string): void {
    if (PRINT_TO_CONSOLE_LOG) {
        console.log(value);
    } else {
        prints.push(value);
        console.log(value);
    }
}