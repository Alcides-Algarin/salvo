let data
//
let params = new URLSearchParams(location.search);
let gp = params.get('gp');

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
		getSalvoes(data.salvoes);
		

	})
	.catch(error => console.log(error))
}


function infoGame(data){

	if(data.gamePlayer.length>1){

		let oponente= data.gamePlayer[0].player.name==data.player? data.gamePlayer[1].player.name: data.gamePlayer[0].player.name;
		document.querySelector("#player").innerHTML+=` Welcome to game  ${data.player}. You oponent is ${oponente}`;
	}else{
		document.querySelector("#player").innerHTML+=` Welcome to game  ${data.player}. Esperando oponente`;
	}

}

function getShips(ships){

	ships.forEach(ship => {

		createShips(ship.type,
					ship.locations.length,
					ship.locations[0][0] == ship.locations[1][0] ? "horizontal" : "vertical",
					document.getElementById(`ships${ship.locations[0]}`),
					true
					)
	})
}


function getSalvoes(salvoes){//Incompleto.Falta desarrollar la funcion "mostrarSalvos"

	salvoes.forEach( salvo => {//PROBAR CON OTRA DE LAS OPCIONES DE JSON PARA MOSTAR LOS SALVOS
		//LO MAS CONVENIENTE SERIA AGRUPAR LOS SALVOS MEDIANTE EL ID DEL JUGADOR Y ORDENARLOS SEGUN LOS TURNOS JUGADOS

		mostrarSalvos(salvo.turnNumber, locationSalvo, document.getElementById(`salvoes[0].locationSalvo${salvoes.locations}`))
	})
}

