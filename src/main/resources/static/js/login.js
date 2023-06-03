const passwordInput = document.getElementById("password-input");
const showPasswordBtn = document.getElementById("show-password-btn");

var selectionStart;
var selectionEnd;

showPasswordBtn.addEventListener("mousedown", (event) => {
    event.preventDefault();
    const selectionStart = passwordInput.selectionStart;
  	const selectionEnd = passwordInput.selectionEnd;
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        showPasswordBtn.textContent = "🔒";
    } else {
        passwordInput.type = "password";
        showPasswordBtn.textContent = "👁️";
    }
     setTimeout(() => {
	    passwordInput.setSelectionRange(selectionStart, selectionEnd);
	  }, 0);
});
