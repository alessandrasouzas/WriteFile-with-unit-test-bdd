package RelatorioSpring.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Produto> findAll() {
        String sql = "SELECT descricao, preco FROM produto";
        return jdbcTemplate.query(sql, new ProdutoMapper());
    }

    private static final class ProdutoMapper implements RowMapper<Produto> {
        @Override
        public Produto mapRow(ResultSet rs, int rowNum) throws SQLException {
            Produto produto = new Produto();
            produto.setDescricao(rs.getString("descricao"));
            produto.setPreco(rs.getDouble("preco"));
            return produto;
        }
    }
}
