package com.youngdatafan.dataintegration.core.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * swagger自动配置.
 *
 * @author gavin
 * @create 2020/8/21 4:39 下午
 */
@ConditionalOnClass(value = EnableOpenApi.class)
@EnableOpenApi
@EnableKnife4j
@Configuration
@Import(SwaggerHeaderTransformationFilter.class)
public class SwaggerConfiguration {

    /**
     * 创建Docket.
     *
     * @return Docket
     */
    @Bean
    public Docket createRestApi() {
        //返回文档摘要信息
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            .paths(PathSelectors.any())
            .build()
            .globalRequestParameters(getGlobalRequestParameters())
            .globalResponses(HttpMethod.GET, getGlobalResponseMessage())
            .globalResponses(HttpMethod.POST, getGlobalResponseMessage());
    }

    /**
     * 生成接口信息，包括标题、联系人等.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("接口文档")
            .description("")
            .contact(new Contact("stella", "senses-ai", "stella@senses-ai.com"))
            .version("1.0")
            .build();
    }

    /**
     * 生成全局通用参数.
     */
    private List<RequestParameter> getGlobalRequestParameters() {
        return Collections.emptyList();
    }

    /**
     * 生成通用响应信息.
     */
    private List<Response> getGlobalResponseMessage() {
        List<Response> responseList = new ArrayList<>();
        responseList.add(new ResponseBuilder().code("404").description("找不到资源").build());
        return responseList;
    }

}
