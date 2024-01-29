package com.tlaxcala.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {
  @Value("${misRutas_SALVADOR.openapi.dev-url}")
  private String devUrl;
  @Value("${misRutas_SALVADOR.openapi.prod-url}")
  private String prodUrl;

  @Bean
  public OpenAPI myOpenAPI() {

    Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("URL del servidor en entorno de Desarrollo");
    Server prodServer = new Server();
    prodServer.setUrl(prodUrl);
    prodServer.setDescription("URL del servidor en entorno de producción");

    Contact contact = new Contact();
    contact.setEmail("salvador_alvarezj@finanzastlax.gob.mx");
    contact.setName("SECRETARÍA DE FINANZAS Desarrollador / (ING:SalvadorAlvarezJ.)");
    contact.setUrl("https://www.finanzastlax.gob.mx");
    // License mitLicense = new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0");
    Info info = new Info()
        .title("Documentación de la API de Mediapp: (ING.Salvador Álvarez Juárez)")
        .version("3.0")
        .contact(contact)
        .description("En el siguiente menú con se muestran todos los servicios disponibles para su consumo de quien / corresponda.")
        .termsOfService("https://www.finanzastlax.gob.mx");
    // .license(mitLicense);
    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
  }
  // public class SwaggerDemoApplication {
  // }
}