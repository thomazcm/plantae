function formatarData(data) {
	var dia = data.substring(8,10);
	var mes = data.substring(5,7);
	return `${dia}/${mes}`;
}

function onLoad() {
	var ingresso = new Vue({
		el: '#ingresso',
		data: {
			ingresso: null,
			ingressos: [],
			clienteDto: {
				nome : null,
			}
		},
		mounted() {
			url = `${apiEndpoint}/ingressos`;
            axios.get(url).then(res => {
				this.ingressos = res.data;
				this.ingressos.forEach(ingresso => {
					ingresso.data = formatarData(ingresso.data);
				})
			})
            .catch(error => console.log(error));
			
			
		},
		methods: {
			  gerarIngresso() {
			    const payload = {
			        clienteDto: this.clienteDto
			    };
			
			    axios.post(`${apiEndpoint}/qr-code/novo`, payload, {
			        responseType: 'arraybuffer',
			        headers: {
			            'Content-Type': 'application/json',
			        },
			    })
			    .then(response => {
					console.log(response.data);
			        const blob = new Blob([response.data], { type: 'application/pdf' });
			        const url = URL.createObjectURL(blob);
			        const link = document.createElement('a');
			        link.href = url;
			        var nome = this.clienteDto.nome;
			        if (nome.indexOf(" ") == -1){
						var nomeIngresso = "entrada-" + nome + "-plantae.pdf";
					} else {
				        var nomeIngresso = "entrada-" + nome.substring(0, nome.indexOf(" ")) + "-plantae.pdf";
					}
			        link.download = nomeIngresso;
			        link.click();
			    })
			    .catch(error => {
			        console.error(error);
			    });
			}
		}
	});
}
