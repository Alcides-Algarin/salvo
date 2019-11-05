package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {
    /*===================
    =====================
        Atributos
    =====================
    =====================*/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private int turNumber;// para el numero de turno

    @ElementCollection
    private List<String> locationSalvo = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public Salvo(){ }

    public Salvo( int turNumber, List<String> locationSalvo){
        this.turNumber= turNumber;
        this.locationSalvo= locationSalvo;
    }

    public long getId() {
        return this.id;
    }

    public int getTurNumber(){
        return this.turNumber;
    }

    public void setTurNumber(int turNumber){
        this.turNumber= turNumber;
    }

    public  List<String> getLocationSalvo(){
        return this.locationSalvo;
    }

    public void setLocationSalvo( List<String> locationSalvo){
        this.locationSalvo= locationSalvo;
    }

    public GamePlayer getGamePlayer(){
        return this.gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer){
        this.gamePlayer=gamePlayer;
    }

    public Map<String, Object> salvoDTO(){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turnNumber", this.getTurNumber());
        dto.put("locationSalvo", this.getLocationSalvo());
        dto.put("playerId", this.gamePlayer.getPlayer().getId() );
        return dto;
    }
}
