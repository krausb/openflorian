package de.openflorian.data;

/*
 * This file is part of Openflorian.
 * 
 * Copyright (C) 2015  Bastian Kraus
 * 
 * Openflorian is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version)
 *     
 * Openflorian is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *     
 * You should have received a copy of the GNU General Public License
 * along with Openflorian.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Persistence JPA Configuration Bean<br/>
 * <br/>
 * Notice: Replaces hibernate xml and persistence xml.
 * <br/>
 * Requires {@link DataSource} with name {@link PersistenceJPAConfig#JNDI_DATASOURCE} from {@link InitialContext}.
 * 
 * @author Bastian Kraus <me@bastian-kraus.me>
 */
@Configuration
@EnableTransactionManagement
public class PersistenceJPAConfig {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String JNDI_DATASOURCE = "java:/comp/env/jdbc/openflorian";

	/**
	 * EntityManager Factory Bean
	 * 
	 * @return
	 * 		{@link LocalContainerEntityManagerFactoryBean}
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "de.openflorian.data.model", "de.openflorian.web.user", "de.openflorian.media.model" });

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());
		return em;
	}

	/**
	 * Obtain DataSource from JNDI Initial Context.
	 * 
	 * @return
	 * 		{@link DataSource} from container context
	 */
	@Bean
	public DataSource dataSource() {
		DataSource dataSource = null;

		try {
			InitialContext context = new InitialContext();
			dataSource = (DataSource) context.lookup(JNDI_DATASOURCE);
		} catch (NamingException e) {
				log.error(e.getMessage(), e);
		}
		return dataSource;
	}

	/**
	 * Platform Transaction Manager Factory
	 * 
	 * @param emf
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	/**
	 * Platform Transaction Manager Factory
	 * 
	 * @return
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	/**
	 * Compose required JPA / Hibernate Properties
	 * 
	 * @return
	 * 		{@link Properties}
	 */
	protected Properties additionalProperties() {
		Properties properties = new Properties();
		// TODO: Change to 'none' for release
		properties.setProperty("hibernate.hbm2ddl.auto", "update");
		properties.setProperty("hibernate.default_schema", "openflorian");
		properties.setProperty("hibernate.dialect",
				"org.hibernate.dialect.MySQL5Dialect");
		return properties;
	}

}
