function onLoad() {
    var pix = new Vue({
        el: '#pix',
        data: {
            pix : '',
            display : '1 convite',
            quantity: 1,
            displayTotal: 'R$ 78',
            total: 78,
            links: [
				'00020126480014br.gov.bcb.pix0126lauramirandalima@gmail.com520400005303986540578.005802BR5918Laura Miranda Lima6009Sao Paulo62070503***6304277B',
				'00020126480014br.gov.bcb.pix0126lauramirandalima@gmail.com5204000053039865406156.005802BR5918Laura Miranda Lima6009Sao Paulo62070503***6304C3F7',
				'00020126480014br.gov.bcb.pix0126lauramirandalima@gmail.com5204000053039865406234.005802BR5918Laura Miranda Lima6009Sao Paulo62070503***6304ECAD',
				'00020126480014br.gov.bcb.pix0126lauramirandalima@gmail.com5204000053039865406312.005802BR5918Laura Miranda Lima6009Sao Paulo62070503***63048FD0'
			]
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
			      });
			});
			
		},
		methods : {
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