package com.codeoftheweb.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id; //notacion para indicarle a Spring que autogenere el id
    private LocalDateTime creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    Set<GamePlayer> gamePlayers = new HashSet<>();

    //contructores
    public Game(){}

    public Game(LocalDateTime creationDate ) {
        this.creationDate = creationDate;
    }

    //Getters
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public long getId() {
        return id;
    }
    @JsonIgnore
    public Set<GamePlayer> getGames() {
        return gamePlayers;
    }

    //Setters
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}