import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import br.com.alura.leilao.service.EnviadorDeEmails;
import br.com.alura.leilao.service.FinalizarLeilaoService;
import com.sun.xml.internal.ws.client.ClientSchemaValidationTube;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FinalizaLeilaoServiceTeste {

    private FinalizarLeilaoService service;

    @Mock
    private LeilaoDao daoMock;
    @Mock
    private EnviadorDeEmails enviadorDeEmails;

    @BeforeEach
    public void BeforeEach() {
        MockitoAnnotations.initMocks(this);
        this.service = new FinalizarLeilaoService(daoMock, enviadorDeEmails);
    }

    @Test
    void deveriaFinalizarUmLeilao() {
        List<Leilao> listaLeiloes = leiloes();

        Mockito.when(daoMock.buscarLeiloesExpirados())
                .thenReturn(listaLeiloes);
        // System.out.println(listaLeiloes.get(0).getUsuario().getNome().toString());
        service.finalizarLeiloesExpirados();

        Leilao leilao = listaLeiloes.get(0);


        Assertions.assertEquals(new BigDecimal("900"), leilao.getLanceVencedor().getValor());
        Assertions.assertTrue(leilao.isFechado());
        Mockito.verify(daoMock).salvar(leilao);
    }
    @Test
    void deveriaEnviarEmailParaVencedorDoleilao() {
        List<Leilao> listaLeiloes = leiloes();

        Mockito.when(daoMock.buscarLeiloesExpirados())
                .thenReturn(listaLeiloes);
        // System.out.println(listaLeiloes.get(0).getUsuario().getNome().toString());
        service.finalizarLeiloesExpirados();

        Leilao leilao = listaLeiloes.get(0);
        Lance lanceVencedor = leilao.getLanceVencedor();


        Mockito.verify(enviadorDeEmails).enviarEmailVencedorLeilao(lanceVencedor);
    }
    @Test
    void naoDeveriaEnviarEmailParaVencedorDoleilaoEmCadoDeErroAoEncerrarOLeilao() {
        List<Leilao> listaLeiloes = leiloes();

        Mockito.when(daoMock.buscarLeiloesExpirados())
                .thenReturn(listaLeiloes);

        Mockito.when(daoMock.salvar(Mockito.any())).thenThrow(RuntimeException.class);
        try{
            service.finalizarLeiloesExpirados();
            Mockito.verifyNoInteractions(enviadorDeEmails);
        }catch (Exception e){

        }

    }

    public List<Leilao> leiloes() {
        List<Leilao> lista = new ArrayList<>();
        Leilao leilao = new Leilao("Celular",
                new BigDecimal("500"),
                new Usuario("Jinior"));

        Lance primeiro = new Lance(new Usuario("Mauricio"),
                new BigDecimal("600"));
        Lance segundo = new Lance(new Usuario("Caio"),
                new BigDecimal("900"));

        leilao.propoe(primeiro);
        leilao.propoe(segundo);

        lista.add(leilao);

        return lista;
    }

}
