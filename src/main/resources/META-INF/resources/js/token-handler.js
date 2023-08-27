// token-handler.js
function getCookie(name) {
    const value = `; ${document.cookie}`;
    console.log("cookie="+value);
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
