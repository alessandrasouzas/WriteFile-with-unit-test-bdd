package RelatorioSpring.demo;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoApplication {

	 public static void main(String[] args) throws IOException {
	        ApplicationContext context = SpringApplication.run(DemoApplication.class, args);

	        // Obtendo o serviço de geração de relatórios do contexto Spring
	        ProdutoReportService produtoReportService = context.getBean(ProdutoReportService.class);

	        // Chamando o método para gerar o relatório
	        produtoReportService.generateCSVReport("C:\\Users\\ale\\Documents\\out\\relatorio.csv");
	        produtoReportService.generateXLSXReport("C:\\Users\\ale\\Documents\\out\\relatorio.xlxs");
	    }
}