<script>
    import { onMount } from 'svelte';
    import { isAuthenticated } from '../stores/auth';

    onMount(() => {
        // Log the current URL to help debug
        console.log("Current URL:", window.location.href);

        // Get the hash part of the URL
        const hash = window.location.hash;
        console.log("Hash from URL:", hash);  // Debugging line

        // Extract the access token from the URL hash
        const token = new URLSearchParams(hash.replace('#', '')).get('access_token');

        if (token) {
            console.log("Access Token Retrieved:", token);  // Verify token retrieval
            // Store the access token in localStorage
            localStorage.setItem('access_token', token);
            // Update the authentication state
            isAuthenticated.set(true);
            // Redirect to the main app
            window.location.href = "/";
        } else {
            console.error("No Access Token Found in URL");  // Debugging line
            window.location.href = "/";
        }
    });
</script>

<h1>Logging in...</h1>
