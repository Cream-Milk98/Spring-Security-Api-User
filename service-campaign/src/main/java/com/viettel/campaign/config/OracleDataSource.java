package com.viettel.campaign.config;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @author anhvd_itsol
 */
public class OracleDataSource extends HikariDataSource {
    public OracleDataSource(){
        super.addDataSourceProperty("useSSL", "false");
        super.addDataSourceProperty("cachePrepStmts", "true");
        super.addDataSourceProperty("prepStmtCacheSize", "256");
        super.addDataSourceProperty("allowMultiQueries", "true");
        super.addDataSourceProperty("useServerPrepStmts", "false");
        super.addDataSourceProperty("useLocalSessionState", "true");
        super.addDataSourceProperty("prepStmtCacheSqlLimit", "81920");
        super.addDataSourceProperty("nullCatalogMeansCurrent", "true");
        super.addDataSourceProperty("rewriteBatchedStatements", "false");
        super.addDataSourceProperty("useOldAliasMetadataBehavior", "true");
        this.setIdleTimeout(1000000); this.setConnectionTimeout(1000000);  this.setMinimumIdle(0);
        this.setMaxLifetime(1000000); this.setInitializationFailTimeout(0);
    }
}

