<script>

    function login(event) {
        fetch("/login", {
            body: new FormData(this),
            method: "POST"
        }).then(result => console.log(result));
    }

</script>

<h1>Login</h1>

<form action="/login" method="post">
  <input type="text" name="username" placeholder="username" required>
  <input type="password" name="password" placeholder="password" required>
  <input type="submit" value="Log in">
</form>
