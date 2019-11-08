package com.codeoftheweb.salvo.model;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Score{
    /*===================
    =====================
        Atributos
    =====================
    =====================*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private double point;

	private LocalDateTime finishDate;

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

    public Score(){}

    public Score(Player player, Game game, LocalDateTime finishDate, double point) {
        this.player = player;
        this.game = game;
        this.finishDate=finishDate;
        this.point = point;
    }

    //GETTERS
    public long getId(){
    	return id;
    }

    public LocalDateTime finishDate(){
    	return this.finishDate;
    }

    public Game getGame(){
    	return this.game;
    }

    public Player getPlayer(){
    	return this.player;
    }

    public double getPoint(){
    	return this.point;
    }

    //SETTERS
    public void setFinishDate(LocalDateTime end){
    	this.finishDate=end;
    }

    public void setGame(Game game) {
        this.game= game;
    }
    
    public void setPlayer(Player player) {
        this.player= player;
    }

    public void setPoint(double point){
    	this.point = point;
    }



}