function onLoad() {
    var pix = new Vue({
        el: '#pix',
        data: {
            pix: '',
            loading: null,
            display: '1 convite',
            quantity: 1,
            displayTotal: `R$ ${config.unitPrice}`,
            total: config.unitPrice,
            links: config.pixLinks,
            unitPrice: config.unitPrice,
            remainingTickets: remainingTickets,
            soldOut: false,
            soldOutMessage: ''
        },
        mounted() {
            $("#copy-button").click(function () {
                var copyText = document.getElementById("copy-textarea");
                if (document.queryCommandSupported('copy')) {
                    copyText.select();
                    document.execCommand('copy');
                    var copied = document.queryCommandSupported('copy');
                    if (copied) {
                        $(this).tooltip('dispose');
                        $(this).attr('title', 'Link Copiado!').tooltip();
                        $(this).tooltip('show');
                        $('#modalInstagramDirect').modal('show');
                    }
                } else {
                    navigator.clipboard.writeText(copyText.value).then(() => {
                        $(this).tooltip('dispose');
                        $(this).attr('title', 'Link Copiado!').tooltip();
                        $(this).tooltip('show');
                        $('#modalInstagramDirect').modal('show');
                    }).catch(err => {
                        console.error('Failed to copy text: ', err);
                    });
                }
            });
            
            this.loading = true;
        },
        methods: {
            isVueLoaded() {
                return this.vueLoaded;
            },
            increase() {
                if (this.quantity >= 4) {
                    return;
                }
                if (this.ticketsAreSoldOut()) {
                    return;
                }
                this.quantity++;
                this.total += this.unitPrice;
                this.updateDisplay();
            },
            decrease() {
                if (this.quantity <= 1) {
                    return;
                }
                this.quantity--;
                this.total -= this.unitPrice;
                this.updateDisplay();
            },
            updateDisplay() {
                this.display = `${this.quantity} convite`;
                this.displayTotal = `R$ ${this.total}`;
                if (this.quantity > 1) {
                    this.display += 's';
                }
            },
            ticketsAreSoldOut() {
                if (this.quantity >= this.remainingTickets) {
                    this.soldOut = true;
                    this.soldOutMessage = `Só temos mais ${this.quantity} ingresso`;
                    if (this.quantity == 1) {
                        this.soldOutMessage += ' disponível!';
                    } else {
                        this.soldOutMessage += 's disponíveis!';
                    }
                    return true;
                }
                return false;
            }
        }
    });
}