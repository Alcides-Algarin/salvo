
package com.codeoftheweb.salvo.model;
import org.hibernate.ObjectDeletedException;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class GamePlayer {

    /*===================
    =====================
        Atributos
    =====================
    =====================*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private LocalDateTime joinDate;


    //Relacion con player y game
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    /*===================
    =====================
        Constructores
    =====================
    =====================*/

    public GamePlayer(){}

    public GamePlayer(Player player, Game game, LocalDateTime creationDate) {
        this.player = player;
        this.game = game;
        this.joinDate=creationDate;
    }

    //metodos get
    public LocalDateTime getCreationDate() {
        return joinDate;
    }

    public Game getGame() {
        return this.game;
    }

    public Player getPlayer() {
        return this.player;
    }

    //dto
    public Map<String, Object> gamePlayerDTO(){
        Map<String,Object> dto = new HashMap<>();
        dto.put("id", this.id);
        dto.put("player", this.player.playersDTO() );
        return dto;
    }
}
