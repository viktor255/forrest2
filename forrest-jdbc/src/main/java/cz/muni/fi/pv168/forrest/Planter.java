package cz.muni.fi.pv168.forrest;

/**
 * @author opontes
 */
public class Planter {
    private Long id;
    private Tree tree;
    private Pot pot;

    public Planter(Long id, Tree tree, Pot pot) {
        this.id = id;
        this.tree = tree;
        this.pot = pot;
    }

    public Long getId() {
        return id;
    }

    public Planter setId(Long id) {
        this.id = id;
        return this;
    }

    public Tree getTree() {
        return tree;
    }

    public Planter setTree(Tree tree) {
        this.tree = tree;
        return this;
    }

    public Pot getPot() {
        return pot;
    }

    public Planter setPot(Pot pot) {
        this.pot = pot;
        return this;
    }

    @Override
    public String toString() {
        return "Planter{" +
                "id=" + id +
                ", tree=" + tree +
                ", pot=" + pot +
                '}';
    }
}
