package ca.sheridan.golamhai.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ProductSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSec) throws Exception {
        httpSec
                // 1) Allow anyone to see the landing page ("/") and the login/logout pages +
                // CSS/JS/images
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/",
                                "/login",
                                "/logout",
                                "/css/**",
                                "/js/**",
                                "/images/**")
                        .permitAll()

                        // 2) ADMIN-only pages
                        .requestMatchers(
                                "/productDataInput/**",
                                "/editableListOfProducts/**",
                                "/editProduct/**",
                                "/updateProduct/**",
                                "/deleteProduct/**")
                        .hasAuthority("ADMIN")

                        // 3) SALES-only pages
                        .requestMatchers(
                                "/listOfProducts/**",
                                "/productsByBrand/**",
                                "/productsByQuantity/**")
                        .hasAuthority("SALES")

                        // 4) everything else (like /index post-login) requires you to be authenticated
                        .anyRequest().authenticated())

                // form-based login configuration
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .failureUrl("/login?error=true")
                        .permitAll())

                // logout configuration
                .logout(logout -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())

                // custom access-denied page
                .exceptionHandling()
                .accessDeniedPage("/accessDenied")
                .and()

                // optional: enable HTTP Basic as well
                .httpBasic();

        return httpSec.build();
    }

    @Bean
    public UserDetailsService users() {
        UserBuilder users = User.withDefaultPasswordEncoder();

        UserDetails admin = users
                .username("UserA")
                .password("aaaa")
                .authorities("ADMIN", "SALES")
                .build();

        UserDetails sales = users
                .username("UserB")
                .password("bbbb")
                .authorities("SALES")
                .build();

        UserDetails both = users
                .username("UserC")
                .password("cccc")
                .authorities("ADMIN", "SALES")
                .build();

        return new InMemoryUserDetailsManager(admin, sales, both);
    }
}
