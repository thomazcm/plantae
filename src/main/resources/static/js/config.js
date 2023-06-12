class UserConfigurationDto {
    constructor(pixLinks, unitPrice) {
        this.pixLinks = pixLinks;
        this.unitPrice = unitPrice;
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
			unitPrice: 0
        },
        mounted(){
			
			axios.get(`${apiEndpoint}/configurations`)
				.then(res => {
					this.pixLinks = res.data.pixLinks;
					this.unitPrice = res.data.unitPrice;
					this.loading=true;
				})
				.catch(err => {
					console.log(err)
				});
		},
		methods : {
			salvarConfiguracoes(){
				const newConfig = new UserConfigurationDto(this.pixLinks, this.unitPrice);
//				console.log(newConfig);
				axios.put(`${apiEndpoint}/configurations`, newConfig)
				.then(res =>{
//					console.log(res.data);
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
