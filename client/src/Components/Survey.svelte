<script>
    import { onMount } from "svelte";

    let surveys = []; // Holds all surveys fetched from the backend
    let apiBase = "http://localhost:8080/api/surveys"; // API endpoint

    // Fetch all surveys
    async function fetchSurveys() {
        try {
            const res = await fetch(apiBase);
            if (!res.ok) throw new Error("Failed to fetch surveys");
            surveys = await res.json();
        } catch (error) {
            console.error(error);
        }
    }

    // Load surveys on mount
    onMount(fetchSurveys);
</script>

<main>
    <h1>Surveys</h1>
    <!-- Display Existing Surveys -->
    <section>
        <h2>Existing Surveys</h2>
        {#if surveys.length}
            <div class="survey-list">
                {#each surveys as survey}
                    <div class="survey-card">
                        <h3>{survey.title}</h3>
                        <p class="author-id">Author ID: {survey.authorId || "N/A"}</p>
                        {#if survey.polls && survey.polls.length}
                            <ul class="poll-list">
                                {#each survey.polls as poll}
                                    <li class="poll">
                                        <h4>{poll.title}</h4>
                                        {#if poll.options && poll.options.length}
                                            <ul class="option-list">
                                                {#each poll.options as option}
                                                    <li>{option.text}</li>
                                                {/each}
                                            </ul>
                                        {:else}
                                            <p class="no-options">No options available.</p>
                                        {/if}
                                    </li>
                                {/each}
                            </ul>
                        {:else}
                            <p class="no-polls">No polls available.</p>
                        {/if}
                    </div>
                {/each}
            </div>
        {:else}
            <p class="no-surveys">No surveys available.</p>
        {/if}
    </section>
</main>

<style>
    /* General Page Styles */
    main {
        padding: 2rem;
        font-family: Arial, sans-serif;
        background-color: #385170;
        color: #333;
        max-width: 800px;
        margin: 0 auto;
        border-radius: 8px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    h1 {
        text-align: center;
        font-size: 2.5rem;
        color: #ececec;
        margin-bottom: 2rem;
    }

    h2 {
        font-size: 1.75rem;
        color: #ececec;
        border-bottom: 2px solid #ddd;
        padding-bottom: 0.5rem;
    }

    /* Survey List Styles */
    .survey-list {
        display: flex;
        flex-direction: column;
        gap: 1.5rem;
    }

    .survey-card {
        background-color: #9fd3c7;
        padding: 1.5rem;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        border: 1px solid #e6e6e6;
    }

    .survey-card h3 {
        font-size: 1.5rem;
        color: #000000;
        margin-bottom: 0.5rem;
    }

    .survey-card .author-id {
        font-size: 0.9rem;
        color: #777;
        margin-bottom: 1rem;
    }

    /* Poll List Styles */
    .poll-list {
        list-style-type: none;
        padding: 0;
        margin: 0;
    }

    .poll {
        margin-bottom: 1rem;
    }

    .poll h4 {
        font-size: 1.2rem;
        color: #444;
        margin-bottom: 0.5rem;
    }

    .option-list {
        list-style: none; /* Remove default bullets */
        padding: 0; /* Remove extra padding */
        margin: 0; /* Reset list margin */
    }

    .option-list li {
        display: flex; /* Use flexbox for alignment */
        align-items: center; /* Vertically align bullet and text */
        gap: 0.5rem; /* Space between bullet and text */
        font-size: 0.9rem;
        color: #000000; /* Text color */
        margin: 0.3rem 0; /* Vertical spacing between items */
    }

    .option-list li::before {
        content: "•"; /* Custom bullet */
        font-size: 1.2rem; /* Bullet size */
        color: #000000; /* Bullet color */
        display: inline-block;
        margin-right: 0.5rem; /* Space between bullet and text */
    }








    .no-options,
    .no-polls {
        font-size: 0.9rem;
        color: #999;
        margin-top: 0.5rem;
    }

    /* No Surveys Message */
    .no-surveys {
        font-size: 1.2rem;
        color: #999;
        text-align: center;
        margin-top: 2rem;
    }

</style>
