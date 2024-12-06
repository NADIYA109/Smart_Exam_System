package com.smartexamsystem.configuration;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class MyConfiguration  {

	@Autowired
	public AuthenticationSuccessHandler  customSuccessHandler;

	@Bean
	public UserDetailsService getUserDetailsService() {
		return new CustomUserService();
	}


	@Bean
	public BCryptPasswordEncoder getPassword() {

		return new BcryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider getDaoAuthProvider() {

		DaoAuthenticationProvider dao= new DaoAuthenticationProvider();
		dao.setUserDetailsService(getUserDetailsService());
		dao.setPasswordEncoder(getPassword());
		return dao;
	}

	 @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }





//	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//
//		auth.authenticationProvider(getDaoAuthProvider());
//
//	}
//



//	protected void configure(HttpSecurity http) throws Exception{
//
//		http.authorizeRequests().antMatchers("/user/**").hashRole("USER").antMatchers("/**").permitAll().and().formLogin().and().csrf().disable();
//
//	}
//
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                auth -> auth.requestMatchers("/admin/**").hasRole("ADMIN") // Existing rule
                        //.requestMatchers("/sme/**").hasAuthority("ROLE_SME")

                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/").permitAll()  // Allow access to "/" (index page)
                        .requestMatchers("/login").permitAll()  // Allow access to "/login"
                        .requestMatchers("/register").permitAll() // Allow access to "/register"
                        .requestMatchers("/error").permitAll()  // Allow access to "/error"
                        .anyRequest().authenticated()
        )


        .csrf(csrf -> csrf.disable())  // Disable CSRF protection

                .formLogin(formLogin -> formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/dologin")
                                //.usernameParameter("username")
                                .successHandler(customSuccessHandler)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                );

	    return http.build();
	}
}