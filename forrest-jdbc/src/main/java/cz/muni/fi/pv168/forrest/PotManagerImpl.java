package cz.muni.fi.pv168.forrest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Viktor Lehotsky on 14.03.2017.
 */
public class PotManagerImpl implements PotManager {

    private JdbcTemplate jdbc;

    public PotManagerImpl(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public void createPot(Pot pot) {
        SimpleJdbcInsert insertPot = new SimpleJdbcInsert(jdbc).withTableName("pot").usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("row", Integer.toString(pot.getRow()));
        parameters.put("col", Integer.toString(pot.getColumn()));
        parameters.put("capacity", Integer.toString(pot.getCapacity()));
        parameters.put("note", pot.getCapacity());
        Number id = insertPot.executeAndReturnKey(parameters);
        pot.setId(id.longValue());
    }

    @Override
    public void updatePot(Pot pot) {
        jdbc.update("UPDATE pot SET row=?, col=?, capacity=?, note=? WHERE id=?",
                pot.getRow(), pot.getColumn(), pot.getCapacity(), pot.getNote(), pot.getId());
    }

    @Override
    public void deletePot(Pot pot) {
        jdbc.update("DELETE FROM pot WHERE id=?", pot.getId());
    }

    @Override
    public List<Pot> findAllPots() {
        return jdbc.query("SELECT * FROM pot ", potMapper);
    }

    @Override
    public Pot findPotById(Long id) {
        return jdbc.queryForObject("SELECT * FROM pot WHERE id=?", potMapper, id);
    }

    private RowMapper<Pot> potMapper = (RowMapper<Pot>) (rs, rowNum) ->
            new Pot(
                    rs.getLong("id"),
                    rs.getInt("row"),
                    rs.getInt("col"),
                    rs.getInt("capacity"),
                    rs.getString("note")
            );

}
