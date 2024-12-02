<script>
    import { reload } from "./Account.svelte"

    /** @param {Event} event */
    function login(event) {
        event.preventDefault();
        fetch("/login", {
                method: "POST",
                redirect: "manual",
                mode: "same-origin",
                body: new FormData(this)})
            .then(updateUser)
            .then(redirect)
        return false;
    }

    /** @param {Response} result */
    function updateUser(result) {
        reload();
        return result;
    }

    /** @param {Response} result */
    function redirect(result) {
        history.pushState({}, null, history.state["referer"] ?? "/");
        return result;
    }

</script>

<div class="form">
    <h1>Log in</h1>

    <form on:submit={login}>
        <label for="username">Username</label>
        <input type="text" name="username" placeholder="example" value="user" required>
        <label for="password">Password</label>
        <input type="password" name="password" placeholder="password" value="password" required>
        <button>Log in</button>
    </form>

    <p>Log in with: <a href="/oauth2/authorization/github">Github</a></p>
</div>
