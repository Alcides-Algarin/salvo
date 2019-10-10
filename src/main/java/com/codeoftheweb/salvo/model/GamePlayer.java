package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime joinDate;

    @ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL )
    @JoinColumn(name="player_id")
    private Player player;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    public GamePlayer(){}

    public GamePlayer(Player player, Game game, LocalDateTime creationDate) {
        this.player = player;
        this.game = game;
        this.joinDate=creationDate;
    }

    public LocalDateTime getCreationDate() {
        return joinDate;
    }
}
