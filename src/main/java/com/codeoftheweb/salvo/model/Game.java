/**
 *
 */
package com.codeoftheweb.salvo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
public class Game {

    /*===================
    =====================
        Atributos
    =====================
    =====================*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id; //notacion para indicarle a Spring que autogenere el id
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    /*===================
    =====================
        Constructores
    =====================
    =====================*/
    public Game(){}

    public Game(LocalDateTime creationDate ) {
        this.creationDate = creationDate;
    }


    /*===================
    =====================
        Métodos
    =====================
    =====================*/

    //DTO
    public Map<String,Object> gameDTO(){
        Map<String,Object> dto = new HashMap<>();
        dto.put("created", this.creationDate);
        dto.put("id", this.id);
        dto.put("gamePlayers", this.gamePlayers.stream().map(GamePlayer::gamePlayerDTO));
        return dto;
    }

    //Getters
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public long getId() {
        return id;
    }

    //Setters
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }


    public Set<Player> getPlayers() {
        return this.gamePlayers.stream().map(GamePlayer::getPlayer).collect(Collectors.toSet());
    }


}
