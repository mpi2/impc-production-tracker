package org.gentar.framework;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Class created to remove warnings while running tests:
 * "Potential problem found: The configured data type factory
 * 'class org.dbunit.dataset.datatype.DefaultDataTypeFactory' might cause problems with the current
 * database 'H2' (e.g. some datatypes may not be supported properly).
 * Solution taken from https://stackoverflow.com/questions/27652689/spring-test-dbunit-warning
 */

@Configuration
public class DBUnitConfig
{
    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
        DatabaseConfigBean bean = new DatabaseConfigBean();
        bean.setDatatypeFactory(new H2DataTypeFactory());

        DatabaseDataSourceConnectionFactoryBean dbConnectionFactory = new DatabaseDataSourceConnectionFactoryBean(dataSource);
        dbConnectionFactory.setDatabaseConfig(bean);
        return dbConnectionFactory;
    }
}
