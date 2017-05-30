package cz.muni.fi.pv168.forrest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.DERBY;

/**
 * Spring config for tests. See
 * <ul>
 *  <li><a href="http://static.springsource.org/spring/docs/current/spring-framework-reference/html/beans.html#beans-java">Java-based container configuration</a></li>
 *  <li><a href="http://static.springsource.org/spring/docs/current/spring-framework-reference/html/testing.html#testcontext-tx">Testing - Transaction management</a></li>
 *  <li><a href="http://static.springsource.org/spring/docs/current/spring-framework-reference/html/jdbc.html#jdbc-embedded-database-dao-testing">Testing data access logic with an embedded database</a></li>
 * </ul>
 */
@Configuration
@EnableTransactionManagement
public class MySpringTestConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(DERBY)
                .setScriptEncoding("utf-8")
                .addScript("classpath:schema-javadb.sql")
                .addScript("classpath:test-data.sql")
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public TreeManager treeManager() {
        return new TreeManagerImpl(dataSource());
    }

    @Bean
    public PotManager potManager() {
        return new PotManagerImpl(dataSource());
    }

    @Bean
    public PlanterManager planterManager() {
        PlanterManagerImpl planterManager = new PlanterManagerImpl(new TransactionAwareDataSourceProxy(dataSource()));
        // PlanterManagerImpl nepoužívá Spring JDBC, musíme mu vnutit spolupráci se Spring transakcemi !
        planterManager.setTreeManager(treeManager());
        planterManager.setPotManager(potManager());
        return planterManager;
    }
}