


fetch( "/api/games").then(function(response) {
  if (response.ok) {
  // Añadir una nueva promesa a la cadena
    return response.json();
  }
  // señalar un error del servidor a la cadena
  throw new Error(response.statusText);
}).then(function(json) {
  // Hacer algo con el JSON
  // Tenga en cuenta que esto no añade una nueva promesa


  jsonGames = json;// 2) asigno  el JSON a la variable que declare despues del fetch
  //createList();
  createTable();//invoco a la funcion dentro del then para asegurarme que se ejecute solo despues obtener los datos del servidor


}).catch(function(error) {
  // Se llama cuando se produce un error en cualquier punto de la cadena
  console.log( "Request failed: " + error.message );
});

let jsonGames=[];// 1)declaro un array vacio para guardar lo que me retorna el fetch


let tableGames= document.querySelector("#tableGames");



function createTable(){


  jsonGames.forEach(function(game) {
    tableGames.innerHTML += `
                <tr>
                  <td>${game.id}</td>
                  <td>${game.created}</td>
                  <td>${game.gamePlayers.map(gamePlayer => gamePlayer.player.name).join(" VS ")}</td>
                </tr>
                `
                /*${ } Dentro de los bigotes escribo codigo javaScript. 

                <td>${game.gamePlayers.map(gamePlayer => gamePlayer.player.name).join(" VS ")}</td> En esta linea 
                vuelvo a recorrer el array de gamePlayer esta vez con un map. y cocateno lo que me retorna el map(retorna un array)
                con un join(" VS ") para que el navegador me muestre juagadorA VS jugadorB

                */
});
}



/* Misma resolucion hecha con for
let gamesList= document.querySelector("#gamesList");

function createList(){// 
    let aux = "<ul>"
    for(let i=0 ; i<jsonGames.length; i++){
    let aux2 = ""
        for(let j = 0 ; j < jsonGames[i].gamePlayers.length ; j++){

            if(j == 0 && jsonGames[i].gamePlayers.length > 1){
                aux2 += jsonGames[i].gamePlayers[j].player.name + " vs "
            } else {
                aux2 += jsonGames[i].gamePlayers[j].player.name
            }
        }
        aux += `<li> Game ${jsonGames[i].id} created on ${jsonGames[i].created} players: ${aux2}</li>`
    }
    aux += "</ul>"
    gamesList.innerHTML = aux
}*/



