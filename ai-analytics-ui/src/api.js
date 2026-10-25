export async function apiRequest(path, options) {
    const response = await fetch(path, options);
    const body = await response.json();

    if (!response.ok || body?.success === false) {
        throw new Error(body?.error || body?.message || `Request failed (${response.status})`);
    }

    return body;
}
