package org.isj.ing.annuarium.webapp.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity// toujours ajoutee les deux ci pour la config
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;// pour encoder les mots de passe

	@Autowired
	private DataSource dataSource;// recuperer toutes les valeurs utilisees dans applicatio property

	@Value("${spring.queries.users-query}")
	private String usersQuery;// permet la gestion des utilisations

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;// permet la gestion des roles des utilisateurs

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.
		jdbcAuthentication()
		.usersByUsernameQuery(usersQuery)
		.authoritiesByUsernameQuery(rolesQuery)
		.dataSource(dataSource)
		.passwordEncoder(bCryptPasswordEncoder);// PasswordEncoder permet d'indiquer que les mots de passe sont cryptee
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.
		authorizeRequests()
		.antMatchers("/login").permitAll()// permitAll permet de rendre login accessible a tout le monde
				.antMatchers("/registration").permitAll()
				.antMatchers("/","rechercherform","/listactes","/detail").permitAll()// permitAll permet de rendre les pages accessible a tout le monde.antMatchers("/api/**").permitAll()
				.antMatchers("/api/**").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN")
		.anyRequest().authenticated().and().csrf().disable().formLogin()
		.loginPage("/login").failureUrl("/login?error=true")
		.defaultSuccessUrl("/")//pour indiquer quelle page va s'ouvrir quand l'user a reussi a se loguer
		.usernameParameter("email")// email represente le name dans le input dans le formulaire
		.passwordParameter("password")
		.and().logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))// route pour se deconnecter
		.logoutSuccessUrl("/login").and().exceptionHandling()
		.accessDeniedPage("/access-denied");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring()
		.antMatchers( "/assets/**", "/forms/**", "/static/**", "/ressources/**");
	}

}
