
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

import java.time.LocalDateTime;
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
	@Autowired
	private PasswordEncoder passwordEncoder;


    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {
    	Map<String, Object> dto = new LinkedHashMap<>();
    	if (!this.isGuest(authentication)) {
			dto.put("player", playerRepository.findByEmail(authentication.getName()).playerDTO());
		}else {
			dto.put("player", "guest");
		}
    	dto.put("games", gameRepository.findAll().stream().map(Game::gameDTO).collect(Collectors.toList()));

    	return dto;
    }


	@RequestMapping("/scores")
	public List<Map<String, Object>> getScores() {
		return playerRepository.findAll().stream().map(Player::scoreDTO).collect(Collectors.toList());//QUE HACE ESTO?
	}


    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String,Object>> getGameView(@PathVariable long gamePlayerId, Authentication authentication){

    	ResponseEntity<Map<String,Object>> response;

        if(isGuest(authentication)){
        	response = new ResponseEntity<>(makeMap("error", "usuario no logueado"), HttpStatus.UNAUTHORIZED);
        }else{
        	GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
        	Player player = playerRepository.findByEmail(authentication.getName());
        	
        	if(gamePlayer==null){
        		response = new ResponseEntity<>(makeMap("error", "Usuario no valido"), HttpStatus.NOT_FOUND);
        	}else if(gamePlayer.getPlayer().getId() != player.getId()){
        		response = new ResponseEntity<>(makeMap("error", "este game no pertenece al usuario logueado"), HttpStatus.UNAUTHORIZED);
        	}else {
        		response = new ResponseEntity<>(this.gameViewDTO(gamePlayer), HttpStatus.OK);
        	}
        }
        return response;
    }

	//REGISTRO DE NUEVOS PLAYERS
	//##########################
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
			response = new ResponseEntity<>(makeMap("email", newPlayer.getEmail()), HttpStatus.CREATED);
		}
		return response;
	}

	//###############################################################
	@RequestMapping(path = "/games", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> createGames( Authentication authentication) {

		ResponseEntity<Map<String,Object>> response;
		if(this.isGuest(authentication)){
			response = new ResponseEntity<>(makeMap("error", "usuario no logueado"), HttpStatus.UNAUTHORIZED);
		}else{
			Player player= playerRepository.findByEmail(authentication.getName());
			Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
			GamePlayer newGamePlayer= gamePlayerRepository.save(new GamePlayer(player, newGame, newGame.getCreationDate()));

			response= new ResponseEntity<>(makeMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
		}
		return response;
	}


	@RequestMapping(path = "/games/{gameId}/players", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> joinGames(@PathVariable long gameId, Authentication authentication) {

		ResponseEntity<Map<String,Object>> response;
		Player player= playerRepository.findByEmail(authentication.getName());
		Game game = gameRepository.findById(gameId).orElse(null);

		if(this.isGuest(authentication)){
			response = new ResponseEntity<>(makeMap("error", "usuario no logueado"), HttpStatus.UNAUTHORIZED);
		}else{
			if(game==null){
				response = new ResponseEntity<>(makeMap("error", "No existe el game solicitado"), HttpStatus.NOT_FOUND);
			}else if(game.getGamePlayers().size()>1){
				response = new ResponseEntity<>(makeMap("error", "El game ya se encuentra completo"), HttpStatus.FORBIDDEN);
			}else{
				if(game.getGamePlayers().stream().anyMatch(gp -> gp.getPlayer().getId()==player.getId())){
					response = new ResponseEntity<>(makeMap("error", "No se puede jugar contra si mismo"), HttpStatus.FORBIDDEN);
				}else{
				GamePlayer newGamePlayer= gamePlayerRepository.save(new GamePlayer(player, game, LocalDateTime.now()));
				response= new ResponseEntity<>(makeMap("gpId", newGamePlayer.getId()), HttpStatus.CREATED);
				}
			}
		}
		return response;
	}

	//###############################################################

	private Map<String, Object> makeMap(String key, Object value) {
		Map<String, Object> map = new HashMap<>();
		map.put(key, value);
		return map;
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

}
