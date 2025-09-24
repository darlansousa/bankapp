package br.com.darlansilva.bankapp.dataprovider.database.config;

import static br.com.darlansilva.bankapp.dataprovider.database.config.DatabaseSourceContext.READ;
import static br.com.darlansilva.bankapp.dataprovider.database.config.DatabaseSourceContext.WRITE;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
public class DatasourceConfig {

    @Bean(name = "writeDataSource")
    @ConfigurationProperties("spring.datasource.write.hikari")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "readDataSource")
    @ConfigurationProperties("spring.datasource.read.hikari")
    public DataSource readDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public DataSource routingDataSource(
            @Qualifier("writeDataSource") DataSource writeDs,
            @Qualifier("readDataSource") DataSource readDs) {

        Map<Object, Object> map = new HashMap<>();
        map.put(WRITE, writeDs);
        map.put(READ,  readDs);

        var routing = new RoutingDataSource();
        routing.setDefaultTargetDataSource(writeDs);
        routing.setTargetDataSources(map);
        return routing;
    }

    @Bean
    @Primary
    public DataSource appDataSource(DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(@Qualifier("appDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean(name = "readTxManager")
    public PlatformTransactionManager readTxManager(@Qualifier("readDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean(name = "writeTxManager")
    public PlatformTransactionManager writeTxManager(@Qualifier("writeDataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}
