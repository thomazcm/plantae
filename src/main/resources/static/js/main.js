axios.defaults.withCredentials = true;

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

class PedidoDto {
    constructor() {
        this.clientes = [];
        this.email = '';
        this.cortesia = false;
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
            pedidoDto: new PedidoDto(),
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
            cortesias: 0,
            primeiroLote: 0,
            segundoLote: 0,
            gerarCortesia: false,
            maxTickets : maxTickets,
            mostrarForm : null
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
            openCameraModal() {
                $('#cameraModal').modal('show');
                this.openCamera();
            },
            closeCamera() {
                $('#cameraModal').modal('hide');
                this.$refs.videoElement.srcObject.getTracks().forEach(track => track.stop());
                clearInterval(this.scanInterval);
            },
            async openCamera(retryCount = 6, delay = 500){
                try {
                    const constraints = { video: { facingMode: "environment" } };
                    const stream = await navigator.mediaDevices.getUserMedia(constraints);
                    this.$refs.videoElement.srcObject = stream;
            
                    this.scanInterval = setInterval(() => {
                      this.scanQRCode();
                    }, 1000);
                  } catch(err) {
                    console.error("Error: " + err);
                    if(retryCount > 0) {
                        setTimeout(() => this.openCamera(retryCount - 1, delay), delay);
                    }
                  }
            },
            scanQRCode() {
                const video = this.$refs.videoElement;
                const canvas = this.$refs.canvasElement;
                const ctx = canvas.getContext("2d", { willReadFrequently: true });

                canvas.width = video.videoWidth;
                canvas.height = video.videoHeight;
                ctx.drawImage(video, 0, 0, canvas.width, canvas.height);
          
                const imageData = ctx.getImageData(0, 0, canvas.width, canvas.height);
                const code = window.jsQR(imageData.data, imageData.width, imageData.height);
                if (code) {
                  console.log("Found QR code", code.data);
                  clearInterval(this.scanInterval);
                  video.srcObject.getTracks().forEach(track => track.stop());
                  this.closeCamera();
                  window.location.href = code.data;
                }
            },
            confirmarNovoIngresso() {
                this.ingressoGerado = true;
                let emptyCliente = this.pedidoDto.clientes.some(cliente => cliente.nome.trim() === '');
                if (emptyCliente || !this.isEmailValid(this.pedidoDto.email)) {
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
            mostrarFormulario(){
				this.mostrarForm = true;
			},
            gerarIngresso() {
                this.ingressoGerado = true;
                if (this.pedidoDto.clientes.some(cliente => cliente.nome === '')
                    || !this.isEmailValid(this.pedidoDto.email)) {
                    return
                }
                this.loadingMessage = 'Gerando ingresso,'
                this.loading = true;
                
                
                this.pedidoDto.cortesia = this.gerarCortesia;
                const payload = {
                    pedidoDto: this.pedidoDto
                };
                axios.post(`${apiEndpoint}/ingressos/novo`, payload, {
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
                        this.pedidoDto = new PedidoDto();
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
                this.pedidoDto.clientes.push(newCliente);
            },
            removerClienteInput(index) {
                this.pedidoDto.clientes.pop();
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
                    this.cortesias = 0;
                    this.ingressos.forEach(ingresso => {
                        ingresso.data = formatarData(ingresso.data);
                        if (ingresso.lote == 'PRIMEIRO') {
                            this.primeiroLote++;
                        } else if (ingresso.lote == 'SEGUNDO') {
                            this.segundoLote++;
                        } else if (ingresso.lote == 'CORTESIA') {
                            this.cortesias++;
                        }
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
