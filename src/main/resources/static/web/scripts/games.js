
//Declaro las variables
let tableScores2= document.querySelector("#tableScores2");
let tableGames= document.querySelector("#tableGames");
let tableScores= document.querySelector("#tableScores");
let welcome= document.querySelector("#welcome");
let games=[];// 1)declaro un array vacio para guardar lo que me retorna el fetch
let scoresJSON=[];
let gamePlayers=[];
let leaderBoard = [];
let playerLogin;


//Oculto lo que no quiero que se vea al iniciar la pagina
document.getElementById("logout-form").style.display="none";
document.getElementById("createGame").style.display="none";
document.getElementById("Signup-form").style.display="none";
document.getElementById("login-form").style.display="none";


//Al abrir la pagina por primera vez invoco a fetchGames() y fetchScores() para que me muestre la tabla de 
//isLogin();
fetchGames();
fetchScores();


//################# Funcion main###################
// Para garantizar que se muetre informacion dependiendo de si hay alguien logueado antes de cualquier accion verifico si hay un player logueado. 

function  isLogin(){
  if(games.player=="guest"){

    document.getElementById("logout-form").style.display="none";
    document.getElementById("createGame").style.display="none";
    document.getElementById("Signup-form").style.display="none";
    document.getElementById("login-form").style.display="none";
    document.getElementById("mostrarRegister").style.display="block";
    document.getElementById("mostrarLogin").style.display="block";

  }else{
    document.getElementById("logout-form").style.display="block";
    document.getElementById("createGame").style.display="block";
    document.getElementById("mostrarRegister").style.display="none";
    document.getElementById("mostrarLogin").style.display="none";
  }
}

//################# FETCH GAMES ###################
function fetchGames(){

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
    createTableGames();//invoco a la funcion dentro del then para asegurarme que se ejecute solo despues obtener los datos del servidor
    createTableLeaderBoard();
    playerActual(games.player);
    isLogin();

  }).catch(function(error) {
    // Se llama cuando se produce un error en cualquier punto de la cadena
    console.log( "Request failed: " + error.message );
  });
}


//#####################################
function playerActual(player){
  player=="guest" ? welcome.innerHTML= ` Welcome to Salvo game.`
  :
  welcome.innerHTML= ` Welcome to game  ${player.name}.`;
}



//################### FETCH LOGIN ##################
function login(formulario){
  
  let form;

  formulario==null? form = new FormData(document.getElementById("login-form")) : form = formulario;
  
  fetch( "/api/login", {
     method: 'POST',
     body: form
  }).then(function(res) {
    if (res.ok) {
      console.log("Login Exitoso");
      fetchGames();
      createTableGames();
      document.getElementById("logout-form").style.display="block";
      document.getElementById("login-form").style.display="none";
      document.getElementById("createGame").style.display="block";
      document.getElementById("Signup-form").style.display="none";
    }else{
      throw new Error('error');
    }

  }).catch(function(error) {
    console.log( "Request failed: " + error.message );
    alert("Error de logueo. Verifique los datos ingresados. Debe estar previamente registrado para poder loguearse");

  });
}

//################### FETCH LOGOUT ##################
function logout() {

  fetch( "/api/logout", {
    method: 'POST'
  })
    .then(function(res) {
    if (res.ok) {
      console.log("Logout Exitoso");
      fetchGames();
      document.getElementById("logout-form").style.display="none";
    }else{
      return Promise.reject(res.json());
      console.log("ERROR  fallo Logout");
    }

  }).catch(function(error) {
    error.then(jsonError => alert(jsonError.error))
    console.log( "Request failed: " + error.message );
  });

}


//################### FETCH REGISTER ##################
function Signup(){
  
  let form = new FormData(document.getElementById("Signup-form"));

  let form2=new FormData();
  form2.append("username", form.get("email"));
  form2.append("password", form.get("password"));

  
  fetch( "/api/players", {
     method: 'POST',
     body: form
  }).then(function(res) {
    if (res.ok) {
      console.log("REGISTRO Exitoso");
      login(form2);
    }else{
      return Promise.reject(res.json());
      console.log("ERROR  FALLO REGISTRO");
    }

  }).catch(function(error) {
    error.then(jsonError => alert(jsonError.error))
    console.log( "Request failed: " + error.message );
  });


}

//################## #####################
function createGame(){
  
  fetch( "/api/games",{
    method: 'POST'

  }).then(function(response) {
    if (response.ok) {
      return response.json();
    }
    throw new Error(response.statusText);
  }).then(function(json) {

    newGame= json;
    fetchGames();
    createTableGames();
    location.href="/web/game.html?gp="+json.gpId
    console.log("Juego creado exitosamente")

  }).catch(function(error) {
    console.log( "Request failed: " + error.message );
  });

}

//################## #####################
function joinGame(gameId){

  fetch( `/api/games/${gameId}/players` ,{
    method: 'POST'

  }).then(function(response) {
    if (response.ok) {
      return response.json();
    }
    throw new Error(response.statusText);
  }).then(function(json) {

  
    fetchGames();
    createTableGames();
    location.href="/web/game.html?gp="+json.gpId



  }).catch(function(error) {
    console.log( "Request failed: " + error.message );
  });
}

//################## FETCH SCORES #####################
function fetchScores(){
  
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

}


//################## #####################
function mostrarLogin(){

  document.getElementById("login-form").style.display="block";
  document.getElementById("mostrarRegister").style.display="none";
  document.getElementById("mostrarLogin").style.display="none";
}

//############################################################
function mostrarRegister(){

  document.getElementById("Signup-form").style.display="block";
  document.getElementById("mostrarLogin").style.display="none";
  document.getElementById("mostrarRegister").style.display="none";
}

//################## #####################
function updatePlayerScore(id,score){

  leaderBoard.forEach(p => {

    if(id==p.id){
      p.points+=score;
      score==1? p.won++ : (score==0.5? p.tied++ :p.lost++);
    }
  });
}

//################## #####################
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

//################## #####################
function createTableGames(){

  tableGames.innerHTML="";

  if(games.player == "guest"){
      tableGames.innerHTML="";
      games.games.forEach(game => {
        tableGames.innerHTML += `
                    <tr>
                      <td data-gameId="${game.gameId}" >${game.gameId}</td>
                      <td>${game.created}</td>
                      <td>${game.gamePlayers.map(gamePlayer => gamePlayer.player.name).join(" VS ") }</td>

                    </tr>
                    `       
      });
  }else{
    let playerLogin= games.player;

    tableGames.innerHTML="";
    games.games.forEach(game => {

      if(game.gamePlayers.length ==2? true:false){
        tableGames.innerHTML += `
                    <tr>
                      <td>${game.gameId}</td>
                      <td>${game.created}</td>
                      <td>${game.gamePlayers.map(gp => gp.player.id==playerLogin.id ? ` <span> <button > <a href="game.html?gp=${gp.gpId}" >Go to Game </a></button></span>` : gp.player.name ).join(" VS ") }
                      </td>
                    </tr>
                    `
        }else{
          tableGames.innerHTML += `
                    <tr>
                      <td>${game.gameId}</td>
                      <td>${game.created}</td>
                      <td>${game.gamePlayers.map(gp => gp.player.id==playerLogin.id ? ` <span> <button > <a href="game.html?gp=${gp.gpId}" >Go to Game </a></button></span>` : gp.player.name +` <span> <button data-joinGame="gameId"  id="joinGame" onclick="joinGame(${game.gameId})" > Join Game</button>  </span> `) }
                      </td>
                    </tr>
                    `
        }

      });
  }
}

//##################  #####################
function createTableLeaderBoard(){

  leaderBoard.forEach(player => {

    tableScores.innerHTML +=`
                <tr>
                  <td>${player.userName}</td>
                  <td>${player.points}</td>
                  <td>${player.won}</td>
                  <td>${player.lost}</td>
                  <td>${player.tied}</td>
                </tr>`
  })
}


//################## #####################
function createTableLeaderBoard2(){

  scoresJSON.forEach(p => {

    if(!(p.points==0.0 && p.lost==0)){
      tableScores2.innerHTML +=`
                <tr>
                  <td>${p.userName}</td>
                  <td>${p.points}</td>
                  <td>${p.won}</td>
                  <td>${p.lost}</td>
                  <td>${p.tied}</td>
                </tr>
                     `
    }
  })
}


/*
//################## #####################
function createdLeaderboard(){

  //ESTA FUNCION CREA LA TABLA DE LEADERBOARD DESDE EL FRONTEND

  //some(); Recibe una funcion evalua una condicion. Si al menos un elemento retorna true corta la iteracion. Caso contrario retorna false
  
  gamePlayers=games.games.flatMap(g=>g.gamePlayers);

  //games.flatMap(g=>g.gamePlayers).forEach(gp => leaderBoard.some(p=>p.id==gp.player.id)?updatePlayerScore(gp.player.id,gp.score):newPlayerScore(gp));
  gamePlayers.forEach(gp => leaderBoard.some(p=>p.id==gp.player.id)?updatePlayerScore(gp.player.id,gp.score):newPlayerScore(gp));
}
*/

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