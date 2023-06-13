function formatarData(data) {
    var dia = data.substring(8, 10);
    var mes = data.substring(5, 7);
    return `${dia}/${mes}`;
}

class Cliente {
    constructor(nome, index) {
        this.nome = nome;
        this.index = index;
    }
}

class ClienteDto {
    constructor() {
        this.clientes = [];
        this.email = '';
    }
}

function onLoad() {
    var ingresso = new Vue({
        el: '#ingresso',
        data: {
            ingresso: {
                editando: false
            },
            listaKey: 0,
            formKey: 0,
            ingressos: [],
            clienteDto: new ClienteDto(),
            clientesIndex: 0,
            clienteModal: '',
            idModal: '',
            ingressoGerado: false,
            nomeValidado: '',
            loading: false,
            loadingMessage: '',
            nomeOriginal: '',
            textoConfirmarIngressos: null,
            downloadTickets: true,
            sendEmail: true,
            cortesias: null,
            primeiroLote: 0,
            segundoLote: 0,
            gerarCortesia: false
        },
        mounted() {
			
			
            this.addClienteInput();
            this.getIngressos();
            document.addEventListener('click', this.clearErrorMessage);
            if (validado) {
                this.nomeValidado = nomeValidado;
                $('#modalIngressoValidado').modal('show');
            } else if (expirado) {
                this.nomeValidado = nomeValidado;
                $('#modalIngressoExpirado').modal('show');
            }
        },
        beforeDestroy() {
            document.removeEventListener('click', this.clearErrorMessage);
        },
        methods: {
            confirmarNovoIngresso() {
                this.ingressoGerado = true;
                let emptyCliente = this.clienteDto.clientes.some(cliente => cliente.nome.trim() === '');
                if (emptyCliente || !this.isEmailValid(this.clienteDto.email)) {
                    return;
                }
                this.definirTexto();
                $('#confirmationModalNovoIngresso').modal('show');
            },
            definirTexto() {
                if (this.clientesIndex == 1) {
                    this.textoConfirmarIngressos = 'Gerar 1 ingresso para:';
                } else {
                    this.textoConfirmarIngressos = `Gerar ${this.clientesIndex} ingressos para:`
                }
            },
            gerarIngresso() {
                this.ingressoGerado = true;
                if (this.clienteDto.clientes.some(cliente => cliente.nome === '')
                    || !this.isEmailValid(this.clienteDto.email)) {
                    return
                }
                this.loadingMessage = 'Gerando ingresso,'
                this.loading = true;
                
                if (this.gerarCortesia) {
					axios.get(`${apiEndpoint}/configurations/cortesias/${this.clientesIndex}`)
					.catch(err => console.log(err));
				}
                
                const payload = {
                    clienteDto: this.clienteDto
                };
                axios.post(`${apiEndpoint}/qr-code/novo`, payload, {
                    responseType: 'arraybuffer',
                    headers: {
                        'Content-Type': 'application/json',
                        'Enviar-Email': this.sendEmail.toString(),
                    },
                })
                    .then(response => {
                        const blob = new Blob([response.data], { type: 'application/pdf' });
                        const url = URL.createObjectURL(blob);
                        const link = document.createElement('a');
                        link.href = url;
                        const nomeIngresso = response.headers['file-pdf-name'];
                        link.download = nomeIngresso;
                        if (this.downloadTickets) {
                            link.click();
                        }
                        this.ingressoGerado = false;
                        this.clienteDto = new ClienteDto();
                        this.clientesIndex = 0;
                        this.addClienteInput();
                        this.getIngressos();
                        this.formKey++;
                    })
                    .catch(error => {
                        console.error(error);
                    })
                    .finally(() => {
                        this.loading = false;
                        this.loadingMessage = '';
                    });

            },
            isEmailValid(email) {
                if (!email) {
                    return true;
                } else {
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    return emailRegex.test(email);
                }
            },
            addClienteInput() {
                this.clientesIndex++;
                const newCliente = new Cliente('', this.clientesIndex);
                this.clienteDto.clientes.push(newCliente);
            },
            removerClienteInput(index) {
                this.clienteDto.clientes.pop();
                this.clientesIndex--;
            },
            home() {
                window.location.href = '/';
            },
            getIngressos() {
                axios.get(`${apiEndpoint}/ingressos`).then(res => {
                    this.ingressos = res.data;
                    this.primeiroLote = 0;
                    this.segundoLote = 0;
                    this.ingressos.forEach(ingresso => {
                        ingresso.data = formatarData(ingresso.data);
                        var data = ingresso.data;
                        
                        var dia = parseInt(data.split('/')[0]);
                        var mes = parseInt(data.substring(data.length-2));
                        
                        
						if (dia >= 13 || mes > 6) {
							this.segundoLote++;
						} else {
							this.primeiroLote++;
						}  
                    })
                    axios.get(`${apiEndpoint}/configurations/totalCortesias`)
		            .then(res => {
						this.cortesias = res.data;
						this.segundoLote-= this.cortesias;
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
                const gerarIngressoButton = document.querySelector('#gerar-ingresso-button');

                if (target !== inputField && target !== gerarIngressoButton) {
                    this.ingressoGerado = false;
                }
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
                        const nomeIngresso = response.headers['file-pdf-name'];
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
