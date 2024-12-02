<script>
    import { account } from "./Account.svelte"
    import { requireLogin } from "../lib/RequireLogin.svelte";
    //requireLogin($account);

    /** @param {Event} event */
    function post(event) {
        event.preventDefault();
        fetch("/api/surveys", {
            method: "POST",
            mode: "same-origin",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)})
        return false;
    }

    let data = {
        title: "survey",
        author: $account.id,
        polls: [{
            title: "",
            options: [ { text: ""}]
        }]
    }

    /**
     * @Param {Number} poll_id
     * @Param {Event} event
     */
    function pollInput(poll_id, event) {
        data.polls[poll_id].title = event.target.value.trim();
    }

    /**
     * @Param {Number} poll_id
     * @Param {Event} event
     */
    function pollChange(poll_id, event) {
        data.polls[poll_id].title = event.target.value.trim();
    }

    /**
     * @Param {Number} poll_id
     * @Param {Number} option_id
     * @Param {Event} event
     */
    function optionInput(poll_id, option_id, event) {
        data.polls[poll_id].options[option_id].text = event.target.value.trim();

        if (option_id === data.polls[poll_id].options.length - 1 && event.target.value !== "")
            data.polls[poll_id].options = [...data.polls[poll_id].options, { text: "" }];
    }

    /**
     * @Param {Number} poll_id
     * @Param {Number} option_id
     * @Param {Event} event
     */
    function optionChange(poll_id, option_id, event) {
        data.polls[poll_id].options[option_id].text = event.target.value.trim();

        if (option_id !== data.polls[poll_id].options.length - 1 && event.target.value === "") {
            data.polls[poll_id].options.splice(option_id, 1);
            data.polls[poll_id].options = data.polls[poll_id].options;
        }
    }

</script>

{#if $account.authenticated || true}
    <div class="form">
        <h1>New Poll</h1>

        <form class="wide" on:submit={post}>
            <label for="poll_{0}">Question</label>
            <input id="poll_{0}" type="text" placeholder="What is your favorite color?"
                   on:input={(event) => pollInput(0, event)}
                   on:change={(event) => pollChange(0, event)}>

            {#each data.polls[0].options as option, option_id}
                <label for="option_{0}_{option_id}">Option {option_id + 1}</label>
                <input id="option_{0}_{option_id}" type="text" placeholder="Blue"
                       on:input={(event) => optionInput(0, option_id, event)}
                       on:change={(event) => optionChange(0, option_id, event)}>
            {/each}

            <button>Create</button>
        </form>
    </div>
{:else}
    <h1>Not logged in!</h1>
    <p class="message">Redirecting to login page</p>
{/if}