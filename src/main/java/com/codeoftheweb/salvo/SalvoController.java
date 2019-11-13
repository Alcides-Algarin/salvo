
package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.*;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ScoreRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ScoreRepository scoreRepository;

	@Autowired
	private PlayerRepository playerRepository;

    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
    	Map<String, Object> dto = new HashMap<>();
    	if (!this.isGuest(authentication)) {
			dto.put("Player", playerRepository.findByEmail(authentication.getName()).playerDTO());
		}else {
			dto.put("player", "guest");
		}
    	dto.put("games", gameRepository.findAll().stream().map(Game::gameDTO).collect(Collectors.toList()));
    	return dto;
        //return gameRepository.findAll().stream().map(Game::gameDTO).collect(Collectors.toList());
    }

	@RequestMapping("/scores")
	public List<Map<String, Object>> getScores() {
		return playerRepository.findAll().stream().map(Player::scoreDTO).collect(Collectors.toList());//QUE HACE ESTO?
	}

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String,Object> getGameView(@PathVariable long gamePlayerId){
        return this.gameViewDTO(gamePlayerRepository.findById(gamePlayerId).orElse(null));
    }

    private boolean isGuest(Authentication authentication){

    	return authentication == null || authentication instanceof AnonymousAuthenticationToken;

	}

    private Map<String,Object> gameViewDTO(GamePlayer gamePlayer){
        Map<String,Object> dto = new LinkedHashMap<>();

        if(gamePlayer != null){
            dto.put("gameId", gamePlayer.getGame().getId());
            dto.put("creationDate", gamePlayer.getGame().getCreationDate());
            dto.put("gamePlayer", gamePlayer.getGame().getGamePlayers().stream().map(GamePlayer::gamePlayerDTO));
            dto.put("player", gamePlayer.getPlayer().getName());
            dto.put("ships", gamePlayer.getShips().stream().map(Ship::shipDTO));
            dto.put("salvos", gamePlayer.getGame().getGamePlayers().stream().flatMap(gp -> gp.getSalvoes().stream().map(Salvo::salvoDTO)));
        }else{
            dto.put("error", "no such game game");
        }
        return dto;
    }


	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(path = "/players", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> createUser(@RequestParam String userName, @RequestParam String email, @RequestParam String password) {

		ResponseEntity<Map<String,Object>> response;
		Player player = playerRepository.findByEmail(email);

		if (email.isEmpty() || password.isEmpty()) {
			response = new ResponseEntity<>(makeMap(" Error", "no name or password"), HttpStatus.FORBIDDEN);

		}else if (player !=  null) {
			response = new ResponseEntity<>(makeMap(" Error", "userName already exists"), HttpStatus.FORBIDDEN);
		}else{
			Player newPlayer=  playerRepository.save(new Player(userName, email, passwordEncoder.encode(password)));
			response = new ResponseEntity<>(makeMap("id", newPlayer.getId()), HttpStatus.CREATED);
		}

		return response;
	}

	private Map<String, Object> makeMap(String key, Object value) {
		Map<String, Object> map = new HashMap<>();
		map.put(key, value);
		return map;
	}

	/**
	 @RequestMapping(path = "/players", method = RequestMethod.POST)
	 public ResponseEntity<Object> register(@RequestParam String name, @RequestParam String email, @RequestParam String password) {

	 if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
	 return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
	 }

	 if (playerRepository.findByEmail(email) !=  null) {
	 return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
	 }

	 playerRepository.save(new Player(name ,email, passwordEncoder.encode(password)));
	 return new ResponseEntity<>(HttpStatus.CREATED);
	 }
	 */

}
