<script>
  import { fade } from 'svelte/transition';

    let request = fetch("/api/users", { redirect: "manual", mode: "same-origin" })
        .then(result => ({ status: result.status, location: result.url, users: result.json() }),
            result => ({ status: result.status, location: result.headers.get("Location"), users: []}));

    request.then(result => {
        console.log(result, Infinity)
        if (result.status === 401)
            history.pushState(null, null, "/login");
    })

</script>

<h1>Users</h1>

{#await request}
  <p>Loading ... </p>
{:then response}
  {#if response.status === 200}
    {#await response.users}
      <p>Loading ... </p>
    {:then users}
      <p>status: {response.status}</p>
      {#each users as user}
        <p>{user.username}</p>
      {/each}
    {/await}
  {:else}
    <p>Unavailable</p>
  {/if}
{/await}

