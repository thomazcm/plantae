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
			$(document).ready(function(){
			      $('[data-toggle="tooltip"]').tooltip();
			
			      $("#copy-button").click(function(){
			        var copyText = document.getElementById("copy-textarea");
			        navigator.clipboard.writeText(copyText.value);
			
			        $(this).tooltip('dispose');  
			        $(this).attr('title', 'Link Copiado!').tooltip();  
			        $(this).tooltip('show');
			        
			        this.vueLoaded = true;
			      });
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