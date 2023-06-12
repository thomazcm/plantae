class UserConfigurationDto {
    constructor(pixLinks, unitPrice, maxTickets) {
        this.pixLinks = pixLinks;
        this.unitPrice = unitPrice;
        this.maxTickets = maxTickets;
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
			maxTickets: null
        },
        mounted(){
			
			axios.get(`${apiEndpoint}/configurations`)
				.then(res => {
					this.pixLinks = res.data.pixLinks;
					this.unitPrice = res.data.unitPrice;
					this.maxTickets = res.data.maxTickets;
					this.loading=true;
				})
				.catch(err => {
					console.log(err)
				});
		},
		methods : {
			salvarConfiguracoes(){
				this.loading = null;
				const newConfig = new UserConfigurationDto(this.pixLinks, this.unitPrice, this.maxTickets);
//				console.log(newConfig);
				axios.put(`${apiEndpoint}/configurations`, newConfig)
				.then(res =>{
//					console.log(res.data);
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
            }
		}
	    });
}
