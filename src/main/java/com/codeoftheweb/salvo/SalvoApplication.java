package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}


    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
        return (args) -> {

            //Players
            Player jack= new Player("Jack", "j.bauer@ctu.gov","24");
            Player chole= new Player("Chloe", "c.obrian@ctu.gov","42");
            Player kim= new Player("Kim", "kim_bauer@gmail.com","kb");
            Player tony= new Player("Tony", "t.almeida@ctu.gov","mole");

            //Games
            Game game1=new Game(LocalDateTime.now());
            Game game2=new Game(LocalDateTime.now().plusHours(1));
            Game game3=new Game(LocalDateTime.now().plusHours(2));


            //players
            playerRepository.save(jack);
            playerRepository.save(chole);
            playerRepository.save(kim);
            playerRepository.save(tony);

            //games
            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);


            gamePlayerRepository.save(new GamePlayer(jack,game1,LocalDateTime.now()));
            gamePlayerRepository.save(new GamePlayer(chole,game1,LocalDateTime.now()));

            gamePlayerRepository.save(new GamePlayer(chole,game2,LocalDateTime.now().plusHours(1)));
            gamePlayerRepository.save(new GamePlayer(tony,game2,LocalDateTime.now().plusHours(1)));

            gamePlayerRepository.save(new GamePlayer(kim,game3,LocalDateTime.now().plusHours(2)));
            gamePlayerRepository.save(new GamePlayer(jack,game3,LocalDateTime.now().plusHours(2)));


        };
    }
}
