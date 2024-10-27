<script>
    export let pollId = 101;
    export let question = "Pineapple on Pizza: Is it ok?";
    export let responses = [
        { text: "Oh yammy!", votes: 2 },
        { text: "Mamma Mia! Hell no!", votes: 12 },
        { text: "IDK... I do not like pizza actually...", votes: 1 }
    ];

    // This flag will track whether the poll is created
    let pollCreated = false;

    function upvote(response) {
        response.votes += 1;
    }

    function downvote(response) {
        if (response.votes > 0) {
            response.votes -= 1;
        }
    }

    // Step 4: Fetch poll
    import { onMount } from "svelte";

    // Ensure the poll object is defined initially
    let poll = {
        question: '',
        options: []
    };

    // Fetch the poll data when the component is mounted
    onMount(async () => {
        try {
            const res = await fetch(`http://localhost:8080/poll/${question}`);
            if (res.ok) {
                const text = await res.text();  // Read the response as plain text first
                if (text) {
                    poll = JSON.parse(text);  // Parse only if there's content
                } else {
                    console.error("Response is empty");
                }
            } else {
                console.error("Failed to fetch poll");
            }
        } catch (error) {
            console.error("Error fetching poll:", error);
        }
    });



    // Step 4: Create a poll
    let newPoll = {
        question: "",
        options: []
    };

    // Temporary variable for holding new alternative
    let newOption = "";

    // Function to add a new voting alternative to the poll
    function addOption() {
        if (newOption.trim() !== "") {
            newPoll.options.push({
                text: newOption,
                votes: 0
            });
            newOption = ""; // Clear input field after adding
        }
    }

    async function createPoll() {
        try {
            const res = await fetch('http://localhost:8080/poll/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(newPoll)
            });
            if (res.ok) {
                console.log("Poll created successfully");
                pollCreated = true; // Set pollCreated to true when poll is created
            } else {
                console.error("Failed to create poll");
            }
        } catch (error) {
            console.error("Error creating poll:", error);
        }
    }

    // Step 4: Voting on a Poll Option
    async function vote(option, type) {
        const updatedOption = {
            ...option,
            votes: option.votes + (type === 'upvote' ? 1 : -1)
        };

        try {
            const res = await fetch(`http://localhost:8080/poll/vote`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(updatedOption)
            });
            if (res.ok) {
                console.log(`${type} successful`);
            } else {
                console.error("Vote failed");
            }
        } catch (error) {
            console.error("Error voting:", error);
        }
    }

</script>


<!-- Create Poll Section -->
{#if !pollCreated}
    <!-- Poll question input -->
    <input type="text" bind:value={newPoll.question} placeholder="Poll question">

    <!-- Voting alternatives section -->
    <input type="text" bind:value={newOption} placeholder="Add an option">
    <button on:click={addOption}>Add Option</button>

    <!-- Show the list of added alternatives -->
    <ul>
        {#each newPoll.options as option, index}
            <li>{option.text} <button on:click={() => newPoll.options.splice(index, 1)}>Remove</button></li>
        {/each}
    </ul>

    <!-- Button to create poll -->
    <button on:click={createPoll}>Create Poll</button>

{:else}
    <!-- Display the created poll and its voting options -->
    <div class="poll-container">
        <div class="poll-header">Poll#{pollId}</div>
        <div class="poll-question">"{newPoll.question}"</div>
        <ul class="response-list">
            {#each newPoll.options as response}
                <li class="response-item">
                    <span class="response-text">{response.text}</span>
                    <div class="vote-buttons">
                        <button class="vote-button upvote" on:click={() => upvote(response)}>upvote</button>
                        <button class="vote-button downvote" on:click={() => downvote(response)}>downvote</button>
                    </div>
                    <span class="vote-count">{response.votes} {response.votes === 1 ? 'Vote' : 'Votes'}</span>
                </li>
            {/each}
        </ul>
    </div>
{/if}

<style>
    /* General container styling */
    .poll-container {
        background-color: #333;
        border-radius: 8px;
        padding: 20px;
        width: 350px;
        margin: 0 auto;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.3);
        border: 1px solid #444;
    }

    /* Poll header (ID) */
    .poll-header {
        color: #fff;
        font-size: 1.2em;
        font-weight: bold;
        margin-bottom: 10px;
        text-align: center;
    }

    /* Poll question area */
    .poll-question {
        font-size: 1.1em;
        background-color: #ffebbb;
        padding: 10px;
        border-radius: 6px;
        color: #333;
        text-align: center;
        margin-bottom: 20px;
    }

    /* Response list */
    .response-list {
        list-style: none;
        padding: 0;
    }

    /* Styling each response item */
    .response-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px;
        background-color: #fff;
        border-radius: 6px;
        margin-bottom: 10px;
        box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
    }

    /* Text for each response */
    .response-text {
        flex-grow: 1;
        margin-right: 20px;
        color: #333;
    }

    /* Button area */
    .vote-buttons {
        display: flex;
        gap: 10px;
    }

    /* Buttons for upvote/downvote */
    .vote-button {
        border: none;
        padding: 6px 12px;
        border-radius: 4px;
        font-size: 0.9em;
        cursor: pointer;
        font-weight: bold;
    }

    .upvote {
        background-color: #4CAF50;
        color: white;
        transition: background-color 0.2s ease;
    }

    .upvote:hover {
        background-color: #45a049;
    }

    .downvote {
        background-color: #f44336;
        color: white;
        transition: background-color 0.2s ease;
    }

    .downvote:hover {
        background-color: #e53935;
    }

    /* Vote count style */
    .vote-count {
        font-size: 1em;
        color: #777;
        margin-left: 10px;
        white-space: nowrap;
    }
</style>