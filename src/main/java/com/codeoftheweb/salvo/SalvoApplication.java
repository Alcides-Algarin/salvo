package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Ship;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;

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


            //players Repository
            playerRepository.save(jack);
            playerRepository.save(chole);
            playerRepository.save(kim);
            playerRepository.save(tony);

            //games Repository
            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);


            GamePlayer gp1= gamePlayerRepository.save(new GamePlayer(jack,game1,LocalDateTime.now()));
            GamePlayer gp2= gamePlayerRepository.save(new GamePlayer(chole,game1,LocalDateTime.now()));

            GamePlayer gp3= gamePlayerRepository.save(new GamePlayer(chole,game2,LocalDateTime.now().plusHours(1)));
            GamePlayer gp4= gamePlayerRepository.save(new GamePlayer(tony,game2,LocalDateTime.now().plusHours(1)));

            GamePlayer gp5= gamePlayerRepository.save(new GamePlayer(kim,game3,LocalDateTime.now().plusHours(2)));
            GamePlayer gp6= gamePlayerRepository.save(new GamePlayer(jack,game3,LocalDateTime.now().plusHours(2)));

            gp1.addShip(new Ship("destroyer", Arrays.asList("A1","A2","A3")));
            gp1.addShip(new Ship("submarine", Arrays.asList("C1","C2","C3","C4")));
            gp1.addShip(new Ship("patrol_boat", Arrays.asList("F7","G7")));
            gp1.addShip(new Ship("carrier", Arrays.asList("A10","B10","C10","D10","E10")));
            gp1.addShip(new Ship("battleship", Arrays.asList("I1","I2","I3")));

            gp2.addShip(new Ship("destroyer", Arrays.asList("H1","I1","J1")));
            gp2.addShip(new Ship("submarine", Arrays.asList("D4","D5","D6","D7")));

            gamePlayerRepository.save(gp1);
            gamePlayerRepository.save(gp2);
            gamePlayerRepository.save(gp3);
            gamePlayerRepository.save(gp4);
            gamePlayerRepository.save(gp5);
            gamePlayerRepository.save(gp6);


            /*
	        @Bean
	        public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
		    return (args) -> {

			Player jack = playerRepository.save(new Player("j.bauer@ctu.gov", "Jack", "Bauer"));
			Player chloe = playerRepository.save(new Player("c.obrian@ctu.gov", "Chloe", "O'Brian"));
			Player kim = playerRepository.save(new Player("kim_bauer@gmail.com", "Kim", "Bauer"));
			Player tony = playerRepository.save(new Player("t.almeida@ctu.gov", "Tony", "Almeida"));

			Game game1 = gameRepository.save(new Game(LocalDateTime.now()));
			Game game2 = gameRepository.save(new Game(LocalDateTime.now().plusHours(1)));
			Game game3 = gameRepository.save(new Game(LocalDateTime.now().plusHours(2)));

			GamePlayer gp1 = gamePlayerRepository.save(new GamePlayer(game1,jack,LocalDateTime.now()));
			GamePlayer gp2 = gamePlayerRepository.save(new GamePlayer(game1,chloe,LocalDateTime.now()));
			GamePlayer gp3 = gamePlayerRepository.save(new GamePlayer(game2,kim,LocalDateTime.now()));
			GamePlayer gp4 = gamePlayerRepository.save(new GamePlayer(game2,tony,LocalDateTime.now()));
			GamePlayer gp5 = gamePlayerRepository.save(new GamePlayer(game3,jack,LocalDateTime.now()));

			gp1.addShip(new Ship("destroyer", Arrays.asList("A1","A2","A3")));
			gp1.addShip(new Ship("submarine", Arrays.asList("C1","C2","C3","C4")));
			gp1.addShip(new Ship("patrol_boat", Arrays.asList("F7","G7")));
			gp1.addShip(new Ship("carrier", Arrays.asList("A10","B10","C10","D10","E10")));
			gp1.addShip(new Ship("battleship", Arrays.asList("I1","I2","I3")));

			gp2.addShip(new Ship("destroyer", Arrays.asList("H1","I1","J1")));
			gp2.addShip(new Ship("submarine", Arrays.asList("D4","D5","D6","D7")));

			gamePlayerRepository.save(gp1);
			gamePlayerRepository.save(gp2);

		};
	}
            */

        };
    }
}
