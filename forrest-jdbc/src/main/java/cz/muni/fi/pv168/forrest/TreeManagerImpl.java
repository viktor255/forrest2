package cz.muni.fi.pv168.forrest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jakub Bohos 422419
 */
public class TreeManagerImpl implements TreeManager {

    private JdbcTemplate jdbc;

    public TreeManagerImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public void createTree(Tree tree) {
        SimpleJdbcInsert insertTree = new SimpleJdbcInsert(jdbc).withTableName("trees").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", tree.getName());
        parameters.put("treeType", tree.getTreeType());
        parameters.put("isProtected", tree.isProtected());
        Number id = insertTree.executeAndReturnKey(parameters);
        tree.setId(id.longValue());
    }

    @Override
    public void updateTree(Tree tree) {
        jdbc.update("UPDATE trees SET name=?, treeType=?, isProtected=? WHERE id=?",
                tree.getName(), tree.getTreeType(), tree.isProtected(), tree.getId());
    }

    @Override
    public void deleteTree(Tree tree) {
        jdbc.update("DELETE FROM trees WHERE id=?", tree.getId());
    }

    @Override
    public List<Tree> findAllTrees() {
        return jdbc.query("SELECT * FROM trees ", treeMapper);
    }

    @Override
    public Tree getTree(Long id) {
        return jdbc.queryForObject("SELECT * FROM trees WHERE id=?", treeMapper, id);
    }

    private RowMapper<Tree> treeMapper = (RowMapper<Tree>) (rs, rowNum) ->
            new Tree(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("treeType"),
                    rs.getBoolean("isProtected")
            );
}