
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
import java.util.*;
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
		return playerRepository.findAll()
				.stream()
				.map(Player::scoreDTO)
				.collect(Collectors.toList());//QUE HACE ESTO?
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


	@RequestMapping(path = "/players", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> createUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {

		ResponseEntity<Map<String,Object>> response;
		Player player = playerRepository.findByEmail(email);

		if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
			response = new ResponseEntity<>(makeMap("error", "Por favor complete todos los campos"), HttpStatus.FORBIDDEN);

		}else if (player !=  null) {
			response = new ResponseEntity<>(makeMap("error", "Ya existe el usuario con el que intenta registrarse"), HttpStatus.FORBIDDEN);
		}else{
			Player newPlayer=  playerRepository.save(new Player(username, email, passwordEncoder.encode(password)));
			response = new ResponseEntity<>(makeMap("email", newPlayer.getEmail()), HttpStatus.CREATED);
		}
		return response;
	}


	public ResponseEntity<Map<String,Object>> createGames( Authentication authentication) {

		ResponseEntity<Map<String,Object>> response;
		if(this.isGuest(authentication)){
			response = new ResponseEntity<>(makeMap("error", "Debe loguearse"), HttpStatus.UNAUTHORIZED);
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
				response = new ResponseEntity<>(makeMap("error", "No existe el Game solicitado"), HttpStatus.NOT_FOUND);
			}else if(game.getGamePlayers().size()>1){
				response = new ResponseEntity<>(makeMap("error", "El Game ya se encuentra completo"), HttpStatus.FORBIDDEN);
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


	@RequestMapping(path="/games/players/{gamePlayerId}/salvos", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> addSalvo(Authentication authentication, @PathVariable long gamePlayerId, @RequestBody List <String> salvoes) {
		ResponseEntity<Map<String,Object>> response;
		if(this.isGuest(authentication)){
			response = new ResponseEntity<>(makeMap("error", "usuario no logueado"), HttpStatus.UNAUTHORIZED);
		}else {
			GamePlayer gamePlayer= gamePlayerRepository.findById(gamePlayerId).orElse(null);
			Player player= playerRepository.findByEmail(authentication.getName());//El player actual
			assert gamePlayer != null;// En caso de que "gamePLayer" sea NULL, se corta la ejecucion del programa

			if(gamePlayer.getGame().getGamePlayers().size() == 2){//Controlar que efectivamente existan dos Player en el Game.
				// Obtengo el gamePlayer del oponente
				GamePlayer oponentGP= gamePlayer.getGame().getGamePlayers().stream().filter(gp -> gp.getId()!= gamePlayer.getId()).findFirst().orElse(null);
				int myTurn = gamePlayer.getSalvoes().size();
				int enemyTurn= oponentGP.getSalvoes().size();

				GamePlayer creadorJuego = gamePlayer.getId() < oponentGP.getId()? gamePlayer : oponentGP;
				boolean gPlayer1= creadorJuego.getId() < oponentGP.getId();

				if(gamePlayer.getPlayer().getId() != player.getId()){// El player debe pertenecer al este GP
					response = new ResponseEntity<>(makeMap("error", "usuario no perteneces a este game"), HttpStatus.UNAUTHORIZED);
				}else if(salvoes.size() != 5){
					//Mas adelante poner como condicion que la cantidad de disparos sea igual a la cantidad de ships vivos.
					response = new ResponseEntity<>(makeMap("error", "Los salvos recibidos no son correctos"), HttpStatus.FORBIDDEN);
				}else if (gamePlayer.shipSunked().size() !=5 || oponentGP.shipSunked().size()!=5){
					
					//Es una logica un poco rebuscada, pero funciona.
					// La idea es que el credor del juego sea el primero en disparar
					//
					//Mas adelante debe ser mejorado
					if(myTurn == enemyTurn && gPlayer1){//Siempre que los turnos sean iguales, el creador del juego es quien va a disparar
						System.out.println("Entro en el primer if del controler ");
						Salvo salvo = new Salvo(myTurn, salvoes); //Se crea una instancia de salvo
						gamePlayer.addSalvo(salvo); //se agrega un salvo al gamePlayer
						gamePlayerRepository.save(gamePlayer); //Guarda el gamePlayer con los nuevos salvos
						response = new ResponseEntity<>(makeMap("success", "salvo recibido con exito"), HttpStatus.CREATED); //Respuesta del server exitosa

					}else if(myTurn < enemyTurn && !gPlayer1){// Si los tunos no son iguales, dispara el segundo jugador(el que se unio al juego)
						System.out.println("Entro en el segundo if del controler ");
						Salvo salvo = new Salvo(myTurn, salvoes); //Se crea una instancia de salvo
						gamePlayer.addSalvo(salvo); //se agrega un salvo al gamePlayer
						gamePlayerRepository.save(gamePlayer); //Guarda el gamePlayer con los nuevos salvos
						response = new ResponseEntity<>(makeMap("success", "salvo recibido con exito"), HttpStatus.CREATED); //Respuesta del server exitosa
					}else{
						response = new ResponseEntity<>(makeMap("error", "Es turno del oponente"), HttpStatus.NOT_FOUND);
					}
				}else{
					//NOTA para mi yo del futuro:
					//Cambiar la logica de finalizacion del juego.
					//Por el momento el juego solo finaliza cuando los dos jugadores tienen todos sus barcos undidos.
					// y los puntajes los esta calculando dos veces( una vez por cada jugador)

					if(gamePlayer.shipSunked().size() == 5 && oponentGP.shipSunked().size()!=5){
						scoreRepository.save(new Score( gamePlayer.getPlayer(),gamePlayer.getGame(), LocalDateTime.now(), 0));
						scoreRepository.save(new Score( oponentGP.getPlayer(),oponentGP.getGame(), LocalDateTime.now(), 1));
					}else if(gamePlayer.shipSunked().size() !=5 && oponentGP.shipSunked().size()==5){
						scoreRepository.save(new Score( gamePlayer.getPlayer(),gamePlayer.getGame(), LocalDateTime.now(), 1));
						scoreRepository.save(new Score( oponentGP.getPlayer(),oponentGP.getGame(), LocalDateTime.now(), 0));
					}else{
						scoreRepository.save(new Score( gamePlayer.getPlayer(),gamePlayer.getGame(), LocalDateTime.now(), 0.5));
						scoreRepository.save(new Score( oponentGP.getPlayer(),oponentGP.getGame(), LocalDateTime.now(), 0.5));
					}
					response = new ResponseEntity<>(makeMap("error", " El juego finalizo"), HttpStatus.FORBIDDEN);
				}
			}else{
				response = new ResponseEntity<>(makeMap("error", "Falta un oponente para poder jugar"), HttpStatus.NOT_FOUND);
			}

		}
		return response;
	}


	@RequestMapping(path="/games/players/{gamePlayerId}/ships", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> addShip(Authentication authentication, @PathVariable long gamePlayerId, @RequestBody List <Ship> ships) {

		ResponseEntity<Map<String,Object>> response;
		GamePlayer gamePlayer= gamePlayerRepository.findById(gamePlayerId).orElse(null);
		Player player= playerRepository.findByEmail(authentication.getName());

		//Guardo en una lista todas la ubicaciones permitidas
		//FALTA DESARROLLAR
		/*
		List<String> legacyLocations = new ArrayList<String>();

		for (int i=0 ; i<10 ; i++ ) {
		    for(int j=1 ; j<11 ; j++){
		        
                char b = (char) (i + 65);
                String c = b + ""+ j;
                legacyLocations.add(c);
		    }
		}
		 */


		if(this.isGuest(authentication)){
			response = new ResponseEntity<>(makeMap("error", "usuario no logueado"), HttpStatus.UNAUTHORIZED);
		}else {
			if(gamePlayer == null) {
				response = new ResponseEntity<>(makeMap("error", "No existe el gamePlayer solicitado"), HttpStatus.NOT_FOUND);
			}else if (gamePlayer.getShips().size() > 1) {
				response = new ResponseEntity<>(makeMap("error", "Ya posee los ships ubicados, no es posible volver a ubicarlos"), HttpStatus.FORBIDDEN);
			}else if(gamePlayer.getPlayer().getId() != player.getId()){
				response = new ResponseEntity<>(makeMap("error", "usuario no perteneces a este game"), HttpStatus.UNAUTHORIZED);
			}else if(ships.size() != 5) {
				response = new ResponseEntity<>(makeMap("error", "Se intento crear mas ships de los permitidos. La cantidad permitida es 5"), HttpStatus.FORBIDDEN);
			}else {

				/*
				Nueva lista con las ubicaciones
				List<String> allLocs = new ArrayList<>();
				ships.forEach(x -> {
					allLocs.addAll(x.getLocations());
				});

				 */

				ships.forEach(ship -> gamePlayer.addShip(ship));
				gamePlayerRepository.save(gamePlayer);

				response = new ResponseEntity<>(makeMap("success", "ships added"), HttpStatus.CREATED);
				/*
				Hay que checkear:
					# -que sea un usuario logueado
					# - que exista el gamePlayerId
					# -Que el gamePlayer aun no posea ships
					# -que los ships sean exactamente 5
					-que las ubicaciones de los ships esten dentro de las coordenadas de la grilla
					-que las posiciones de cada ship sea consecutivas en horizontal o vertical
					-que las posiciones no se repitan(no se solapen los ships)
				 */
				
			}
		}
		return response;
	}


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
			dto.put("player", gamePlayer.getPlayer().playerDTO() );
			dto.put("ships", gamePlayer.getShips().stream().map(Ship::shipDTO));
			dto.put("salvos", gamePlayer.getGame().getGamePlayers().stream().flatMap(gp -> gp.getSalvoes().stream().map(Salvo::salvoDTO)));
		}else{
			dto.put("error", "no such game game");
		}
		return dto;
	}

}
