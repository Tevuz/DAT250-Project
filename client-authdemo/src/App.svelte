<script>
    import NotFound from "./pages/NotFound.svelte"
    import { views } from "./generated/Views.js"
    import { account } from "./pages/Account.svelte"

    let path = location.pathname;

    function refresh() {
        path = location.pathname;
    }

    const func = history.pushState;
    history.pushState = function() {
        func.apply(this, arguments);
        refresh();
    }
    window.addEventListener("popstate", refresh);

    /** @param {Event} event */
    function route(event) {
        event.preventDefault();
        history.pushState({}, null, this.href);
    }

</script>

<svelte:head>
    <link rel="icon" type="image/svg+xml" href="/assets/favicon.ico" />
</svelte:head>

<div class="app">

    <nav class="menu">
        <div>
            <ul>
                <li><div class="a-mimic"></div></li>
                <li><a href="/" on:click={route} class={path === "/" ? "selected" : ""}>Feed</a></li>
                <li><a href="/account" on:click={route} class={path === "/account" ? "selected" : ""}>Account</a></li>
                <li><a href="/login" on:click={route} class={path === "/login" ? "selected" : ""}>Log in</a></li>
                <li><a href="/logout" on:click={route} class={path === "/logout" ? "selected" : ""}>Log out</a></li>
                <li><div class="a-mimic"></div></li>
                <li><a href="/post" on:click={route} class={path === "/post" ? "selected" : ""}>New Poll</a></li>
            </ul>
        </div>
    </nav>

    <main class="content">
        {#if path in views}
            <svelte:component this={views[path]} />
        {:else}
            <NotFound />
        {/if}
    </main>
</div>