<script>
    import NotFound from "./pages/NotFound.svelte"
    import { views } from "./generated/Views.js"

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
    function navigate(event) {
        event.preventDefault();
        history.pushState(null, null, this.href);
    }

</script>

<header class="nav-head">
    <div class="nav-wrap">
        <a class="logo" href="/" on:click={navigate} aria-label="Poll app"></a>
        <nav>
            <span class="nav-left">
                <a href="/users" on:click={navigate}>Users</a>
            </span>
            <span class="nav-right">
                <a href="/login" on:click={navigate}>Login</a>
                <a href="/logout" on:click={navigate}>Logout</a>
            </span>
        </nav>
    </div>
</header>

<main>
    <div class="content-wrap">
        {#if path in views}
            <svelte:component this={views[path]} />
        {:else}
            <NotFound />
        {/if}
    </div>
</main>