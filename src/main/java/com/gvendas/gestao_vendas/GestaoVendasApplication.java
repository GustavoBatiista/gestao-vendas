package com.gvendas.gestao_vendas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = { "com.gvendas.gestao_vendas.entidades" })
@EnableJpaRepositories(basePackages = { "com.gvendas.gestao_vendas.repository" })
@ComponentScan(basePackages = { "com.gvendas.gestao_vendas.service","com.gvendas.gestao_vendas.controller","com.gvendas.gestao_vendas.exception" })
@SpringBootApplication
public class GestaoVendasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestaoVendasApplication.class, args);
	}

}
