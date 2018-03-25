package com.geh.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Temporary In-memory Web Security Configuration
 *
 * @author Vera Boutchkova
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().and()
	        .authorizeRequests()
	            .antMatchers("/login", "/list", "/search").permitAll()
	        	.antMatchers("/admin/**").hasRole("ADMIN")
	        	.anyRequest().authenticated()
	        .and()
	        .formLogin()
	            .loginPage("/login")
	        .and()
	        .logout()
	        	.invalidateHttpSession(true)
	        	.logoutUrl("/logout");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER")
        	.and().withUser("admin").password("tmpadmin").roles("ADMIN", "USER");
	}
	
	@Override
	public void configure(WebSecurity web) {
	    web.ignoring().antMatchers("/fonts/**", "/images/**", "/css/**", "/js/**");
	}
}