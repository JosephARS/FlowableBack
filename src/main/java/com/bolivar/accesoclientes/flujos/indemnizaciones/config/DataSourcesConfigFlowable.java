package com.bolivar.accesoclientes.flujos.indemnizaciones.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(entityManagerFactoryRef = "flowEntityManagerFactory", 
//						basePackages = "com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model",
//						transactionManagerRef = "flowTransactionManager" )
public class DataSourcesConfigFlowable {

	@Autowired
	private Environment env;
	
    //    @Bean(name = "dsFlowable")
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource flowableDataSource(){
//        return DataSourceBuilder.create().build();
//    }
    
    
//    @Bean(name = "dsIndemnizaciones")
//    @ConfigurationProperties(prefix = "indemnizaciones.datasource")
//    public DataSource indemnizacionesDataSource(){
//        return DataSourceBuilder.create().build();
//    }
    
    @Bean(name = "dsFlow")
    @Primary
    public DataSource flowableDataSource(){
    	DriverManagerDataSource datasource = new DriverManagerDataSource();
    	datasource.setUrl(env.getProperty("spring.datasource.url"));
    	datasource.setUsername(env.getProperty("spring.datasource.username"));
    	datasource.setPassword(env.getProperty("spring.datasource.password"));
    	datasource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        return datasource;
    }
    
//    @Bean(name = "flowEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//		em.setDataSource(flowDataSource());
//		em.setPackagesToScan("com.bolivar.accesoclientes.flujos.indemnizaciones.crearcaso.model");
//		
//		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//		em.setJpaVendorAdapter(vendorAdapter);
//		
//		Map<String, Object> properties = new HashMap<>();
//		properties.put("hibernate.database", env.getProperty("spring.jpa.database"));
//		properties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
//		
//		em.setJpaPropertyMap(properties);
//
//    	
//    	return em;
//    	
//    }
//    
//    @Bean(name = "flowTransactionManager")
//    public PlatformTransactionManager transactionManager() {
//		JpaTransactionManager transactionManager = new JpaTransactionManager();
//		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//    	
//    	
//    	return transactionManager;
//    	
//    }
    
    
}
