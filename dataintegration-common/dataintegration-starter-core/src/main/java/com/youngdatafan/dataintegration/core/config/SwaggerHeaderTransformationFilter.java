package com.youngdatafan.dataintegration.core.config;

import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.swagger2.web.SwaggerTransformationContext;
import springfox.documentation.swagger2.web.WebMvcSwaggerTransformationFilter;

/**
 * swagger header自定义处理过滤器.
 * @author gavin
 * @create 2022/2/9 21:06
 */
@Component
public class SwaggerHeaderTransformationFilter implements WebMvcSwaggerTransformationFilter {

    private static final String HEADER_NAME = "X-Forwarded-Prefix";

    @Override
    public Swagger transform(SwaggerTransformationContext<HttpServletRequest> context) {
        HttpServletRequest request = context.request().get();

        final String basePath = request.getHeader(HEADER_NAME);
        if (basePath == null || !basePath.startsWith("/api")) {
            // 如果没有HEADER_NAME请求头，并且不是/api开头，则代表不是通过网关访问，就不需要设置header
            return context.getSpecification();
        }

        Swagger swagger = context.getSpecification();
        swagger.setBasePath(basePath);

        swagger.getPaths().forEach((s, pathItem) -> {
            for (Operation readOperation : pathItem.getOperations()) {
                final List<Parameter> parameters = readOperation.getParameters();
                if (parameters == null) {
                    continue;
                }

                List<Parameter> newParameters = new ArrayList<>();
                for (Parameter parameter : parameters) {
                    if (!parameter.getIn().equalsIgnoreCase("header")) {
                        newParameters.add(parameter);
                    }
                }
                readOperation.setParameters(newParameters);
            }
        });

        return swagger;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return documentationType.equals(DocumentationType.SWAGGER_2);
    }

}
