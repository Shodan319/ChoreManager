package root.chores;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChoreConfiguration
{
    @Bean
    ChoreDomain getChoreDomain()
    {
        return new ChoreDomain();
    }
}
