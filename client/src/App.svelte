<!-- src/App.svelte -->
<script>
  import { isAuthenticated } from './stores/auth'; // Import your authentication store
  import { onMount } from 'svelte';
  import LoginButton from './routes/LoginButton.svelte'; // Assuming you have this component
  import Register from "./Components/Login.svelte";
  import Survey from './Components/Survey.svelte';
  
  let authenticated;

  // Subscribe to the store
  const unsubscribe = isAuthenticated.subscribe(value => {
    authenticated = value; // Update the local variable
  });

  // Clean up the subscription when the component is destroyed
  onMount(() => {
    return () => unsubscribe();
  });
</script>

{#if authenticated}
  <!-- Main application content -->
  <Survey/>

{:else}
  <!-- Login Screen -->
  <h1>Please log in</h1>

  <Register />
  <br>
  <LoginButton /> <!-- Render your login button component -->
{/if}