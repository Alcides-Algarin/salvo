package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}


    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ScoreRepository scoreRepository) {
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
            Game game4=new Game(LocalDateTime.now().plusHours(3));
            Game game5=new Game(LocalDateTime.now().plusHours(4));
            Game game6=new Game(LocalDateTime.now().plusHours(5));
            Game game7=new Game(LocalDateTime.now().plusHours(6));
            Game game8=new Game(LocalDateTime.now().plusHours(7));
            Game game9=new Game(LocalDateTime.now().plusHours(8));


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

            // ship location GAMEPLAYER1
            gp1.addShip(new Ship("destroyer", Arrays.asList("A1","A2","A3")));
            gp1.addShip(new Ship("submarine", Arrays.asList("C1","C2","C3","C4")));
            gp1.addShip(new Ship("patrol_boat", Arrays.asList("F7","G7")));
            gp1.addShip(new Ship("carrier", Arrays.asList("A10","B10","C10","D10","E10")));
            gp1.addShip(new Ship("battleship", Arrays.asList("I1","I2","I3")));

            // ship location GAMEPLAYER2
            gp2.addShip(new Ship("destroyer", Arrays.asList("H1","I1","J1")));
            gp2.addShip(new Ship("submarine", Arrays.asList("D4","D5","D6","D7")));
            gp2.addShip(new Ship("patrol_boat", Arrays.asList("F7","G7")));
            gp2.addShip(new Ship("carrier", Arrays.asList("A10","B10","C10","D10","E10")));
            gp2.addShip(new Ship("battleship", Arrays.asList("I4","I5","I6")));

            // ship location GAMEPLAYER4
            gp3.addShip(new Ship("destroyer", Arrays.asList("A1","A2","A3")));
            gp3.addShip(new Ship("submarine", Arrays.asList("C1","C2","C3","C4")));
            gp3.addShip(new Ship("patrol_boat", Arrays.asList("F7","G7")));
            gp3.addShip(new Ship("carrier", Arrays.asList("A10","B10","C10","D10","E10")));
            gp3.addShip(new Ship("battleship", Arrays.asList("I1","I2","I3")));

            // ship location GAMEPLAYER4
            gp4.addShip(new Ship("destroyer", Arrays.asList("A1","A2","A3")));
            gp4.addShip(new Ship("submarine", Arrays.asList("C1","C2","C3","C4")));
            gp4.addShip(new Ship("patrol_boat", Arrays.asList("F7","G7")));
            gp4.addShip(new Ship("carrier", Arrays.asList("A10","B10","C10","D10","E10")));
            gp4.addShip(new Ship("battleship", Arrays.asList("I1","I2","I3")));

            // ship location GAMEPLAYER5
            gp5.addShip(new Ship("destroyer", Arrays.asList("A1","A2","A3")));
            gp5.addShip(new Ship("submarine", Arrays.asList("C1","C2","C3","C4")));
            gp5.addShip(new Ship("patrol_boat", Arrays.asList("F7","G7")));
            gp5.addShip(new Ship("carrier", Arrays.asList("A10","B10","C10","D10","E10")));
            gp5.addShip(new Ship("battleship", Arrays.asList("I1","I2","I3")));

            // ship location GAMEPLAYER4
            gp6.addShip(new Ship("destroyer", Arrays.asList("I1","I2","I3")));
            gp6.addShip(new Ship("submarine", Arrays.asList("A9","B9","C9","D9")));
            gp6.addShip(new Ship("patrol_boat", Arrays.asList("F7","G7")));
            gp6.addShip(new Ship("carrier", Arrays.asList("A10","B10","C10","D10","E10")));
            gp6.addShip(new Ship("battleship", Arrays.asList("A1","A2","A3")));


            //salvo location GAMEPLAYER!
            gp1.addSalvo(new Salvo(1,Arrays.asList("I1","I2","I6","J5","A3")));
            gp1.addSalvo(new Salvo(2,Arrays.asList("H1","A2","F3","E5","G3")));
            gp1.addSalvo(new Salvo(3,Arrays.asList("A1","J2","I3","D10","D3")));

            gp2.addSalvo(new Salvo(1,Arrays.asList("D1","C2","D10","J3","D5")));
            gp2.addSalvo(new Salvo(2,Arrays.asList("E1","D2","F5","J5","D3")));



            scoreRepository.save(new Score(jack, game1, LocalDateTime.now(),1));
            scoreRepository.save(new Score(chole, game1, LocalDateTime.now(),0));

            scoreRepository.save(new Score(chole, game2, LocalDateTime.now(),1));
            scoreRepository.save(new Score(tony, game2, LocalDateTime.now(),0));

            scoreRepository.save(new Score(kim,game3,LocalDateTime.now().plusHours(2), 0.5));
            //scoreRepository.save(new Score(jack,game3,LocalDateTime.now().plusHours(2), 0.5));

            gamePlayerRepository.save(gp1);
            gamePlayerRepository.save(gp2);
            gamePlayerRepository.save(gp3);
            gamePlayerRepository.save(gp4);
            gamePlayerRepository.save(gp5);
            gamePlayerRepository.save(gp6);



        };
    }
}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Player player = playerRepository.findByEmail(inputName);
            if (player != null) {
                return new User(player.getEmail(), player.getPassword(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        });
    }
}
