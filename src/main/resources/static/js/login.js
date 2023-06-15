

class LoginForm {
	constructor(email, senha) {
		this.email = email;
		this.senha = senha;
	}
}

function onLoad() {
	
	var login = new Vue({
		el:'#login',
		data: {
			email : 'plantae',
			senha : 'veganas2023'
		},
		mounted() {
			const passwordInput = document.getElementById("password-input");
			const showPasswordBtn = document.getElementById("show-password-btn");
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
		}, 
		methods : {
			fazerLogin(){
				console.log('fazendo login...');
				const loginForm = new LoginForm(this.email, this.senha)
				console.log(loginForm);
				axios.post(`${apiEndpoint}/auth`, loginForm)
				.then(res => {
					console.log(res.data);
					localStorage.setItem('jwtToken', res.data.token)
				})
				.catch(error => {
					console.log(error);
				})
			}
			
		}
	})
	
}
