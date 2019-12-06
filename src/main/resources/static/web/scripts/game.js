
let gameData
let params = new URLSearchParams(location.search);//retorna todos los parametros que posee la URL
let gpId = params.get('gp');//retorna el valor del parametro "gp" contenido en la "params" 
let oponente;
let shipLocation = [];

getGameData(gpId);

/*
	# Consultar si el jugador ya posee ships guardados.
		* Si los tiene, mostrarlos en la grilla.
		* 
		* Si no los tiene, mostrar los ships en el docks y mostar el boton para "enviarShips"
			* Una vez que se envian los ships, recargar la pagina y mostar la grilla de salvos y ocultar el boton de enviar.
			* 
*/

/*function cleanSalvoGrid(){
	document.querySelectorAll(".salvo").classList.toggle("salvo");

}*/



function getGameData(gpId){

	fetch(`/api/game_view/${gpId}`)
	.then(res => {
		if(res.ok){
			console.log("gfhghjop")
			return res.json()
		}else{
			throw new Error(res.statusText)
		}
	})
	.then(json => {
	    gameData= json;
		infoGame(gameData.gamePlayer);
	    getShips(gameData.ships);
		getSalvoes(gameData.salvos);
	})
	.catch(error => console.log(error))
}
//#####################################################################

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
			document.querySelector("#submitShips").style.display="none";
			location.reload(false);//recarga la pagina. parametro boolean opcional. false: recarga la pagina desde el servidor. true: recarga desde cache del navegador

		}else
		return Promise.reject(res.json());
	}).catch(function(error) {
		error.then(jsonError => alert(jsonError.error))

	});
}
//#####################################################################

//####################################################################################
//####################################################################################
//#####Solo de debe ejecutar si los 5 ships estan ubicados en la grilla correcta######
//####################################################################################
//####################################################################################
function sendSalvoesLocations(gpId){

	//Al  enviar los ships al servidor, y obtener una respuesta exitosa, hago otro fetch a game_view y actualizo los datos de la pagina
	//
	

	let salvo= salvoLocation();

	fetch( `/api/games/players/${gpId}/salvos`, {
		method: 'POST',
		headers: {"Content-type": "application/json"},
		body:  JSON.stringify(salvo)
	}).then(function(res) {
		if (res.ok) {
			console.log("Salvo enviado correctamente");
			location.reload(false);
		}else
		return Promise.reject(res.json());
	}).catch(function(error) {
		error.then(jsonError => alert(jsonError.error))

	});
}
//#####################################################################

function infoGame(gp){

	if(gp.length>1){

		// oponente= gp.gamePlayer[0].player.name==gp.player? gp.gamePlayer[0].player: gp.gamePlayer[1].player;
		oponente= gp[0].gpId==gpId? gp[1].player: gp[0].player;
		document.querySelector("#player").innerHTML+=` Welcome to game  ${gameData.player.name}. Your opponent is ${oponente.name}`;
	}else{

		//mientras no tenga oponente se mostrara una sola grila y msj "Esperando al oponente"
		//desactivo el boton de enviar salvos.//tambien podria crearlo directamente cuado muestre esa grilla
		document.querySelector("#grid2").style.display="none";
		document.querySelector("#submitSalvo").style.display="none"
		document.querySelector("#player").innerHTML+=` Welcome to game  ${gameData.player.name}. Esperando oponente`;
	}
}
//#####################################################################

function getShips(ships){

	if(ships.length==5){

		document.querySelector("#submitShips").style.display="none";

		ships.forEach(ship => {

			ship.locations.forEach(loc => shipLocation.push(loc))
			// shipLocation.push(ship.locations.map(loc => loc)) //Obtengo un  array con todos los ship.locations(cada array corresponde a un ship) 
			createShips(ship.type,
						ship.locations.length,
						ship.locations[0][0] == ship.locations[1][0] ? "horizontal" : "vertical",
						document.getElementById(`ships${ship.locations[0]}`),
						true)
		})
	}else{

		document.querySelector("#submitSalvo").style.display="none";
		createShips('carrier', 5, 'horizontal', document.getElementById('dock'),false)
		createShips('battleship', 4, 'horizontal', document.getElementById('dock'),false)
		createShips('submarine', 3, 'horizontal', document.getElementById('dock'),false)
		createShips('destroyer', 3, 'horizontal', document.getElementById('dock'),false)
		createShips('patrol_boat', 2, 'horizontal', document.getElementById('dock'),false)
	}
}
//#####################################################################

function salvoLocation(){

	//Busco todas las ubicaciones de la grilla que poseen la clase salvo.
	//reccoro el elemento y obtengo su data-set y retorno un array con la lista de salvos
	
	let salvoLocation=document.querySelectorAll(".salvo");
	let locat= [];
	salvoLocation.forEach(x => { locat.push(x.dataset.y + x.dataset.x) });

	return locat;
}
//#####################################################################


for(let i = 0; i < 11; i++){//Agrega un eventListener a todos los grids de la grilla de salvoes

    for(let j = 0; j < 11; j++){
        if(i > 0 && j > 0){

            id = `${String.fromCharCode(i - 1 + 65)}${ j }`
            document.querySelector("#salvo"+id).addEventListener("click", salvoObjetive);
        }
    }
}
//#####################################################################

function salvoObjetive(ev){
		
	let cell = ev.target // Obtengo la etiqueta donde se ha disparado el evento

	if(document.querySelectorAll(".salvo").length<5 || cell.classList[1]=="salvo" ){//Si ya selecciono los 5 lugares, aun puede quitar los que ya habia seleccionado previamente
		cell.classList.toggle("salvo")// Agreaga la clase salvo, Si ya posee esa clase la elimina	
	}
}
//  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

function getSalvoes(salvoes){

	salvoes.forEach(salvo => {
		salvo.locationSalvo.forEach(loc => {
			if(salvo.playerId == oponente.id){
				if(shipLocation.includes(loc)){
					document.querySelector("#ships" + loc).style.background = "red";// Cambiar esta linea por una que agregue una classList para luego darle estilo en css
					document.querySelector("#ships" + loc).innerHTML= salvo.turnNumber;	
				}
			}else{
				document.querySelector("#salvo" + loc).innerHTML= salvo.turnNumber;
				document.querySelector("#salvo" + loc).classList.remove("salvo");//borro la clase "salvo" para luego agregar la clase hits
				document.querySelector("#salvo" + loc).classList.add("hits");
				document.querySelector("#salvo" + loc).removeEventListener("click", salvoObjetive);// quito el eventListener para no volver a disparar en el mismo lugar
			}
		})
	});


}
// #######################################################################

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
//########################################################################


function createShipArrayOBJ(){//Retorna un array de OBJETOS con los tipode ships y sus respectivas lacations.

	let shipArrayOBJ= [];
	let nameShips=["carrier", "battleship", "submarine", "destroyer", "patrol_boat"];	
	nameShips.forEach(name => shipArrayOBJ.push(shipsLocation(name)));

	return shipArrayOBJ;
}
