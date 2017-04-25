package cz.muni.fi.pv168.forrest;

import java.util.Objects;

/**
 * @author Jakub Bohos 422419
 */
public class Tree {
    private Long id;
    private String name;
    private String treeType;
    private boolean protectedByLaw;

    public Tree() {
    }

    public Tree(String name, String treeType, boolean protectedByLaw) {
        this.name = name;
        this.treeType = treeType;
        this.protectedByLaw = protectedByLaw;
    }

    public Tree(Long id, String name, String treeType, boolean isProtected) {
        this.id = id;
        this.name = name;
        this.treeType = treeType;
        this.protectedByLaw = isProtected;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTreeType() {
        return treeType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTreeType(String treeType) {
        this.treeType = treeType;
    }

    public boolean isProtected() {
        return protectedByLaw;
    }

    public void setProtected(boolean isProtected) {
        this.protectedByLaw = isProtected;
    }

    public String toString() {
        return "Tree{"
                + "id=" + id
                + ", name=" + name
                + ", treeType=" + treeType
                + ", isProtected=" + protectedByLaw
                + '}';
    }

    /**
     * Returns true if obj represents the same tree. Two objects are considered
     * to represent the same body when both are instances of {@link Tree} class,
     * both have assigned some id and this id is the same.
     *
     *
     * @param obj the reference object with which to compare.
     * @return true if obj represents the same body.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tree other = (Tree) obj;
        if (obj != this && this.id == null) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
