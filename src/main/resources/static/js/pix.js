function onLoad() {
    var pix = new Vue({
        el: '#pix',
        data: {
            pix : '',
            loading: null,
            display : '1 convite',
            quantity: 1,
            displayTotal: 'R$ ',
            unitPrice: 0,
            total: null,
            vueLoaded : false,
            links: ['', '', '', ''],
            remainingTickets: remainingTickets,
            soldOut : false,
            soldOutMessage: ''
        },
        mounted(){
			axios.get(`${apiEndpoint}/configurations`)
				.then(res => {
					this.links = res.data.pixLinks;
					this.unitPrice = res.data.unitPrice;
					this.total = this.unitPrice;
					this.displayTotal = `R$ ${this.total}`;
					this.loading = true;
				})
				.catch(err => {
					console.log(err)
				});
			$("#copy-button").click(function(){
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
					},
		methods : {
			isVueLoaded() {
				return this.vueLoaded;
			},
			increase() {
				if (this.quantity >= 4) {
					return;
				} else if (this.quantity >= this.remainingTickets) {
					this.soldOut = true;
					this.soldOutMessage = `Só temos mais ${this.quantity} ingresso`;
					if (this.quantity == 1) {
						this.soldOutMessage+=' disponível!';
					} else {
						this.soldOutMessage+='s disponíveis!';
					}
					console.log(this.soldOutMessage);
					return;
				}
				this.quantity++;
				this.total += this.unitPrice;
				this.display = `${this.quantity} convite`;
				this.displayTotal = `R$ ${this.total}`;
				if (this.quantity > 1) {
					this.display+='s';
				}
			},
			decrease() {
				if (this.quantity <= 1) {
					return;
				}
				this.quantity--;
				this.total -= this.unitPrice;
				this.display = `${this.quantity} convite`;
				this.displayTotal = `R$ ${this.total}`;
				if (this.quantity > 1) {
					this.display+='s';
				}
			}
		}
	    });
}