let data
//
let params = new URLSearchParams(location.search);
let gp = params.get('gp');
let oponente;
let shipLocation = [];

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

	if(ships.length==5){
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
	}else{
		createShips('carrier', 5, 'horizontal', document.getElementById('dock'),false)
		createShips('battleship', 4, 'horizontal', document.getElementById('dock'),false)
		createShips('submarine', 3, 'horizontal', document.getElementById('dock'),false)
		createShips('destroyer', 3, 'horizontal', document.getElementById('dock'),false)
		createShips('patrol_boat', 2, 'horizontal', document.getElementById('dock'),false)
	}
}
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

function sendShipsLocations(gpId){

	let shipsLocationArray= createShipArrayOBJ();
	let a=JSON.stringify(shipsLocationArray)



	fetch( `/api/games/players/${gpId}/ships`, {
		method: 'POST',
		body:  a,
		headers: {"Content-type": "application/json"}
	}).then(function(res) {
		if (res.ok) {
			console.log("locations guardados exitosamente");
		}else
		return Promise.reject(res.json());
	}).catch(function(error) {
		error.then(jsonError => alert(jsonError.error))

	});
}

//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
/*
function sendSalvoesLocations(gpId){

	let shipsLocationArray= createShipArrayOBJ();
	let a=JSON.stringify(shipsLocationArray)



	fetch( `/api/games/players/{gamePlayerId}/salvos`, {
		method: 'POST',
		body:  a,
		headers: {"Content-type": "application/json"}
	}).then(function(res) {
		if (res.ok) {
			console.log("locations guardados exitosamente");
		}else
		return Promise.reject(res.json());
	}).catch(function(error) {
		error.then(jsonError => alert(jsonError.error))

	});
}





function createSalvoArrayOBJ(){//Retorna un array de OBJETOS con los tipode ships y sus respectivas lacations.

	let salvoArrayOBJ= [];

	let nameShips=["carrier", "battleship", "submarine", "destroyer", "patrol_boat"];	
	nameShips.forEach(name => shipArrayOBJ.push(shipsLocation(name)));

	return shipArrayOBJ;
}
*/

function salvoLocation(){

	//Busco todas las ubicaciones de la grilla que poseen la clase salvo.
	//reccoro el elemento y obtengo su data-set y retorno un array con la lista de salvos
	
	let salvoLocation=document.querySelectorAll(".salvo");
	let locat= [];
	salvoLocation.forEach(x => { locat.push(x.dataset.y + x.dataset.x) });

	return locat;
}




//Agrega un eventListener a todos los grids de la grilla de salvoes
for(let i = 0; i < 11; i++){

    for(let j = 0; j < 11; j++){
        if(i > 0 && j > 0){

            id = `${String.fromCharCode(i - 1 + 65)}${ j }`

            document.querySelector("#salvo"+id).addEventListener("click", salvoObjetive);
        }
    }
}


function salvoObjetive(ev){

 	let cell = ev.target // Obtengo la etiqueta donde se ha disparado el evento
 	cell.classList.toggle("salvo")// Agreaga la clase salvo, Si ya posee esa clase la elimina

}


//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

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

//########################################################################################


function shipsLocation(name){

	//Busco todas las ubicaciones de la grilla que poseen una clase que coincide con el name(nombre del ship). 
	//reccoro el elemento y obtengo su data-set y retorno un obj con key= nombre del ship y value =arrayLocation de ese ship

	obj={};
	let shipData=document.querySelectorAll("."+name+"-busy-cell");
	let locat= [];

	shipData.forEach(x => { locat.push(x.dataset.y + x.dataset.x) });

	obj.type=name;
	obj.locations=locat;

	return obj;
}



function createShipArrayOBJ(){//Retorna un array de OBJETOS con los tipode ships y sus respectivas lacations.

	let shipArrayOBJ= [];
	let nameShips=["carrier", "battleship", "submarine", "destroyer", "patrol_boat"];	
	nameShips.forEach(name => shipArrayOBJ.push(shipsLocation(name)));

	return shipArrayOBJ;
}




/* ####################################################################

PROXIMOS PASOS:

AL INGRESAR AL JUEGO, CONSULTAR SI EL JUGADOR YA POSEE LOS SHIPS COLOCADOS.

SI YA LOS TIENE COLOCADOS, MOSTRARLOS EN LA GRILLA.
CASO CONTRARIO MOSTRAR LOS BARCOS EN EL LA DARSENA.

PONER UN BOTON PARA ENVIAR BARCOS.

*/