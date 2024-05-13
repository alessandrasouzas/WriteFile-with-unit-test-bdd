package RelatorioSpring.demo;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Produto {

    @JsonProperty("Descrição")
    private String descricao;

    @JsonProperty("Preço")
    private Double preco;

    private UUID id;

    // Construtor
    public Produto() {
        // Gera um UUID aleatório para o produto
        this.id = UUID.randomUUID();
    }
}
