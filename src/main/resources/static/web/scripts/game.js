let data
//
let params = new URLSearchParams(location.search);
let gp = params.get('gp');
let oponente;
let shipLocation = []

getGameData(gp);

function getGameData(gpId){


	fetch(`/api/game_view/${gpId}`)
	.then(res => {
		if(res.ok){
			return res.json()

		}else{
			throw new Error(res.statusText)
		}
	})
	.then(json => {
	    data= json;
		getShips(data.ships);
		infoGame(data);
		getSalvoes(data.salvoes)

	})
	.catch(error => console.log(error))
}


function infoGame(data){

	if(data.gamePlayer.length>1){

		// oponente= data.gamePlayer[0].player.name==data.player? data.gamePlayer[0].player: data.gamePlayer[1].player;
		oponente= data.gamePlayer[0].gpId==gp? data.gamePlayer[1].player: data.gamePlayer[0].player;
		document.querySelector("#player").innerHTML+=` Welcome to game  ${data.player}. Your opponent is ${oponente.name}`;
	}else{
		document.querySelector("#player").innerHTML+=` Welcome to game  ${data.player}. Esperando oponente`;
	}
}



function getShips(ships){

	ships.forEach(ship => {

		ship.locations.forEach(loc => shipLocation.push(loc))
		// shipLocation.push(ship.locations.map(loc => loc)) //Obtengo un  array con todos los ship.locations(cada array corresponde a un ship) 

		createShips(ship.type,
					ship.locations.length,
					ship.locations[0][0] == ship.locations[1][0] ? "horizontal" : "vertical",
					document.getElementById(`ships${ship.locations[0]}`),
					true
		)
	})
}

function getSalvoes(salvoes){

	salvoes.forEach(salvo => {
		salvo.locationSalvo.forEach(loc => {
			if(salvo.playerId == oponente.id){
				if(shipLocation.includes(loc)){
					document.getElementById("ships" + loc).style.background = "red"
					document.getElementById("ships" + loc).innerHTML= salvo.turnNumber
				}
			}else{
				document.getElementById("salvo" + loc).style.background = "red"
				document.getElementById("salvo" + loc).innerHTML= salvo.turnNumber
			}
		})
	})
}