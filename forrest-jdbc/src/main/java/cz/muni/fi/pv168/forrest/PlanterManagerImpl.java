package cz.muni.fi.pv168.forrest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class PlanterManagerImpl implements PlanterManager {

    final static Logger logger = LoggerFactory.getLogger(PlanterManagerImpl.class);

    private JdbcTemplate jdbc;
    private TreeManager treeManager;
    private PotManager potManager;

    public PlanterManagerImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    public void setTreeManager(TreeManager treeManager) {
        this.treeManager = treeManager;
    }

    public void setPotManager(PotManager potManager) {
        this.potManager = potManager;
    }

    @Override
    public Pot findPotWithTree(Tree tree) {
        return jdbc.queryForObject(
                "SELECT * FROM PLANTERS WHERE treeId=?",
                (rs, rowNum) -> {
                    long potId = rs.getLong("potId");
                    Pot pot = null;
                    try {
                        pot = potManager.findPotById(potId);
                    } catch (PotException e) {
                        logger.error("cannot find pot", e);
                    }
                    return pot;
                },
                tree.getId());
    }

    @Override
    public List<Tree> findTreesInPot(Pot pot) {
        List<Long> ids = jdbc.queryForList(
                "SELECT treeId FROM PLANTERS WHERE potId=?",
                Long.class,
                pot.getId());
        List<Tree> trees = new ArrayList<>();
        ids.forEach(id -> {
                    try {
                        trees.add(treeManager.getTree(id));
                    } catch (TreeException e) {
                        logger.error("cannot find tree", e);
                    }
                }
        );
        return trees;
    }

    @Override
    public List<Tree> findUnplantedTrees() {
        return jdbc.query(
                "SELECT id FROM TREES WHERE id NOT IN (SELECT treeId FROM PLANTERS)",
                (rs, rowNum) -> {
                    long treeId = rs.getLong("id");
                    Tree tree = null;
                    try {
                        tree = treeManager.getTree(treeId);
                    } catch (TreeException e) {
                        logger.error("cannot find tree", e);
                    }
                    return tree;
                }
        );
    }

    @Override
    public List<Pot> findEmptyPots() {
        return jdbc.query(
                "SELECT id FROM POT WHERE id NOT IN (SELECT potId FROM PLANTERS)",
                (rs, rowNum) -> {
                    long potId = rs.getLong("id");
                    Pot pot = null;
                    try {
                        pot = potManager.findPotById(potId);
                    } catch (PotException e) {
                        logger.error("cannot find tree", e);
                    }
                    return pot;
                }
        );
    }

    @Override
    public List<Pot> findPotsWithSomeFreeSpace() {
        throw new NotImplementedException();
    }

    @Override
    public void putTreeIntoPot(Tree tree, Pot pot) {
        SimpleJdbcInsert insertPlanter = new SimpleJdbcInsert(jdbc).withTableName("PLANTERS").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("treeId", tree.getId());
        parameters.put("potId", pot.getId());
        insertPlanter.executeAndReturnKey(parameters);
    }

    private static void checkIfPotHasSpace(Connection conn, Pot pot) {
        throw new NotImplementedException();
    }

    @Override
    public void removeTreeFromPot(Tree tree, Pot pot) {

    }
}