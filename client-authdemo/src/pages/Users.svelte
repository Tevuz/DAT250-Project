<script>
    let request = fetch("/api/users", { redirect: "manual", mode: "same-origin" })
        .then(handle);

    /** @param {Response} response */
    async function handle(response){
        if (response.status === 200) {
            return await response.json();
        } else {
            console.log("fetch /api/users { status: ", response.status, " }")
            history.pushState({ referer: "/users" }, null, "/login");
        }
    }
</script>

{#await request}
    <p>Loading ... </p>
{:then content}
    <p>{JSON.stringify(content)}</p>
{/await}