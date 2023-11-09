package org.telegram.toto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "org.telegram.toto.repository")
@EnableTransactionManagement(order = 70)
@Configuration
public class JpaConfig {

}
