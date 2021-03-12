package br.com.alura.leilao.service;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Pagamento;

@Service
public class GeradorDePagamento {


	private PagamentoDao pagamentos;
	@Autowired
	public GeradorDePagamento(PagamentoDao pagamentos) {
		this.pagamentos = pagamentos;
	}

	public void gerarPagamento(Lance lanceVencedor) {
		LocalDate vencimento = LocalDate.now().plusDays(1);
		Pagamento pagamento = new Pagamento(lanceVencedor, proximoDiaUtil(vencimento));
		this.pagamentos.salvar(pagamento);
	}
	public LocalDate proximoDiaUtil(LocalDate localDate) {
		DayOfWeek diaDaSemana = localDate.getDayOfWeek();
		if(diaDaSemana == DayOfWeek.SATURDAY){
			return  localDate.plusDays(2);
		}else if(diaDaSemana == DayOfWeek.SUNDAY){
			return localDate.plusDays(1);
		}
		return localDate;
	}

}
