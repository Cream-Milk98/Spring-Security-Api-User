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
        basePackages = "com.viettel.campaign.repository.acd_full",
        entityManagerFactoryRef = "acdFullEntityManager",
        transactionManagerRef = DataSourceQualify.ACD_FULL
)
public class ACDFullDatasourceConfig {

    @Autowired
    private Environment env;

    @Value("${datasource-property.acd-full.driver-class-name}")
    private String driverClassName;

    @Value("${datasource-property.acd-full.url}")
    private String url;

    @Value("${datasource-property.acd-full.max_pool_size}")
    private Integer maxPoolSize;

    @Value("${datasource-property.acd-full.password}")
    private String password;

    @Value("${datasource-property.acd-full.username}")
    private String username;

    @Bean(name = "datasource.datasource2")
    public DataSource dataSource() {
        OracleDataSource r = new OracleDataSource();
        r.setDriverClassName(driverClassName);
        r.setPoolName("app.datasource2");
        r.setJdbcUrl(url);
        r.setMaximumPoolSize(maxPoolSize);
        r.setPassword(password);
        r.setUsername(username);
        return r;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean acdFullEntityManager() {
        LocalContainerEntityManagerFactoryBean em2 = new LocalContainerEntityManagerFactoryBean();
        em2.setDataSource(dataSource());
        em2.setPackagesToScan(new String[]{"com.viettel.campaign.model.acd_full"});
        em2.setPersistenceUnitName(DataSourceQualify.JPA_UNIT_NAME_ACD_FULL); // Important !!

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em2.setJpaVendorAdapter(vendorAdapter);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
        em2.setJpaPropertyMap(properties);

        return em2;
    }

    @Bean(name = DataSourceQualify.ACD_FULL)
    public PlatformTransactionManager acdFullTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(acdFullEntityManager().getObject());

        return transactionManager;
    }

    @Bean(name = DataSourceQualify.NAMED_JDBC_PARAMETER_TEMPLATE_ACD_FULL)
    @DependsOn("datasource.datasource2")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("datasource.datasource2") DataSource abcDataSource) {
        return new NamedParameterJdbcTemplate(abcDataSource);
    }

    @Bean(name = DataSourceQualify.CHAINED)
    public ChainedTransactionManager transactionManager(@Qualifier(DataSourceQualify.ACD_FULL) PlatformTransactionManager ds1,
                                                        @Qualifier(DataSourceQualify.CCMS_FULL) PlatformTransactionManager ds2) {
        return new ChainedTransactionManager(ds1, ds2);
    }
}

