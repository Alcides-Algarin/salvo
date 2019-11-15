/**
 *
 */

package com.codeoftheweb.salvo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


//anotación para Spring. Le dice que debe crear una Tabla en la base de datos para esta clase.
@Entity
public class Player {

    /*===================
    =====================
        Atributos
    =====================
    =====================*/

    //propiedad para identificar a cada instancia de la clase en la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String name;
    private String email;
    private String password;

    //Relacion con la clase GamePlayer: Un jugador puede jugar muchos juegos
    @OneToMany(mappedBy="player", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<GamePlayer> gamePlayers= new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Score> scores= new HashSet<>();


    /*===================
    =====================
        Constructores
    =====================
    =====================*/
    public Player() { }

    public Player(String name, String email, String password ) {
        this.name= name;
        this.email= email;
        this.password=password;
    }

	/*===================
	=====================
	    Métodos
	=====================
	=====================*/

    @JsonIgnore
    public Set<Player> getPlayers(){
        //lamamos a la propiedad GamePlayers que contiene a todos los gameplayers relacionados con el conductor
        //la recorremos y por cada una llamamos a su getter de Game, para almacenar cada player
        //en la lista que devuelve el método.
        return this.gamePlayers.stream().map(GamePlayer::getPlayer).collect(Collectors.toSet());
    }

    @JsonIgnore
    public Set<Game> getGames(){
        //lamamos a la propiedad GamePlayers que contiene a todos los gameplayers relacionados con el conductor
        //la recorremos y por cada una llamamos a su getter de Game, para almacenar cada player
        //en la lista que devuelve el método.
        return this.gamePlayers.stream().map(GamePlayer::getGame).collect(Collectors.toSet());
    }

    //Method getter
    public long getId() { return this.id; }
    public String getName() {
        return this.name;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPassword() {
        return this.password;
    }

    //Method setter
    public void setName(String name) {
         this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Score getScoreByGame(Game game){
        return this.scores.stream().filter(score -> score.getGame().getId() == game.getId()).findFirst().orElse(null);

    }


    //DTO: data transfer object. Método para administrar la información de la clase
    //Retorna una Mapa, un tipo de dato que contiene pares de key y value. Parecido a los objetos de JS
    //Los métodos en Java se declaran:
    //primero con el método de acceso (publico o privado)
    //segundo con el tipo de dato que retorna el método
    //tercero con el nombre del métodos y sus parámetros

    public Map<String, Object> playerDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.id);
        dto.put("name", this.getName());
        dto.put("email", this.getEmail());
        return dto;
    }


    public Map<String, Object> scoreDTO(){
    	Map<String, Object> dto = new LinkedHashMap<>();
		int wons=0;
		int tied=0;
		int lost=0;
		double points = 0.0;
		String userName=null;

		for(Score score : this.scores){
			points += score.getPoint();
			userName= score.getPlayer().getEmail();

			if(score.getPoint()==1){
				wons++;
			}else if(score.getPoint()==0){
				lost++;
			}else{
				tied++;
			}
		}

		dto.put("id", this.id);
		dto.put("points", points);
		dto.put("won", wons);
		dto.put("lost", lost);
		dto.put("tied", tied);
		dto.put("userName",email);

    	return dto;
	}
}
