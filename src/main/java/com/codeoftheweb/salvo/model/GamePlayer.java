
package com.codeoftheweb.salvo.model;
import com.codeoftheweb.salvo.SalvoController;
import org.hibernate.ObjectDeletedException;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

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

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER, cascade= CascadeType.ALL)
    private Set<Salvo> salvoes = new HashSet<>();
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

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }


    public Game getGame() {
        return game;
    }
    public void setGame(Game game) {
        this.game= game;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player= player;
    }

    //metodo para agregar ships
    public void addShip(Ship ship){
        this.ships.add(ship);
        ship.setGamePlayer(this);
    }

    //metodo para agregar salvos
    public void addSalvo(Salvo salvo){
        this.salvoes.add(salvo);
        salvo.setGamePlayer(this);
    }

    public Set<Ship> getShips(){
        return this.ships;
    }

    public Set<Salvo> getSalvoes(){
        return this.salvoes;
    }

    //dto
    public Map<String, Object> gamePlayerDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("player", this.getPlayer().playerDTO() );
        return dto;
    }


}
