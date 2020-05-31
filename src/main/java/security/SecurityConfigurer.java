package security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter
{
    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/chores");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        http.
            csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers("/chores").permitAll() .anyRequest().authenticated();
    }
}
