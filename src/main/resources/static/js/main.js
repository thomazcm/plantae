function formatarData(data) {
    var dia = data.substring(8, 10);
    var mes = data.substring(5, 7);
    return `${dia}/${mes}`;
}

function onLoad() {
    var ingresso = new Vue({
        el: '#ingresso',
        data: {
            ingresso: {
                editando: false
            },
            listaKey: 0,
            ingressos: [],
            clienteDto: {
                nome: '',
                email: ''
            },
            clienteModal: '',
            idModal: '',
            ingressoGerado: false,
            nomeValidado: '',
            loading : false,
            loadingMessage: '',
            nomeOriginal: ''
        },
        mounted() {
            this.getIngressos();
            document.addEventListener('click', this.clearErrorMessage);
//            console.log('validado  ' + validado);
//            console.log('nome validado  ' + nomeValidado);
//            console.log('expirado  ' + expirado);
            if (validado){
				this.nomeValidado = nomeValidado;
                $('#modalIngressoValidado').modal('show');
			}else if (expirado){
				this.nomeValidado = nomeValidado;
                $('#modalIngressoExpirado').modal('show');
			}
        },
        beforeDestroy() {
            document.removeEventListener('click', this.clearErrorMessage);
        },
        methods: {
            home(){
                window.location.href = '/';
            },
            getIngressos() {
                axios.get(`${apiEndpoint}/ingressos`).then(res => {
                    this.ingressos = res.data;
                    this.ingressos.forEach(ingresso => {
                        ingresso.data = formatarData(ingresso.data);
                    })
                })
                    .catch(error => console.log(error));
            },
            validar() {
				this.loading = true;
				this.loadingMessage = "Validando ingresso,";
                axios.get(`${apiEndpoint}/ingressos/validar/${this.idModal}`)
                    .then(res => {
                        ingresso.editando = false;
                        this.getIngressos();
                        this.ingresso.editando = false;
                        this.listaKey++;
                    })
                    .catch(error => console.log(error))
                    .finally(() => {
						this.loading = false;
						this.loadingMessage = '';
					})
            },
            ok(ingresso) {
				if (ingresso.cliente != this.nomeOriginal) {
					 axios.put(`${apiEndpoint}/ingressos/${ingresso.id}`, ingresso)
                    .then(res => {
                    })
                    .catch(error => console.log(error))
				}
                this.ingresso.editando = false;
                ingresso.editando = false;
                this.listaKey++;
            },
            excluir() {
				this.loading = true;
				this.loadingMessage = "Excluindo compra,";
                axios.delete(`${apiEndpoint}/ingressos/${this.idModal}`)
                    .then(res => {
                        ingresso.editando = false;
                        this.getIngressos();
                        this.ingresso.editando = false;
                        this.listaKey++;
                    })
                    .catch(error => console.log(error))
                    .finally(() => {
						this.loading = false;
						this.loadingMessage = '';
					})
            },
            editar(ingresso) {
                this.ingressos.forEach(i => {
                    i.editando = false;
                })
                this.nomeOriginal = ingresso.cliente; 
                this.ingresso.editando = true;
                ingresso.editando = true;
                this.listaKey++;
            },
            clearErrorMessage(event) {
                const target = event.target;
                const inputField = document.getElementById('inputField');
                const button = document.querySelector('.plantae-button');

                if (target !== inputField && target !== button) {
                    this.ingressoGerado = false;
                }
            },
            confirmarNovoIngresso() {
                this.ingressoGerado = true;
                if (!this.clienteDto.nome || !this.isEmailValid(this.clienteDto.email)) {
                    return;
                }
                $('#confirmationModalNovoIngresso').modal('show')
            },
            confirmarExcluirIngresso(ingresso) {
				this.idModal = ingresso.id;
                this.clienteModal = ingresso.cliente;
                $('#confirmationModalExcluirIngresso').modal('show')
            },
            confirmarValidarIngresso(ingresso) {
                this.clienteModal = ingresso.cliente;
                this.idModal = ingresso.id;
                $('#confirmationModalValidarIngresso').modal('show')
            },
            cancelarValidacao(ingresso) {
                this.clienteModal = '';
                this.ingresso.editando = false;
                ingresso.editando = false;
                this.listaKey++;
            },
            cancelarExclusao(ingresso) {
                this.clienteModal = '';
                this.ingresso.editando = false;
                ingresso.editando = false;
                this.listaKey++;
            },
            isEmailValid(email) {
		        if (!email) {
		            return true; 
		        } else {
		            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
		            return emailRegex.test(email);
		        }
		    },
            gerarIngresso() {
                this.ingressoGerado = true;
                if (!this.clienteDto.nome || !this.isEmailValid(this.clienteDto.email)) {
                    return;
                }
                this.loadingMessage = 'Gerando ingresso e enviando email,'
                this.loading = true;
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
                        var nome = this.clienteDto.nome;
                        if (nome.indexOf(" ") == -1) {
                            var nomeIngresso = "entrada-" + nome + "-plantae.pdf";
                        } else {
                            var nomeIngresso = "entrada-" + nome.substring(0, nome.indexOf(" ")) + "-plantae.pdf";
                        }
                        link.download = nomeIngresso;
                        link.click();
                        this.ingressoGerado = false;
                        this.clienteDto.nome = '';
                        this.clienteDto.email ='';
                        this.getIngressos();
                        this.listaKey++;
                    })
                    .catch(error => {
                        console.error(error);
                    })
                    .finally(() => {
						this.loading = false;
						this.loadingMessage = '';
					});

            },
            baixarIngresso(ingresso) {
				this.loading = true;
				this.loadingMessage = 'Fazendo download do ingresso,'
                axios.get(`${apiEndpoint}/qr-code/pdf/${ingresso.id}`, {
                    responseType: 'arraybuffer',
                })
                    .then(response => {
                        const blob = new Blob([response.data], { type: 'application/pdf' });
                        const url = URL.createObjectURL(blob);
                        const link = document.createElement('a');
                        link.href = url;
                        var nome = ingresso.cliente;
                        if (nome.indexOf(" ") == -1) {
                            var nomeIngresso = "entrada-" + nome + "-plantae.pdf";
                        } else {
                            var nomeIngresso = "entrada-" + nome.substring(0, nome.indexOf(" ")) + "-plantae.pdf";
                        }
                        link.download = nomeIngresso;
                        link.click();
                    })
                    .catch(error => {
                        console.error(error);
                    })
                    .finally(() => {
						this.loading = false;
						this.loadingMessage = '';
					}
					);

            }
        }
    });
}
