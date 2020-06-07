package root.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter
{
    @Bean
    @Override
    public UserDetailsService userDetailsService()
    {
        List<UserDetails> users = new ArrayList<>();
        users.add(
                User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build()
        );
        users.add(
                User.withDefaultPasswordEncoder()
                .username("user2")
                .password("password")
                .roles("USER")
                .build()
        );
        return new InMemoryUserDetailsManager(users);
    }
}
