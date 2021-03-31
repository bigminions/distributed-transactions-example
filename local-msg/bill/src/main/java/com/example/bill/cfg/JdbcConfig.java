package com.example.bill.cfg;

import com.example.bill.entity.Bill;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.relational.core.mapping.event.BeforeSaveEvent;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {

    private AtomicInteger atomicInteger = new AtomicInteger(10000);

    @Bean
    ApplicationListener<BeforeSaveEvent> idSetting() {
        return beforeSaveEvent -> {
            if (beforeSaveEvent.getEntity() instanceof Bill) {
                ((Bill) beforeSaveEvent.getEntity()).setId(atomicInteger.incrementAndGet());
            }
        };
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcOperations operations) {
        return new NamedParameterJdbcTemplate(operations);
    }

    @Bean
    TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    DataSourceInitializer initializer(DataSource dataSource) {

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        ClassPathResource script = new ClassPathResource("schema.sql");
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(script);
        initializer.setDatabasePopulator(populator);

        return initializer;
    }

}
