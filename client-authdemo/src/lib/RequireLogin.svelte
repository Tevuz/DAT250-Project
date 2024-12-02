<script context="module">
    import { onMount } from "svelte";

    export function requireLogin(account) {
        onMount(() => {
            let timeout = setTimeout(() => {
                    if (!account.authenticated)
                        history.pushState({ referer: "/account" }, null, "/login");
                }, 500);
            return () => clearTimeout(timeout);
        });
    }
</script>