package com.excellmobility.trafficstate;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class Main
{
  private static final String swaggerBaseUrl = "/TrafficStateService-swagger";

  public static void main(String[] args)
  {
    org.springframework.boot.SpringApplication.run(Main.class, args);

  }

  @Bean
  public Docket docket()
  {
     return new Docket(DocumentationType.SWAGGER_2)
           .apiInfo(apiInfo())
           .select()
           .apis(RequestHandlerSelectors.any())
           .paths(PathSelectors.regex("/TrafficStateService.*"))
           .build();
  }

  
  
  
  private ApiInfo apiInfo()
  {
     return new ApiInfoBuilder()
           .title("ExCELL Traffic Strate Service")
           .description("This service offers an API JUST FOR INTERNAL USE ;-)")
           .version("1.0")
           .contact(new Contact("TU Dresden - Chair of Traffic Control and Process Automatisation", "http://tu-dresden.de/vlp", "henning.jeske@tu-dresden.de"))
           .build();
  }
  
  
  @Controller
  @Configuration
  public class ConfigurerAdapter implements WebMvcConfigurer
  {
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
      registry.addViewController(swaggerBaseUrl + "/v2/api-docs").setViewName("forward:/v2/api-docs");
      registry.addViewController(swaggerBaseUrl + "/swagger-resources/configuration/ui").setViewName("forward:/swagger-resources/configuration/ui");
      registry.addViewController(swaggerBaseUrl + "/swagger-resources/configuration/security").setViewName("forward:/swagger-resources/configuration/security");
      registry.addViewController(swaggerBaseUrl + "/swagger-resources").setViewName("forward:/swagger-resources");
      registry.addViewController(swaggerBaseUrl + "/public").setViewName("forward:/public");
      registry.addRedirectViewController(swaggerBaseUrl, swaggerBaseUrl + "/swagger-ui.html");
      registry.addRedirectViewController(swaggerBaseUrl + "/", swaggerBaseUrl + "/swagger-ui.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
      registry.addResourceHandler(swaggerBaseUrl + "/**").addResourceLocations("classpath:/META-INF/resources/");
    }
  }
}
