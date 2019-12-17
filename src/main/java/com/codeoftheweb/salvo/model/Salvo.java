package com.codeoftheweb.salvo.model;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

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
        dto.put("playerId", this.gamePlayer.getPlayer().getId());
        //dto.put("barquitosUndidos", this.shipSunked().size());
        dto.put("shipSunked", this.gamePlayer.shipSunked().stream().map(Ship::shipDTO));
        dto.put("hits", this.getHits(this.locationSalvo));
        return dto;
    }

	public List<String> getHits( List<String> locationSalvo) {
	    //Este metodo me retorna la lista de disparos positivos a los ships del contrincante

		List<String> hits = new ArrayList<>();

        List<String> oponentShip= this.oponentShips();
		if(oponentShip!=null){
            hits.addAll(locationSalvo.stream().filter(salvo->
                    oponentShip
                            .stream()
                            .anyMatch(loc ->loc.equals(salvo))).collect(Collectors.toList()));
        }
		return hits;
	}

	/*
	public int shipDie(){
	    int x =0;

        Set<Ship> ships = new HashSet<>();

        ships.addAll(this.getKoShips().stream().map(Ship::shipDTO));

        return x;
    }*/

/*
    public List<Ship> shipSunked() {
        //Return the ships sunked

        Set<Salvo> salvoes;
        List<String> allSalvoes= new ArrayList<>();
        long myId;
        GamePlayer oponent;
        Set<Ship> shipsOponente;
        List<Ship> shipSunked;


        salvoes = this.getGamePlayer()
                .getSalvoes()
                .stream()
                .filter(salvo -> salvo.getTurNumber() <= this.getTurNumber())
                .collect(Collectors.toSet());

        salvoes.forEach(salvo -> allSalvoes.addAll(salvo.getLocationSalvo()));
        myId= this.gamePlayer.getId();
        oponent = this.gamePlayer.getGame().getGamePlayers().stream().filter(gp -> gp.getId()!=myId).findFirst().orElse(null);
        shipsOponente= oponent.getShips();

        shipSunked = shipsOponente
                .stream()
                .filter(ship -> allSalvoes.containsAll(ship.getLocations()))
                .collect(Collectors.toList());

        System.out.println( "Tamaño"+shipSunked.size());

        shipSunked.forEach(ship -> System.out.println("Type: "+ship.getType()));


        return shipSunked;
    }


    public int salvosSunked(){
        //Retorno la cantidad de barquitos hundidos
        return this.shipSunked().size();
    }

 */



	public List<String> oponentShips(){//Metodo para obtener los ships del oponente de este game (si lo tiene)

		List<String> oponentShips = new ArrayList<>();
        long myId= this.getGamePlayer().getId();
		GamePlayer oponentGP = this.getGamePlayer().getGame().getGamePlayers().stream().filter(gp -> gp.getId()!=myId).findFirst().orElse(null);
		if(oponentGP != null){
			oponentShips.addAll(oponentGP.getShips().stream().flatMap(e -> e.getLocations().stream()).collect(Collectors.toList()));
		}

		//System.out.println(oponentShips  +" ESTOS SON LOS SHIPS DE SU OPONENTE  " + oponentGP.getPlayer().getName());

		return oponentShips;
	}

}
