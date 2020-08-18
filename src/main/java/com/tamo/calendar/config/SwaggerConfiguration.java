package com.tamo.calendar.config;

import com.tamo.calendar.model.user.User;
import com.tamo.calendar.model.interview.Interview;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.tamo.calendar"))
                .build()
                .apiInfo(apiInfo())
                .ignoredParameterTypes(User.class, Interview.class)
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Calendar Application - Tamo challenge")
                .description("Calendar to set interviews between candidates and interviewers")
                .version("1.0.0")
                .build();
    }
}
