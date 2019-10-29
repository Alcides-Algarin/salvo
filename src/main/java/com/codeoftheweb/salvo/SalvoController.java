/**
 *
 *
 */
package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.Game;
import com.codeoftheweb.salvo.model.GamePlayer;
import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.model.Ship;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    private List<Map<String, Object>> getGames() {
        return gameRepository.findAll().stream().map(Game::gameDTO).collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String,Object> getGameView(@PathVariable long gamePlayerId){
        return this.gameViewDTO(gamePlayerRepository.findById(gamePlayerId).orElse(null));
    }
    private Map<String,Object> gameViewDTO(GamePlayer gamePlayer){
        Map<String,Object> dto = new LinkedHashMap<>();

        if(gamePlayer != null){
            dto.put("id", gamePlayer.getGame().getId());
            dto.put("creationDate", gamePlayer.getGame().getCreationDate());
            dto.put("gamePlayer", gamePlayer.getGame().getGamePlayers().stream().map(GamePlayer::gamePlayerDTO));
            dto.put("player", gamePlayer.getPlayer().getName());
            dto.put("ships", gamePlayer.getShips().stream().map(Ship::shipDTO));

        }else{
            dto.put("error", "no such game");
        }

        return dto;


    }
}
