package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}


    @Bean
    public CommandLineRunner initData(PlayerRepository repository) {
        return (args) -> {
            // save a couple of customers
            repository.save(new Player("Jack", "j.bauer@ctu.gov","24"));
            repository.save(new Player("Chloe", "c.obrian@ctu.gov","42"));
            repository.save(new Player("Kim", "kim_bauer@gmail.com","kb"));
            repository.save(new Player("Tony", "t.almeida@ctu.gov","mole"));

        };
    }
}
