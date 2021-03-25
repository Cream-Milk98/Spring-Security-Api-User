package com.viettel.campaign.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @author anhvd_itsol
 */

@Configuration
@PropertySource({"classpath:application.yml"})
@EnableJpaRepositories(
        basePackages = "com.viettel.campaign.repository.ccms_full",
        entityManagerFactoryRef = "ccmsFullEntityManager",
        transactionManagerRef = DataSourceQualify.CCMS_FULL
)
public class CCMSFullDatasourceConfig {

    @Autowired
    private Environment env;

    @Value("${datasource-property.ccms-full.driver-class-name}")
    private String driverClassName;

    @Value("${datasource-property.ccms-full.url}")
    private String url;

    @Value("${datasource-property.ccms-full.max_pool_size}")
    private Integer maxPoolSize;

    @Value("${datasource-property.ccms-full.password}")
    private String password;

    @Value("${datasource-property.ccms-full.username}")
    private String username;

    @Bean(name = "datasource.datasource1")
    public DataSource dataSource() {
        OracleDataSource r = new OracleDataSource();
        r.setDriverClassName(driverClassName);
        r.setPoolName("app.datasource1");
        r.setJdbcUrl(url);
        r.setMaximumPoolSize(maxPoolSize);
        r.setPassword(password);
        r.setUsername(username);
        return r;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean ccmsFullEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[]{"com.viettel.campaign.model.ccms_full"});
        em.setPersistenceUnitName(DataSourceQualify.JPA_UNIT_NAME_CCMS_FULL); // Important !!

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = DataSourceQualify.CCMS_FULL)
    public PlatformTransactionManager ccmsFullTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(ccmsFullEntityManager().getObject());

        return transactionManager;
    }

    @Bean(name = DataSourceQualify.NAMED_JDBC_PARAMETER_TEMPLATE_CCMS_FULL)
    @DependsOn("datasource.datasource1")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("datasource.datasource1") DataSource abcDataSource) {
        return new NamedParameterJdbcTemplate(abcDataSource);
    }

    @Bean(name = DataSourceQualify.CCMS_FULL_CHAINED)
    public ChainedTransactionManager transactionManager(@Qualifier(DataSourceQualify.ACD_FULL) PlatformTransactionManager ds1) {
        return new ChainedTransactionManager(ds1);
    }
}
