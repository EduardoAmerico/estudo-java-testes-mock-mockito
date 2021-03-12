import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Lance;
import br.com.alura.leilao.model.Leilao;
import br.com.alura.leilao.model.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

public class HelloWorld {
    @Test
    void hello(){
        LeilaoDao mock = Mockito.mock(LeilaoDao.class);
        List<Leilao> todos = mock.buscarTodos();
        Assertions.assertTrue(todos.isEmpty());
    }
}
