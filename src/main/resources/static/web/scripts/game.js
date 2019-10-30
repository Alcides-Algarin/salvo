let data

let params = new URLSearchParams(location.search)
let gp = params.get('gp')

getGameData(gp)

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
		 data = json
		 getShips(data.ships)
	})
	.catch(error => console.log(error))

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
