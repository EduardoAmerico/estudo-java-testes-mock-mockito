import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.dao.PagamentoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Pagamento;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.service.EnviadorDeEmails;
import br.com.alura.leilao.service.FinalizarLeilaoService;
import br.com.alura.leilao.service.GeradorDePagamento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GeradorDePagamentoTeste {

    private GeradorDePagamento geradorDePagamento;

    @Mock
    private PagamentoDao pagamentoDao;

    @Captor
    private ArgumentCaptor<Pagamento> capor;

    @BeforeEach
    public void BeforeEach() {
        MockitoAnnotations.initMocks(this);
        this.geradorDePagamento = new GeradorDePagamento(pagamentoDao);
    }

    @Test
    void deveriaCriarPagamentoParaVencedorDoLeilao() {
        Leilao leilao = leiloes();
        Lance lanceVencedor = leilao.getLanceVencedor();
        geradorDePagamento.gerarPagamento(lanceVencedor);

        Mockito.verify(pagamentoDao).salvar(capor.capture());

        Pagamento pagamento = capor.getValue();

        Assertions.assertEquals(geradorDePagamento.proximoDiaUtil(LocalDate.now().plusDays(1)),
                pagamento.getVencimento());
        Assertions.assertEquals(lanceVencedor.getValor(),
                pagamento.getValor());
        Assertions.assertFalse(pagamento.getPago());
        Assertions.assertEquals(lanceVencedor.getUsuario(),
                pagamento.getUsuario());
        Assertions.assertEquals(leilao, pagamento.getLeilao());
    }

    public Leilao leiloes() {

        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Jinior"));

        Lance lance = new Lance(new Usuario("Mauricio"),
                new BigDecimal("600"));

        leilao.propoe(lance);
        leilao.setLanceVencedor(lance);


        return leilao;
    }
}
