<script>
    import {onMount} from "svelte";
    import {account} from "./Account.svelte";

    let trigger = 1;
    let indices = new Set();
    let data = new Map();

    onMount(() => {
        const interval = setInterval(load, 2000);
        return () => clearInterval(interval);
    })

    async function load_mock() {
        indices.push(1);
        data[1] = {
            id: 1,
            title: "survey",
            polls: [
                {
                    id: 1,
                    title: "Poll",
                    options: [
                        {
                            id: 1,
                            text: "Option 1"
                        },
                        {
                            id: 2,
                            text: "Option 2"
                        }
                    ]
                }
            ]
        }
    }

    async function load() {
        let response = await fetch("/api/surveys");
        if (response.status !== 200)
            return
        let content = await response.json();
        for (let survey of content) {
            indices.add(survey.id)
            data.set(survey.id, survey);
        }
        trigger = trigger + 1;
    }

    /**
     * @Param {Number} survey_id
     * @param {Event} event
     * */
    async function vote(survey_id, event) {
        event.preventDefault();
        let vote = {
            user_id: $account.id,
            survey_id: survey_id,
            options: formdata[survey_id]
        }

        let body = JSON.stringify(vote);
        console.log(body);

        await fetch("/api/votes", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            mode: "same-origin",
            body: body})
        let response = await fetch("/api/surveys/" + survey_id, {
            method: "GET",
            mode: "same-origin"
        })
        if (response.status !== 200)
            return;

        data.set(survey_id, await response.json())
        trigger = trigger + 1;

        return false;
    }

    let formdata = {};

    /**
     * @Param {Number} survey_id
     * @Param {Number} option_id
     * @param {Event} event
     */
    function checkboxChange(survey_id, option_id, event) {
        if (!formdata[survey_id]) {
            formdata[survey_id] = []
        }
        if (event.target.checked) {
            formdata[survey_id].push(option_id)
        } else {
            let index = formdata[survey_id].indexOf(option_id)
            formdata[survey_id].splice(index, 1);
        }
    }

</script>

<h1>Feed</h1>

<div class="feed">
    {#key trigger}
        {#each indices as index}
            {@const survey = data.get(index)}
            {@const voted = !!(data.get(index).vote_total)}
            <div class="survey">
                {#each survey.polls as poll}
                    <form on:submit={(event) => vote(survey.id, event)}>
                        <h2>{poll.title}</h2>
                        <ul>
                            {#each poll.options as option}
                                {@const progress = option.vote_count*100 / data.get(index).vote_total}
                                <li style="--progress: {progress}%">
                                    {#if voted}
                                        <input type="checkbox" id="option_{option.id}" disabled>
                                    {:else}
                                        <input type="checkbox" id="option_{option.id}" on:change={(event) => checkboxChange(survey.id, option.id, event)}>
                                    {/if}
                                    <label for="option_{option.id}">{option.text}</label>
                                    <span></span>
                                </li>
                            {/each}
                        </ul>
                        <button>Vote</button>
                    </form>
                {/each}
            </div>
        {/each}
    {/key}
</div>