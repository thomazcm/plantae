function onLoad() {
	var ingresso = new Vue({
		el: '#ingresso',
		data: {
			ingresso: null,
			clienteDto: {
				nome : null
			}
		},
		mounted() {
			console.log('loading vue');
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
			        const blob = new Blob([response.data], { type: 'application/pdf' });
			        const url = URL.createObjectURL(blob);
			        const link = document.createElement('a');
			        link.href = url;
			        link.download = 'ingresso-plantae.pdf';
			        link.click();
			    })
			    .catch(error => {
			        console.error(error);
			    });
			}
		}
	});
}
