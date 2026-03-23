package edu.dosw.project.SFC_TechUp_Futbol;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("TechCup Football API")
                        .description("""
                                Web platform for the comprehensive management of the internal football tournament
                                for the Systems Engineering, AI, Cybersecurity and Statistics programs.

                                **System actors:**
                                - **Player**: student who registers, marks availability and participates in teams.
                                - **Captain**: player who creates and manages their team, invites players, defines lineups and uploads payment receipts.
                                - **Referee**: user assigned to matches to register results, goals and cards.
                                - **Organizer**: tournament administrator; creates tournaments, verifies payments and manages the overall status.

                                **Development team:** Shawarma F.C — DOSW, University.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Shawarma F.C")
                                .email("techcup@dosw.edu.co")))
                .tags(List.of(
                        new Tag().name("Access").description("Registration and login for all system actors."),
                        new Tag().name("Users").description("Management of system actors: Players, Captains, Referees and Organizers."),
                        new Tag().name("Tournaments").description("Creation, query, standings table, elimination bracket and tournament statistics. Accessible by Organizers."),
                        new Tag().name("Teams").description("Team management: creation, query and player assignment. Managed by the Captain."),
                        new Tag().name("Matches").description("Match scheduling and management: results, goals and cards. Registered by the Referee."),
                        new Tag().name("Lineups").description("Definition and query of lineups per match. Managed by the Captain."),
                        new Tag().name("Payments").description("Payment receipt upload by the Captain and verification/approval by the Organizer.")
                ));
    }
}