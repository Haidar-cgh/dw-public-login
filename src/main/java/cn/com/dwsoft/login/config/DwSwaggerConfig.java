package cn.com.dwsoft.login.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author sqw
 * @version 1.0
 * @description TODO
 * @ClassName DwSwaggerConfig
 * @Date 2020/12/8
 * @since jdk1.8
 */
@EnableSwagger2
@Configuration
@Profile({"dev"})
public class DwSwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                // 详细定制
                .apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("智享通服务接口API")
                .description("智享通服务接口API")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .build();
    }
}
