package dev.josers.realworld.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    @Qualifier("myService")
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtFilter: JWTAuthFilter

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder())
    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/users").permitAll()
                .antMatchers(HttpMethod.POST,"/api/users/login").permitAll()
                .antMatchers(HttpMethod.GET,"/api/tags").permitAll()
                .antMatchers(HttpMethod.GET,"/api/profiles/**").permitAll()
            .anyRequest().authenticated()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**")
    }

    @Bean
    override fun authenticationManagerBean() = super.authenticationManagerBean()

    @Bean
    fun encoder() = BCryptPasswordEncoder()
}