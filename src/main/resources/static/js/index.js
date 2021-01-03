window.onload = async () => {
    alert("HELLO");
    const response = await fetch("/api/v1/users/get");
    console.log(await response.json());
}