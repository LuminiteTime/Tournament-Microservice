package university.innopolis.tabletennis.tournamentmicroservice.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Tournament Management System Api",
                description = "Tournament Management System", version = "1.0.0",
                contact = @Contact(
                        name = "Mikhail Trifonov",
                        email = "trifonov2812@gmail.com"
//                        url = ""
                )
        )
)
public class OpenApiConfig {

}
