package com.workmotion.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {


    @Bean
    public Docket workmotion() {
        return new Docket(SWAGGER_2)
                .groupName("Work Motion")
                .apiInfo(applicationInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.workmotion"))
                .build();
    }


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

    private ApiInfo applicationInfo() {
        return new ApiInfoBuilder()
                .title("Work Motion API")
                .description("This API helps manage the employment status of employees.")
                .license("GP")
                .contact(new Contact("Toseef Zafar", "", "mtouseef.zafar@gmail.com"))
                .build();
    }
}

