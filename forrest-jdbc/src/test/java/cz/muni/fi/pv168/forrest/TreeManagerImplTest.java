package cz.muni.fi.pv168.forrest;

import java.sql.SQLException;
import javax.sql.DataSource;

import cz.muni.fi.pv168.forrest.common.DBUtils;
import cz.muni.fi.pv168.forrest.common.IllegalEntityException;
import cz.muni.fi.pv168.forrest.common.ServiceFailureException;
import cz.muni.fi.pv168.forrest.common.ValidationException;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * @author Jakub Bohos 422419
 */
@RunWith(SpringJUnit4ClassRunner.class) //Spring se zúčastní unit testů
@ContextConfiguration(classes = {MySpringTestConfig.class}) //konfigurace je ve třídě MySpringTestConfig
@Transactional //každý test poběží ve vlastní transakci, která bude na konci rollbackována
public class TreeManagerImplTest {

    private TreeManagerImpl manager;
    private DataSource ds;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();




    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:treeManager-test");
        ds.setCreateDatabase("create");
        return ds;
    }


    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        DBUtils.executeSqlScript(ds,TreeManager.class.getClassLoader().getResource("createTables.sql"));
        manager = new TreeManagerImpl(ds);
        //manager.setDataSource(ds);
    }

    @After
    public void tearDown() throws SQLException {
        DBUtils.executeSqlScript(ds,TreeManager.class.getClassLoader().getResource("dropTables.sql"));
    }


    private TreeBuilder sampleWilhelmTreeBuilder() {
        return new TreeBuilder()
                .name("Wilhelm")
                .treeType("buk")
                .isProtected(false);
    }

    private TreeBuilder sampleBobTreeBuilder() {
        return new TreeBuilder()
                .name("Bob")
                .treeType("tuja")
                .isProtected(true);
    }


    /*
    @Before
    public void setUp() throws SQLException {
        manager = new TreeManagerImpl();
    }
*/


    @Test
    public void createTree() {
        Tree tree = sampleWilhelmTreeBuilder().build();
        manager.createTree(tree);

        Long treeId = tree.getId();
        assertThat(treeId).isNotNull();

        assertThat(manager.getTree(treeId))
                .isNotSameAs(tree)
                .isEqualToComparingFieldByField(tree);
    }


    @Test
    public void findAllTrees() {

        assertThat(manager.findAllTrees()).isEmpty();

        Tree wilhelm = sampleWilhelmTreeBuilder().build();
        Tree bob = sampleBobTreeBuilder().build();

        manager.createTree(wilhelm);
        manager.createTree(bob);

        assertThat(manager.findAllTrees())
                .usingFieldByFieldElementComparator()
                .containsOnly(wilhelm,bob);
    }

    @Test(expected = ValidationException.class)
    public void createNullTree() {
        manager.createTree(null);
    }

    @Test
    public void createTreeWithExistingId() {
        Tree tree = sampleWilhelmTreeBuilder()
                .id(1L)
                .build();
        expectedException.expect(IllegalEntityException.class);
        manager.createTree(tree);
    }

    @Test
    public void createTreeWithNullName() {
        Tree tree = sampleWilhelmTreeBuilder()
                .name(null)
                .build();
        assertThatThrownBy(() -> manager.createTree(tree))
                .isInstanceOf(ValidationException.class);
    }

    @FunctionalInterface
    private static interface Operation<T> {
        void callOn(T subjectOfOperation);
    }


    private void testUpdateTree(Operation<Tree> updateOperation) {
        Tree treeForUpdate = sampleWilhelmTreeBuilder().build();
        Tree anotherTree = sampleBobTreeBuilder().build();
        manager.createTree(treeForUpdate);
        manager.createTree(anotherTree);

        updateOperation.callOn(treeForUpdate);

        manager.updateTree(treeForUpdate);
        assertThat(manager.getTree(treeForUpdate.getId()))
                .isEqualToComparingFieldByField(treeForUpdate);
        // Check if updates didn't affected other records
        assertThat(manager.getTree(anotherTree.getId()))
                .isEqualToComparingFieldByField(anotherTree);
    }

    @Test
    public void updateTreeName() {
        testUpdateTree((tree) -> tree.setName("Jozef"));
    }

    @Test
    public void updateTreeType() {
        testUpdateTree((tree) -> tree.setTreeType("jelsa"));
    }

    @Test
    public void updateTreeProtected() {
        testUpdateTree((tree) -> tree.setProtected(true));
    }


    @Test(expected = ValidationException.class)
    public void updateNullTree() {
        manager.updateTree(null);
    }

    @Test
    public void updateTreeWithNullId() {
        Tree tree = sampleWilhelmTreeBuilder().id(null).build();
        expectedException.expect(IllegalEntityException.class);
        manager.updateTree(tree);
    }

    @Test
    public void updateNonExistingTree() {
        Tree tree = sampleWilhelmTreeBuilder().id(1L).build();
        expectedException.expect(IllegalEntityException.class);
        manager.updateTree(tree);
    }

    @Test
    public void updateTreeWithNullName() {
        Tree tree = sampleWilhelmTreeBuilder().build();
        manager.createTree(tree);
        tree.setName(null);

        expectedException.expect(ValidationException.class);
        manager.updateTree(tree);
    }

    @Test
    public void deleteTree() {

        Tree wilhelm = sampleWilhelmTreeBuilder().build();
        Tree bob = sampleBobTreeBuilder().build();
        manager.createTree(wilhelm);
        manager.createTree(bob);

        assertThat(manager.getTree(wilhelm.getId())).isNotNull();
        assertThat(manager.getTree(bob.getId())).isNotNull();

        manager.deleteTree(wilhelm);

        assertThat(manager.getTree(wilhelm.getId())).isNull();
        assertThat(manager.getTree(bob.getId())).isNotNull();

    }


    @Test(expected = IllegalArgumentException.class)
    public void deleteNullTree() {
        manager.deleteTree(null);
    }

    @Test
    public void deleteTreeWithNullId() {
        Tree tree = sampleWilhelmTreeBuilder().id(null).build();
        expectedException.expect(IllegalEntityException.class);
        manager.deleteTree(tree);
    }

    @Test
    public void deleteNonExistingTree() {
        Tree tree = sampleWilhelmTreeBuilder().id(1L).build();
        expectedException.expect(IllegalEntityException.class);
        manager.deleteTree(tree);
    }



    private void testExpectedServiceFailureException(Operation<TreeManager> operation) throws SQLException {
        SQLException sqlException = new SQLException();
        DataSource failingDataSource = mock(DataSource.class);
        when(failingDataSource.getConnection()).thenThrow(sqlException);
        manager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> operation.callOn(manager))
                .isInstanceOf(ServiceFailureException.class)
                .hasCause(sqlException);
    }

    @Test
    public void updateTreeWithSqlExceptionThrown() throws SQLException {
        Tree tree = sampleWilhelmTreeBuilder().build();
        manager.createTree(tree);
        testExpectedServiceFailureException((treeManager) -> treeManager.updateTree(tree));
    }

    @Test
    public void getTreeWithSqlExceptionThrown() throws SQLException {
        Tree tree = sampleBobTreeBuilder().build();
        manager.createTree(tree);
        testExpectedServiceFailureException((treeManager) -> treeManager.getTree(tree.getId()));
    }

    @Test
    public void deleteTreeWithSqlExceptionThrown() throws SQLException {
        Tree tree = sampleWilhelmTreeBuilder().build();
        manager.createTree(tree);
        testExpectedServiceFailureException((treeManager) -> treeManager.deleteTree(tree));
    }

    @Test
    public void findAllTreesWithSqlExceptionThrown() throws SQLException {
        testExpectedServiceFailureException((treeManager) -> treeManager.findAllTrees());
    }

/*

    @Test
    public void updateTree() throws Exception {
        Tree tree = newTree("Maria","ceresna", true);
        Tree anotherTree = newTree("Ilona", "dub", false);
        manager.createTree(tree);
        manager.createTree(anotherTree);
        Long treeId = tree.getId();

        tree = manager.getTree(treeId);
        tree.setTreeType("Kveta");
        manager.updateTree(tree);
        assertEquals("Kveta", tree.getName());
        assertEquals("ceresna", tree.getTreeType());
        assertEquals(true, tree.isProtected());

        tree = manager.getTree(treeId);
        tree.setTreeType("buk");
        manager.updateTree(tree);
        assertEquals("Kveta", tree.getName());
        assertEquals("buk", tree.getTreeType());
        assertEquals(true, tree.isProtected());

        tree = manager.getTree(treeId);
        tree.setProtected(false);
        manager.updateTree(tree);
        assertEquals("Kveta", tree.getName());
        assertEquals("buk", tree.getTreeType());
        assertEquals(false, tree.isProtected());

        // Check if updates didn't affected other records
        assertDeepEquals(anotherTree, manager.getTree(anotherTree.getId()));
    }

    @Test
    public void deleteTree() throws Exception {
        Tree t1 = newTree("Jozef", "breza",false);
        Tree t2 = newTree("Juraj", "lipa", false);
        manager.createTree(t1);
        manager.createTree(t2);
        assertNotNull(manager.getTree(t1.getId()));
        assertNotNull(manager.getTree(t2.getId()));
        manager.deleteTree(t1);
        assertNull(manager.getTree(t1.getId()));
        assertNotNull(manager.getTree(t2.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullTree() {
        manager.createTree(null);
    }

    @Test
    public void updateTreeWithNullName() {
        Tree tree = newTree("Fero", "dub", true);
        manager.createTree(tree);
        tree.setName(null);
        expectedException.expect(IllegalArgumentException.class);
        manager.updateTree(tree);
    }

    @Test
    public void findAllTrees() throws Exception {

        assertTrue(manager.findAllTrees().isEmpty());

        Tree t1 = newTree("Renata","breza", false);
        Tree t2 = newTree("Viktor", "binary", true);

        manager.createTree(t1);
        manager.createTree(t2);

        List<Tree> expected = Arrays.asList(t1, t2);
        List<Tree> actual = manager.findAllTrees();

        Collections.sort(actual, TREE_ID_COMPARATOR);
        Collections.sort(expected, TREE_ID_COMPARATOR);

        assertEquals(expected, actual);
        assertDeepEquals(expected, actual);
    }

    private static Tree newTree(String name, String treeType, boolean isProtected) {
        Tree tree = new Tree();
        tree.setName(name);
        tree.setTreeType(treeType);
        tree.setProtected(isProtected);
        return tree;
    }

    private void assertDeepEquals(List<Tree> expectedList, List<Tree> actualList) {
        for (int i = 0; i < expectedList.size(); i++) {
            Tree expected = expectedList.get(i);
            Tree actual = actualList.get(i);
            assertDeepEquals(expected, actual);
        }
    }

    private void assertDeepEquals(Tree expected, Tree actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.isProtected(), actual.isProtected());
    }

    private static final Comparator<Tree> TREE_ID_COMPARATOR = Comparator.comparing(Tree::getId);
*/

}