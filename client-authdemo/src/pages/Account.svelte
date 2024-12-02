<script context="module">
    import { writable } from "svelte/store";

    export const account = writable({ authenticated: false, id: 0, username: ""});
    const trigger = writable(0);
    trigger.subscribe(_ => { fetch("/account/user").then(handle); })

    export function reload() {
        trigger.update(v => v + 1);
    }
    reload()

    /** @param {Response} response */
    async function handle(response) {
        console.log(response.status, response.body)

        if (response.status === 200 && response.type !== "opaqueredirect" && response.type !== "opaque") {
            let content = await response.json();
            console.log(content);
            account.set({ authenticated: true, id: content["id"], username: content["username"] });
        } else {
            account.set({ authenticated: false, id: 0, username: ""});
        }
    }

</script>

<script>
    import { requireLogin } from "../lib/RequireLogin.svelte";
    reload();
    requireLogin($account);

    let votes;
    if ($account.authenticated)
        votes = fetch("/api/users/" + $account.username + "/votes").then(response => response.json());

</script>

{#if $account.authenticated}
    <h1>Account: {$account.username}</h1>
    {#await votes}
        <p>Loading votes ...</p>
    {:then votes_}
        {JSON.stringify(votes_)}
    {/await}

{:else}
    <h1>Not logged in!</h1>
    <p class="message">Redirecting to login page</p>
{/if}