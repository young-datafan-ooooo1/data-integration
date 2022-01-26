package com.youngdatafan.portal.model.management.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {
    public static RestTemplate getRestTemplateWithPool(Integer poolSize, Integer perRouteSize, Integer connectTimeout, Integer readTimeout, Integer waitTimeout){

        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        // 将最大连接数增加
        manager.setMaxTotal(poolSize);
        // 将每个路由基础的连接增加
        manager.setDefaultMaxPerRoute(perRouteSize);
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(manager)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(client);
        // 数据读取超时时间
        factory.setReadTimeout(1000 * readTimeout);
        // 连接不够用的等待时间
        factory.setConnectionRequestTimeout(1000 * waitTimeout);
        // 连接超时
        factory.setConnectTimeout(1000 * connectTimeout);

        return new RestTemplate(factory);
    }
}
