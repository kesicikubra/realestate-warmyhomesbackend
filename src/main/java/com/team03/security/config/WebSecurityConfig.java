package com.team03.security.config;


import com.team03.security.jwt.AuthEntryPointJwt;
import com.team03.security.jwt.AuthTokenFilter;
import com.team03.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig{

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .exceptionHandling(
                        handlingConfigurer ->
                            handlingConfigurer.authenticationEntryPoint(unauthorizedHandler)

                ).formLogin(AbstractHttpConfigurer::disable)
                //  .securityMatcher(AUTH_WHITE_LIST) // problem cozumu !!!
                .authorizeHttpRequests(x -> x.requestMatchers(AUTH_WHITE_LIST).permitAll().anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    private static final String[] AUTH_WHITE_LIST = {
            "/",
            "/v3/api-docs/**",
            "swagger-ui/**",
            "/swagger-ui/index.html",
            "/index.html",
            "/css/**",
            "/images/**",
            "/js/**",
            "/users/",
            "/login",
            "/register",
            "/reset-password",
            "/forgot-password",
            "/categories/**",
            "/verify",
            "/cities",
            "/districts",
            "/countries",
            "/adverts",
            "/adverts/cities",
            "/adverts/categories",
            "/adverts/popular/{amount}",
            "/adverts/{slug}",
            "/images/{imageId}",
            "/refreshToken",
            "/advert-types",
            "/contact-messages/save",
            "/categories",
            "/categories/{id:[1-9]\\d*}",
            "/categories/{slug:[a-zA-Z]+[a-zA-Z0-9]*[a-zA-Z]+}"
    };

}
