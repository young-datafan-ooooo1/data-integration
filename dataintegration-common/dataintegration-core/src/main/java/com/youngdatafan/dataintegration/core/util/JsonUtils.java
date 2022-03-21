package com.youngdatafan.dataintegration.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

/**
 * json工具类.
 *
 * @author renhua.zhang
 * @since 2017-10-28 10:32
 **/
public class JsonUtils {

    /**
     * mapper.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 对象转json字符串.
     *
     * @param obj 对象
     * @return json字符串
     */
    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * json字符串转对象.
     *
     * @param json json字符串
     * @param <T>  对象class
     * @param t    t
     * @return T
     * @throws IOException json格式错误
     */
    public static <T> T parseObject(String json, Class<T> t) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return MAPPER.readValue(json, t);
    }

    /**
     * json字符串转对象.
     *
     * @param json json字符串
     * @param <T>  对象class
     * @param type type
     * @return T
     * @throws IOException json格式错误
     */
    public static <T> T parseObject(String json, TypeReference<T> type) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return MAPPER.readValue(json, type);
    }

    /**
     * json字符串转数组.
     *
     * @param json json字符串
     * @param <T>  对象class new TypeReference
     * @param type type
     * @return T
     * @throws IOException json格式错误
     */
    public static <T> T parseArray(String json, TypeReference<T> type) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return MAPPER.readValue(json, type);
    }

}
