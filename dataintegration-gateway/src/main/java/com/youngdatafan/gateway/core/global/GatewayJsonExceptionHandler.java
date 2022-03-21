package com.youngdatafan.gateway.core.global;


import com.youngdatafan.dataintegration.core.util.StatusCode;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 网关json异常控制器
 *
 * @author gavin
 * @since 2020/2/7 12:54 下午
 */
public class GatewayJsonExceptionHandler extends DefaultErrorWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GatewayJsonExceptionHandler.class);

    public GatewayJsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                       ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 构建返回的JSON数据格式
     *
     * @param code 返回状态码
     * @param msg  提示信息
     * @return java.util.HashMap
     */
    private static Map<String, Object> buildResult(String code, String msg) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }

    /**
     * 构建返回的JSON数据格式
     *
     * @param code 返回状态码
     * @param msg  提示信息
     * @return java.util.HashMap
     */
    private static Map<String, Object> buildResult(StatusCode code, String msg) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("code", code.getCode());
        map.put("msg", msg);
        return map;
    }

    /**
     * 获取异常属性
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Throwable error = super.getError(request);
        // 构建异常信息
        String errorMessage = buildMessage(request, error);
        logger.error(errorMessage, error);

        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
            return buildResult(StatusCode.CODE_10001, "服务不可用，请重新登录，或者联系管理员。");
        }

        if (error instanceof org.springframework.cloud.gateway.support.TimeoutException) {
            return buildResult(StatusCode.CODE_10010, "接口请求超时。");
        }

        if (error.getCause() != null && error.getCause() instanceof org.springframework.cloud.gateway.support.TimeoutException) {
            return buildResult(StatusCode.CODE_10010, "接口请求超时。");
        }

        return buildResult(StatusCode.CODE_99999, "系统繁忙。");
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     *
     * @param errorAttributes 错误属性
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * Render the error information as a JSON payload.
     *
     * @param request the current request
     * @return a {@code Publisher} of the HTTP response
     */
    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        boolean includeStackTrace = isIncludeStackTrace(request, MediaType.ALL);
        Map<String, Object> error = getErrorAttributes(request, includeStackTrace);
        return ServerResponse.status(getHttpStatus(error)).contentType(MediaType.APPLICATION_JSON)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "*")
                .header("Access-Control-Max-Age", "3600")
                .header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
                .body(BodyInserters.fromValue(error));
    }

    /**
     * 根据code获取对应的HttpStatus
     *
     * @param errorAttributes 错误属性
     */
    @Override
    protected int getHttpStatus(Map<String, Object> errorAttributes) {
        return 200;
    }

    /**
     * 构建异常信息
     *
     * @param request ServerRequest
     * @param ex      Throwable
     * @return 异常信息
     */
    private String buildMessage(ServerRequest request, Throwable ex) {
        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.methodName());
        message.append(" ");
        message.append(request.uri());
        message.append("]");
        if (ex != null) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }

}
