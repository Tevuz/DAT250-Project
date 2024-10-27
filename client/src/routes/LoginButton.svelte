<!-- src/routes/LoginButton.svelte -->
<script>

    const client_id = import.meta.env.VITE_SECRET_CLIENT_ID;
    const redirect_uri = "http://localhost:5173/auth/callback";
    const scope = "https://www.googleapis.com/auth/userinfo.profile";

    const googleAuthUrl = `https://accounts.google.com/o/oauth2/v2/auth?response_type=token&client_id=${client_id}&redirect_uri=${redirect_uri}&scope=${scope}`;

    function login() {
        window.location.href = googleAuthUrl; // Redirects to Google login
    }
</script>

<button on:click={login}>Log in with Google</button>
