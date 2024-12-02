<script>
  import { reload } from "./Account.svelte"

  /** @param {Event} event */
  function logout(event) {
    event.preventDefault();
    fetch("/logout", { method: "POST", redirect: "manual" })
          .then(updateUser)
          .then(redirect)
    return false;
  }

  /** @param {Response} result */
  function updateUser(result) {
    reload()
    return result;
  }

  /** @param {Response} result */
  function redirect(result) {
    history.pushState({}, null, "/");
    return result;
  }

</script>

<h1>Logout</h1>

<div class="form">
  <form on:submit={logout}>
    <button>Log out</button>
  </form>
</div>