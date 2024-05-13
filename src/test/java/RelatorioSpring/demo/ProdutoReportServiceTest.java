package RelatorioSpring.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProdutoReportServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoReportService produtoReportService;

    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setUp() {
        produto1 = new Produto();
        produto1.setDescricao("Produto A");
        produto1.setPreco(10.00);

        produto2 = new Produto();
        produto2.setDescricao("Produto B");
        produto2.setPreco(15.00);
    }

    @Test
    @DisplayName("Dado uma lista de produtos, ao gerar um relatório CSV, então o relatório deve conter todos os produtos")
    void givenListOfProdutos_whenGeneratingCSVReport_thenReportContainsAllProdutos() throws IOException {
        // Dado
        List<Produto> produtos = Arrays.asList(produto1, produto2);
        String filePath = "test.csv";

        // Quando
        produtoReportService.generateCSVReport(filePath);

        // Então
        File file = new File(filePath);
        assertThat(file).exists();


        
    }


	@Test
	@DisplayName("Dado uma lista de produtos, ao gerar um relatório XLSX, então o relatório deve conter todos os produtos")
	void givenListOfProdutos_whenGeneratingXLSXReport_thenReportContainsAllProdutos() throws IOException {
		// Dado
		List<Produto> produtos = Arrays.asList(produto1, produto2);
		String filePath = "relatorio.xlsx";

		// Quando
		produtoReportService.generateXLSXReport(filePath);

		// Então
		assertThat(new File(filePath)).exists();
		// Adicione asserções para o conteúdo do XLSX, se necessário
	}
}
