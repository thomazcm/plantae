axios.defaults.withCredentials = true;

class UserConfigurationForm {
    constructor(pixLinks, unitPrice, maxTickets, lote) {
        this.pixLinks = pixLinks;
        this.unitPrice = unitPrice;
        this.maxTickets = maxTickets;
        this.lote = lote;
    }
}


function onLoad() {
    var config = new Vue({
        el: '#config',
        data: {
			config: null,
			loading: null,
			loadingMessage: '',
			pixLinks: ['', '', '', ''],
			unitPrice: 0,
			maxTickets: null,
            lote : null
        },
        mounted(){
			
			axios.get(`${apiEndpoint}/configurations`)
				.then(res => {
					this.pixLinks = res.data.pixLinks;
					this.unitPrice = res.data.unitPrice;
					this.maxTickets = res.data.maxTickets;
                    this.lote = res.data.lote;
					this.loading=true;
				})
				.catch(err => {
					console.log(err)
				});
		},
		methods : {
			salvarConfiguracoes(){
				this.loading = null;
				const newConfig = new UserConfigurationForm(this.pixLinks, this.unitPrice, this.maxTickets);
				axios.put(`${apiEndpoint}/configurations`, newConfig)
				.then(res =>{
					this.loading = true;
					$('#modalConfig').modal('show');
				})
				.catch(err => {
					console.log(err)
				})
			},
			selectText(event) {
				event.target.select();
			},
			home() {
                window.location.href = '/';
            },
            report(){
				window.location.href= '/report';
			}
		}
	    });
}
