package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.model.Player;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	//Authentication

	@Autowired
	PlayerRepository playerRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {

			//Busca un nombre de pleyer en la base de datos:
			//Return this player or null si no existe
			Player player = playerRepository.findByEmail(inputName);


			if (player != null) {

				//en este sector se pueden crear todos los roles de usuario que se requiera
				//Defino los roles de cada usuario

				if(player.getEmail().equals("kim_bauer@gmail.com")) {
					return new User(player.getEmail(), player.getPassword(),
							AuthorityUtils.createAuthorityList("ADMIN"));
							//AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN", "USER");
				}else{
					return new User(player.getEmail(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				}
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		}).passwordEncoder(passwordEncoder());
	}
}