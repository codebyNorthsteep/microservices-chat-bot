package org.example.bff;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class BffConfig {

    private final UserFilter userFilter;

    public BffConfig(UserFilter userFilter) {
        this.userFilter = userFilter;
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) {
        return http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                // Enable OAuth2 login (for browser users)
                .oauth2Login(Customizer.withDefaults())
                // Enable OAuth2 client (needed for tokenRelay), för att kunna skicka vidare tokens
                .oauth2Client(Customizer.withDefaults())
                .build();

    }

    /*
 Ett vanligt scenario när man vill förenkla för sina microservices så att de slipper packa upp JWT-tokenet själva
 är att istället för att använda tokenRelay(), som skickar vidare hela Authorization-headern, kan man använda
 en kombination av Springs säkerhetskontext och filtret addRequestHeader.
 */
    //IDOR-säkerhetsrisk: eftersom username skickades med i URL:en så kan en användare potentiellt ändra den och få tillgång till någon annans meddelanden. Ändra till /me och hantera i MessageController
    @Bean
    public RouterFunction<ServerResponse> routeWithUsername() {
        return route()
                .GET("/api/messages/me", http())
                .before(uri("http://localhost:8081/"))
//                .before(setPath("/api/test"))
                .filter(userFilter) // Använd det anpassade filtret för att lägga till användarnamnet i headern
                .build();
    }


    @Bean
    public RouterFunction<ServerResponse> routeToPostMessage() {
        return route()
                .POST("/api/messages", http())
                .before(uri("http://localhost:8081/"))
                .filter(userFilter)
                .build();
    }

    //Wildcard-routes: för att fånga alla requests till /api/users/** och skicka vidare till http://localhost:8083/ (user-service)
    //todo: lägg till patch om jag fortsätter med att man ska kunna uppdatera sitt lösenord
    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return route()
                .path("/api/users/**", () -> route()
                        .GET("/**", http())
                        .POST("/**", http())
                        .PUT("/**", http())
                        .DELETE("/**", http())
                        .build())
                .before(uri("http://localhost:8083/"))
                .filter(userFilter)
                .build();
    }
}
