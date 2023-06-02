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
            },
            clienteModal: '',
            idModal: '',
            ingressoGerado: false,
            nomeValidado
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
            validar(ingresso) {
                axios.get(`${apiEndpoint}/ingressos/validar/${this.idModal}`)
                    .then(res => {
                        ingresso.editando = false;
                        this.getIngressos();
                        this.ingresso.editando = false;
                        this.listaKey++;
                    })
                    .catch(error => console.log(error))
            },
            ok(ingresso) {
                axios.put(`${apiEndpoint}/ingressos/${ingresso.id}`, ingresso)
                    .then(res => {
                        this.ingresso.editando = false;
                        ingresso.editando = false;
                        this.listaKey++;
                    })
                    .catch(error => console.log(error))
            },
            excluir(ingresso) {
                axios.delete(`${apiEndpoint}/ingressos/${ingresso.id}`)
                    .then(res => {
                        ingresso.editando = false;
                        this.getIngressos();
                        this.ingresso.editando = false;
                        this.listaKey++;
                    })
                    .catch(error => console.log(error))
            },
            editar(ingresso) {
                this.ingressos.forEach(i => {
                    i.editando = false;
                })
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
                if (!this.clienteDto.nome) {
                    return;
                }
                $('#confirmationModalNovoIngresso').modal('show')
            },
            confirmarExcluirIngresso(ingresso) {
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
            gerarIngresso() {
                this.ingressoGerado = true;
                if (!this.clienteDto.nome) {
                    return;
                }
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
                        this.getIngressos();
                        this.listaKey++;
                    })
                    .catch(error => {
                        console.error(error);
                    });

            },
            baixarIngresso(ingresso) {
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
                    });

            }
        }
    });
}
