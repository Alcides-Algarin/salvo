
let scoresJSON=[];
let tableScores2= document.querySelector("#tableScores2");


fetch( "/api/scores").then(function(response) {
  if (response.ok) {
    return response.json();
  }
  throw new Error(response.statusText);
}).then(function(json) {

  scoresJSON = json;

  createTableLeaderBoard2();

}).catch(function(error) {
  console.log( "Request failed: " + error.message );
});

function createTableLeaderBoard2(){

  scoresJSON.forEach(p => {

    tableScores2.innerHTML +=`
                <tr>
                  <td>${p.userName}</td>
                  <td>${p.points}</td>
                  <td>${p.won}</td>
                  <td>${p.lost}</td>
                  <td>${p.tied}</td>
                </tr>
                      `
  })
}



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
  games = json;// 2) asigno  el JSON a la variable que declare despues del fetch

  createdLeaderboard()
  createTableGames();//invoco a la funcion dentro del then para asegurarme que se ejecute solo despues obtener los datos del servidor
  createTableLeaderBoard();

}).catch(function(error) {
  // Se llama cuando se produce un error en cualquier punto de la cadena
  console.log( "Request failed: " + error.message );
});



let tableGames= document.querySelector("#tableGames");
let tableScores= document.querySelector("#tableScores");

let games=[];// 1)declaro un array vacio para guardar lo que me retorna el fetch

let gamePlayers=[];
let leaderBoard = [];


function createdLeaderboard(){

  //some(); Recibe una funcion evalua una condicion. Si al menos un elemento retorna true corta la iteracion. Caso contrario retorna false
  
  gamePlayers=games.flatMap(g=>g.gamePlayers);

  //games.flatMap(g=>g.gamePlayers).forEach(gp => leaderBoard.some(p=>p.id==gp.player.id)?updatePlayerScore(gp.player.id,gp.score):newPlayerScore(gp));
  gamePlayers.forEach(gp => leaderBoard.some(p=>p.id==gp.player.id)?updatePlayerScore(gp.player.id,gp.score):newPlayerScore(gp));

}

function updatePlayerScore(id,score){

  leaderBoard.forEach(p => {

    if(id==p.id){
      p.points+=score;
      score==1? p.won++ : (score==0.5? p.tied++ :p.lost++);
    }
  });
}

function newPlayerScore(gp){

  //recibo un gamePlayer. Si su score es distinto de null creo un nuevo playerObj con el id y el username
  //Caso contrario no hago nada xq solo quiero players que hayan finalizado un juego para poder obtener su puntaje

  if(gp.score!=null){

    let playerObj = {
    id: gp.player.id,
    userName: gp.player.email,
    points: 0,
    won: 0,
    lost: 0,
    tied: 0};

    leaderBoard.push(playerObj);//agrego el nuevo playerObj al array de players
    updatePlayerScore(playerObj.id, gp.score);//invoco a la funcion UpdatePlayerScore para cargar losn demas datos del playerObj
  }
}

function createTableGames(){

  games.forEach(game => {
    tableGames.innerHTML += `
                <tr>
                  <td>${game.gameId}</td>
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



function createTableLeaderBoard(){

  leaderBoard.forEach(player => {

    tableScores.innerHTML +=`
                <tr>
                  <td>${player.userName}</td>
                  <td>${player.points}</td>
                  <td>${player.won}</td>
                  <td>${player.lost}</td>
                  <td>${player.tied}</td>
                </tr>
    `

  })
}


/* Misma resolucion hecha con for
let gamesList= document.querySelector("#gamesList");

function createList(){// 
    let aux = "<ul>"
    for(let i=0 ; i<games.length; i++){
    let aux2 = ""
        for(let j = 0 ; j < games[i].gamePlayers.length ; j++){

            if(j == 0 && games[i].gamePlayers.length > 1){
                aux2 += games[i].gamePlayers[j].player.name + " vs "
            } else {
                aux2 += games[i].gamePlayers[j].player.name
            }
        }
        aux += `<li> Game ${games[i].id} created on ${games[i].created} players: ${aux2}</li>`
    }
    aux += "</ul>"
    gamesList.innerHTML = aux
}*/