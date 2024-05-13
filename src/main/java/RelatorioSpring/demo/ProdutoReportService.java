package RelatorioSpring.demo;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.CSVWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProdutoReportService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public void generateCSVReport(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		if (Files.exists(path)) {
			Files.delete(path);
		}

		try (FileWriter fileWriter = new FileWriter(filePath); CSVWriter csvWriter = new CSVWriter(fileWriter)) {

			// Obtem os nomes dos atributos da classe Produto usando as anotações
			// @JsonProperty
			Field[] fields = Produto.class.getDeclaredFields();
			String[] header = new String[fields.length];
			for (int i = 0; i < fields.length; i++) {
				JsonProperty jsonProperty = fields[i].getAnnotation(JsonProperty.class);
				header[i] = jsonProperty != null ? jsonProperty.value() : fields[i].getName();
			}

			csvWriter.writeNext(header);

			List<Produto> produtos = produtoRepository.findAll();

			for (Produto produto : produtos) {
				String[] row = new String[fields.length];

				for (int i = 0; i < fields.length; i++) {
					fields[i].setAccessible(true);
					try {
						Object value = fields[i].get(produto);
						row[i] = value != null ? value.toString() : "";
					} catch (IllegalAccessException e) {
						log.error("Erro ao acessar o atributo: {}", e.getMessage());
					}
				}

				csvWriter.writeNext(row);
			}
		}
	}

	public void generateXLSXReport(String filePath) throws IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Produtos");

		// Obter o cabeçalho dinâmico
		String[] header = getHeaderFromProduto();

		// Escrever o cabeçalho
		writeHeader(sheet, header);

		// Escrever os dados do banco de dados
		writeData(sheet);

		// Escrever o arquivo XLSX
		try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
			workbook.write(fileOut);
		}
	}

	private String[] getHeaderFromProduto() {
		List<Produto> produtos = produtoRepository.findAll();
		if (!produtos.isEmpty()) {
			Produto sampleProduto = produtos.get(0); // Amostra de produto para obter os nomes dos atributos
			return getHeaderNames(sampleProduto);
		}
		return new String[0];
	}

	private String[] getHeaderNames(Produto produto) {
		Field[] fields = produto.getClass().getDeclaredFields();
		String[] header = new String[fields.length];
		int colNum = 0;
		for (Field field : fields) {
			JsonProperty jsonProperty = field.getAnnotation(JsonProperty.class);
			if (jsonProperty != null) {
				header[colNum++] = jsonProperty.value();
			}
		}
		return header;
	}

	private void writeHeader(XSSFSheet sheet, String[] header) {
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < header.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(header[i]);
		}
	}

	private void writeData(XSSFSheet sheet) {
		List<Produto> produtos = produtoRepository.findAll();
		int rowNum = 1;
		for (Produto produto : produtos) {
			Row row = sheet.createRow(rowNum++);
			int cellNum = 0;
			for (Field field : produto.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				Cell cell = row.createCell(cellNum++);
				try {
					Object value = field.get(produto);
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				} catch (IllegalAccessException e) {
					log.error("Erro ao acessar o atributo: {}", e.getMessage());
				}
			}
		}
	}

}
