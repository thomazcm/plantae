const passwordInput = document.getElementById("password-input");
const showPasswordBtn = document.getElementById("show-password-btn");

showPasswordBtn.addEventListener("click", () => {
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        showPasswordBtn.textContent = "🔒";
    } else {
        passwordInput.type = "password";
        showPasswordBtn.textContent = "👁️";
    }
});