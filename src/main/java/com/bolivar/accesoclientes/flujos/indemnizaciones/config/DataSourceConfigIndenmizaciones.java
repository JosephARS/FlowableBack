package com.bolivar.accesoclientes.flujos.indemnizaciones.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "nuevoCasobdEntityManagerFactory", 
						basePackages = "com.bolivar.accesoclientes.flujos.indemnizaciones.util.repository",
						transactionManagerRef = "nuevoCasobdTransactionManager" )
public class DataSourceConfigIndenmizaciones {

	@Autowired
	private Environment env;
	

    @Bean(name = "dsIndemnizaciones")
    public DataSource indemnizacionesDataSource(){
    	DriverManagerDataSource datasource = new DriverManagerDataSource();
    	datasource.setUrl(env.getProperty("indemnizaciones.datasource.url"));
    	datasource.setUsername(env.getProperty("indemnizaciones.datasource.username"));
    	datasource.setPassword(env.getProperty("indemnizaciones.datasource.password"));
    	datasource.setDriverClassName(env.getProperty("indemnizaciones.datasource.driver-class-name"));
        return datasource;
    }
    
    @Bean(name = "nuevoCasobdEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(indemnizacionesDataSource());
		em.setPackagesToScan(new String[] { "com.bolivar.accesoclientes.flujos.indemnizaciones.util.model", "com.bolivar.accesoclientes.flujos.indemnizaciones.asignarUsuarioTarea.model" });
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.database", env.getProperty("indemnizaciones.jpa.database"));
		properties.put("hibernate.show-sql", env.getProperty("indemnizaciones.jpa.show-sql"));
		
		em.setJpaPropertyMap(properties);

    	
    	return em;
    	
    }
    
    @Bean(name = "nuevoCasobdTransactionManager")
    public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
    	
    	
    	return transactionManager;
    	
    }
    
    
}
