package com.codeoftheweb.salvo.model;


import com.codeoftheweb.salvo.model.GamePlayer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String name;
    private String email;
    private String password;

    //Relacion con la clase GamePlayer: Un jugador puede jugar muchos juegos
    @OneToMany(mappedBy="player", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    Set<GamePlayer> gamePlayers= new HashSet<>();

    //Constructores
    public Player() { }

    public Player(String name, String email, String password ) {

        this.name= name;
        this.email= email;
        this.password=password;
    }

    //Method getter
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public Set<GamePlayer> getPlayers() {
        return gamePlayers;
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


}
