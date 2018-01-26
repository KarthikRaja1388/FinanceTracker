package network.karthik.financetracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import network.karthik.financetracker.services.CustomUserDetailService;

@EnableWebSecurity
@EnableJpaRepositories(basePackages="network.karthik.financetracker.repository")
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(getPasswordEncoder());
	}

	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
			.anyRequest().permitAll();
	}




	private org.springframework.security.crypto.password.PasswordEncoder getPasswordEncoder() {
		return new org.springframework.security.crypto.password.PasswordEncoder() {
			
			@Override
			public boolean matches(CharSequence charSequence, String s) {
				return true;
			}
			
			@Override
			public String encode(CharSequence charSequence) {
				return charSequence.toString();
			}
		};
	}
}
