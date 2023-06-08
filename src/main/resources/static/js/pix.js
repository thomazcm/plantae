function onLoad() {
    var pix = new Vue({
        el: '#pix',
        data: {
            pix : '',
            display : '1 convite',
            quantity: 1,
            displayTotal: 'R$ 78',
            total: 78,
            vueLoaded : false,
            links: pixLinks
        },
        mounted(){
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
				}
				this.quantity++;
				this.total += 78;
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
				this.total -= 78;
				this.display = `${this.quantity} convite`;
				this.displayTotal = `R$ ${this.total}`;
				if (this.quantity > 1) {
					this.display+='s';
				}
			}
		}
	    });
}