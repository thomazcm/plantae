

function formatarData(data) {
	var dia = data.substring(8,10);
	var mes = data.substring(5,7);
	return `${dia}/${mes}`;
}

function tokenExpired(error) {
    if (error.request.status == 403) {
        axios.get(`${localEndpoint}/api/token`)
        .then(res => {
            axios.defaults.headers.common['Authorization'] = `Bearer ${res.data}`;
            return true;
        })
        .catch (error => console.log(error));
    } else {
        return false
    }
}

function onLoad() {
	var tituloHome = new Vue({
        el: '#titulo-home',
        data: {
			titulo : titulo,
			mesAnteriorUrl: null,
			proximoMesUrl : null,
            mesAnteriorNome: mesAnteriorNome,
            proximoMesNome: proximoMesNome
        },
        mounted() {
			this.mesAnteriorUrl = `${localEndpoint}${mesAnteriorUrl}`;
			this.proximoMesUrl = `${localEndpoint}${proximoMesUrl}`;
        }
    });
	
    var receitas = new Vue({
        el: '#receitas',
        data: {
            receitas: [],
            receitaForm: new ReceitaForm(),
			receitaEditForm: null,
			mostrarBotaoEdicao: false,
			receitaEditada : null,
			receitasKey: 0,
			receitasListaKey: 0,
            receitaAExcluir: 0
        },
        mounted() {
            this.getReceitas();
        },
        methods: {
            getReceitas() {
                url = `${apiEndpoint}/receitas/${ano}/${mes}`;
                axios.get(url).then(res => this.getSuccess(res))
                .catch(error => console.log(error));
            },
            getSuccess(res) {
                this.receitas = res.data;
                this.receitas.forEach(receita => {
                    receita.dataFormatada = formatarData(receita.data);
                })
            },
            novaReceita: function(receitaForm) {
                this.resetErrors(receitaForm);
                url = `${apiEndpoint}/receitas`;
                body = new ReceitaDto(receitaForm);

                axios.post(url, body).then(res => this.novaSuccess())
                    .catch(error => {
                        if (error.request.status == 403) {
                            axios.get(`${localEndpoint}/api/token`)
                            .then(res => {
                                axios.defaults.headers.common['Authorization'] = `Bearer ${res.data}`;
                                axios.post(url, body).then(res => this.novaSuccess())
                                .catch(error => console.log(error));
                            })
                            .catch(error => console.log(error))
                        } else {
                            displayErrors(error, receitaForm);
                        }
                    })
            },
            novaSuccess() {
                this.getReceitas();
                this.receitasKey++;
                resumo.atualizar();
                this.resetForm(this.receitaForm);
                this.cancelarEdicao();
            },
            editarReceita(receitaEditForm, id){
                this.resetErrors(receitaEditForm);
                url = `${apiEndpoint}/receitas/${id}`;
                body = new ReceitaDto(receitaEditForm);
                axios.put(url, body).then(res => this.editarSuccess())
                .catch(error => {
                    if (error.request.status == 403) {
                        axios.get(`${localEndpoint}/api/token`)
                        .then(res => {
                            axios.defaults.headers.common['Authorization'] = `Bearer ${res.data}`;
                            axios.put(url, body).then(res => this.editarSuccess())
                            .catch(error => console.log(error));
                        })
                        .catch(error => console.log(error))
                    } else {
                        displayErrors(error, receitaEditForm);
                    }
                })
            },
            editarSuccess(){
                this.receitaEditada = null;
                this.getReceitas();
                this.receitasListKey++;
                resumo.atualizar();
                this.cancelarEdicao();
            }
            ,
            excluirReceita(){
                url = `${apiEndpoint}/receitas/${this.receitaAExcluir}`;
                
                axios.delete(url).then(res => this.excluirSuccess())
                .catch(error => {
                    if (error.request.status == 403) {
                        axios.get(`${localEndpoint}/api/token`)
                        .then(res => {
                            axios.defaults.headers.common['Authorization'] = `Bearer ${res.data}`;
                            axios.delete(url).then(res => this.excluirSuccess())
                            .catch(error => console.log(error));
                        })
                        .catch(error => console.log(error))
                    } else {
                        console.log(error);
                    }
                })
            },
            excluirSuccess(){
                this.getReceitas();
                this.receitasListKey++;
                resumo.atualizar();
                this.cancelarEdicao();  
            },
            prepararExclusao(id){
				this.receitaAExcluir = id;
			},
			botaoNovaReceita(){
				this.cancelarEdicao();
				despesas.botaoEdicao(false);
			},
			botaoEdicao(status) {
				this.mostrarBotaoEdicao = status;
				despesas.mostrarBotaoEdicao = false;
				despesas.despesaEditada = null;
                this.receitaEditada = null;
                this.receitaEditForm = null;
				this.receitasKey++;
			},
			inciarEdicao(receita)
			{
				this.receitaEditada = receita.id;
				this.receitasListKey++;
				this.receitaEditForm = new ReceitaEditForm(receita);
			},
			finalizarEdicao(){
				this.cancelarEdicao();
				this.receitasListKey++;
			},
			cancelarEdicao(){
				this.receitaEditada = null;
                this.receitaEditForm = null;
				this.mostrarBotaoEdicao = false;
			},
            resetForm(form){
                form.descricao = "";
                form.valor = "";
                form.data = new Date().toISOString().split('T')[0];
                this.resetErrors(form);
            },
            resetErrors(form) {
				form.errors.descricao = "";
                form.errors.valor = "";
                form.errors.data = "";
                form.errors.dia = "";
                form.errors.mes = "";
                form.errors.ano = "";
			}
		}
    });
    var despesas = new Vue({
        el: '#despesas',
        data: {
            despesas: [],
            despesaForm: new DespesaForm(),
            despesaEditForm:  null,
            mostrarBotaoEdicao: false,
            despesaEditada : null,
            despesasKey: 0,
            despesasListaKey: 0,
            despesaAExcluir: null,
            listaDeCategorias: listaCategorias
        },
        mounted() {
            this.getDespesas();
        },
        methods: {
            getDespesas() {
                url = `${apiEndpoint}/despesas/${ano}/${mes}`;
                axios.get(url).then(res => this.getSuccess(res))
                .catch(error => console.log(error));
            },
            getSuccess(res) {
                this.despesas = res.data;
                this.despesas.forEach(despesa => {
                    despesa.dataFormatada = formatarData(despesa.data);
                })
            },
            novaDespesa: function(despesaForm) {
                this.resetErrors(despesaForm);
                url = `${apiEndpoint}/despesas`;
                body = new DespesaDto(despesaForm);
        
                axios.post(url, body).then(res => this.novaSuccess())
                    .catch(error => {
                        if (error.request.status == 403) {
                            axios.get(`${localEndpoint}/api/token`)
                            .then(res => {
                                axios.defaults.headers.common['Authorization'] = `Bearer ${res.data}`;
                                axios.post(url, body).then(res => this.novaSuccess())
                                .catch(error => console.log(error));
                            })
                            .catch(error => console.log(error))
                        } else {
                            displayErrors(error, despesaForm);
                        }
                    })
            },
            novaSuccess() {
                this.getDespesas();
                this.despesasKey++;
                resumo.atualizar();
                this.resetForm(this.despesaForm);
                this.cancelarEdicao();
            },
            editarDespesa(despesaEditForm, id){
                this.resetErrors(despesaEditForm);
                url = `${apiEndpoint}/despesas/${id}`;
                body = new DespesaDto(despesaEditForm);
                axios.put(url, body).then(res => this.editarSuccess())
                .catch(error => {
                    if (error.request.status == 403) {
                        axios.get(`${localEndpoint}/api/token`)
                        .then(res => {
                            axios.defaults.headers.common['Authorization'] = `Bearer ${res.data}`;
                            axios.put(url, body).then(res => this.editarSuccess())
                            .catch(error => console.log(error));
                        })
                        .catch(error => console.log(error))
                    } else {
                        displayErrors(error, despesaEditForm);
                    }
                })
            },
            editarSuccess(){
                this.despesaEditada = null;
                this.getDespesas();
                this.despesasListKey++;
                resumo.atualizar();
                this.cancelarEdicao();
            }
            ,
            excluirDespesa(){
                url = `${apiEndpoint}/despesas/${this.despesaAExcluir}`;
                
                axios.delete(url).then(res => this.excluirSuccess())
                .catch(error => {
                    if (error.request.status == 403) {
                        axios.get(`${localEndpoint}/api/token`)
                        .then(res => {
                            axios.defaults.headers.common['Authorization'] = `Bearer ${res.data}`;
                            axios.delete(url).then(res => this.excluirSuccess())
                            .catch(error => console.log(error));
                        })
                        .catch(error => console.log(error))
                    } else {
                        console.log(error);
                    }
                })
            },
            excluirSuccess(){
                this.getDespesas();
                this.despesasListKey++;
                resumo.atualizar();
                this.cancelarEdicao();  
            },
            prepararExclusao(id){
				this.despesaAExcluir = id;
			},
            botaoNovaDespesa(){
				this.cancelarEdicao();
				receitas.botaoEdicao(false);
			},
            botaoEdicao(status) {
                receitas.mostrarBotaoEdicao = false;
                receitas.receitaEditada = null;
                this.mostrarBotaoEdicao = status;
                this.despesaEditada = null;
                this.despesaEditForm = null;
                this.despesasKey++;
            },
            inciarEdicao(despesa)
            {
                this.despesaEditForm = new DespesaEditForm(despesa);
                this.despesaEditada = despesa.id;
                this.despesasListKey++;
            },
            finalizarEdicao(){
                this.cancelarEdicao();
                this.despesasListKey++;
            },
            cancelarEdicao(){
				this.despesaEditada = null;
                this.despesaEditForm = null;
                this.mostrarBotaoEdicao = false;
			},
            resetForm(form){
                form.descricao = "";
                form.valor = "";
                form.categoria = "Selecione a Categoria";
                form.data = new Date().toISOString().split('T')[0];
                this.resetErrors(form);
            },
            resetErrors(form){
				form.errors.descricao = "";
                form.errors.valor = "";
                form.errors.data = "";
                form.errors.dia = "";
                form.errors.mes = "";
                form.errors.ano = "";
                form.errors.categoria = "";
			}
        }
    });

    var resumo = new Vue({
        el: '#resumo',
        data: {
            valoresResumo: [],
            zeroReceitas: true,
            zeroDespesas: true,
            resumoKey: 0
        },
        mounted() {
            this.getResumo();
        },
        methods: {
			getResumo(){
				 axios.get(`${apiEndpoint}/resumo/${ano}/${mes}`)
                .then(res => {
                    this.valoresResumo = gerarValoresResumo(res.data);
                    if (res.data.totalReceitas > 0) {
						this.zeroReceitas = false;
					}
                    if (res.data.totalDespesas > 0) {
						this.zeroDespesas = false;
						const gastos = res.data.gastosPorCategoria;
                    	this.gerarGrafico(gastos);
					}
                });
			},
			gerarGrafico(gastos) {
//                    google.charts.load('current', { 'packages': ['corechart'] });
//                    google.charts.setOnLoadCallback(drawChart);

				google.charts.load('current', {
				  callback: function () {
				    drawChart();
				    $(window).resize(drawChart);
				  },
				  packages:['corechart']
				});
                        
                function drawChart() {
                    var data = new google.visualization.DataTable();
                    data.addColumn('string', 'Status');
					data.addColumn('number', 'Count');
                    data.addRow(['Alimentação', gastos.ALIMENTACAO]);
					data.addRow(['Transporte', gastos.TRANSPORTE]);
					data.addRow(['Outras', gastos.OUTRAS]);
					data.addRow(['Lazer', gastos.LAZER]);
					data.addRow(['Saúde', gastos.SAUDE]);
					data.addRow(['Imprevistos', gastos.IMPREVISTOS]);
					data.addRow(['Moradia', gastos.MORADIA]);
					data.addRow(['Educação', gastos.EDUCACAO]);
				    var myColors = { 'Alimentação': '#d13d3d',
									'Transporte': '#7a7a09',
									'Outras': '#b39fab',
									'Lazer': '#de4ea4',
									'Saúde': '#06b850',
									'Imprevistos': '#e68e7e',
									'Moradia': '#638cc2',
									'Educação': '#38e0bc'}

					var slicesColor = {};
					for( var i=0; i < data.getNumberOfRows(); i++){
					  slicesColor[i] = {color: myColors[data.getValue(i, 0)] };
					}
					chartConfiguration.slices = slicesColor;					
                    var chart = new google.visualization.PieChart(document.getElementById('myChartGoogle'));
                    chart.draw(data, chartConfiguration);
                }
            },
            atualizar(){
				this.getResumo();
                resumo.resumoKey++;
			}
		}
    });
}

var chartConfiguration = {
    title: 'Gastos por Categoria',
    fontName: 'cabin, sans-serif',
    titleTextStyle: { fontSize: 24, bold: true },
    chartArea: { width: '100%', height: '100%', left: 10, top: 15, bottom: 15},
    width: '100%',
    fontSize: 22,
    slices: null,
    legend: {position:'right', alignment: 'start'}
}
