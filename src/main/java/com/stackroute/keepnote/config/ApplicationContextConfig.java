package com.stackroute.keepnote.config;

import com.stackroute.keepnote.model.Note;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/*This class will contain the application-context for the application.
 * Define the following annotations:
 * @Configuration - Annotating a class with the @Configuration indicates that the 
 *                  class can be used by the Spring IoC container as a source of 
 *                  bean definitions
 * @EnableTransactionManagement - Enables Spring's annotation-driven transaction management capability.
 *                  
 * */
@Configuration
@EnableTransactionManagement
public class ApplicationContextConfig {

	/*
	 * Define the bean for DataSource. In our application, we are using MySQL as the
	 * dataSource. To create the DataSource bean, we need to know: 1. Driver class
	 * name 2. Database URL 3. UserName 4. Password
	 */
	@Bean
	@Autowired
	public DataSource dataSource() {

		// Use this configuration while submitting solution in hobbes.
		// dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		// dataSource.setUrl("jdbc:mysql://" + System.getenv("MYSQL_HOST") + ":3306/" + System.getenv("MYSQL_DATABASE")
		// 		+"?verifyServerCertificate=false&useSSL=false&requireSSL=false");
		// dataSource.setUsername(System.getenv("MYSQL_USER"));
		// dataSource.setPassword(System.getenv("MYSQL_PASSWORD")); 

		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		basicDataSource.setUrl("jdbc:mysql://localhost:3306/mydatabase");
		basicDataSource.setUsername("root");
		basicDataSource.setPassword("Root@123");
		return basicDataSource;
	}

	/*
	 * Define the bean for SessionFactory. Hibernate SessionFactory is the factory
	 * class through which we get sessions and perform database operations.
	 */
	@Bean
	@Autowired
	public LocalSessionFactoryBean sessionFactory(DataSource dataSource)
	{
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("com.stackroute.keepnote");
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.show_sql", "true");
		hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.put("hibernate.dialect","org.hibernate.dialect.MySQL57Dialect");
		sessionFactory.setAnnotatedClasses(Note.class);
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;

//		SessionFactory sessionFactory=null;
//		try {
//			StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
//			Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
//			sessionFactory = metaData.getSessionFactoryBuilder().build();
//		}
//		catch (Throwable th) {
//
//			System.err.println("Initial SessionFactory creation failed" + th);
//			throw new ExceptionInInitializerError(th);
//
//		}
//		return sessionFactory;
	}
	/*
	 * Define the bean for Transaction Manager. HibernateTransactionManager handles
	 * transaction in Spring. The application that uses single hibernate session
	 * factory for database transaction has good choice to use
	 * HibernateTransactionManager. HibernateTransactionManager can work with plain
	 * JDBC too. HibernateTransactionManager allows bulk update and bulk insert and
	 * ensures data integrity.
	 */
	@Bean
	@Autowired
	public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory)
	{
		HibernateTransactionManager hibernateTransactionManager=new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory);
		return hibernateTransactionManager;
	}
}
