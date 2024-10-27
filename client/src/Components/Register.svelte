<!-- src/routes/Login.svelte -->
<script>
    import { isAuthenticated } from '../stores/auth';

    let username = '';
    let password = '';

    function handleLogin() {
        // Replace this logic with actual authentication logic
        if (username === 'user' && password === 'password') {
            isAuthenticated.set(true);  // Update authentication state
        } else {
            alert('Invalid credentials');
        }
    }
</script>

<h1>Register</h1>
<form on:submit|preventDefault={handleLogin}>
    <input
            type="text"
            placeholder="Username"
            bind:value={username}
    />
    <input
            type="password"
            placeholder="Password"
            bind:value={password}
    />
    <button type="submit">Register</button>
</form>
