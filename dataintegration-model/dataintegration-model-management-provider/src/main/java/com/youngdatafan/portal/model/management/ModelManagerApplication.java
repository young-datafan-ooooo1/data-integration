package com.youngdatafan.portal.model.management;

import com.youngdatafan.portal.model.management.config.DefaultDatasource;
import com.youngdatafan.portal.model.management.datasource.dto.DatasourceDTO;
import com.youngdatafan.portal.model.management.datasource.mapper.DatasourceMapper;
import java.util.Date;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.pentaho.di.core.encryption.KettleTwoWayPasswordEncoder;
import org.pentaho.di.core.encryption.TwoWayPasswordEncoderInterface;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.StringUtils;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 系统管理启动Main
 *
 * @author gavin
 * @since 2020-01-09 15:59
 */
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableSwagger2
@MapperScan(value = "com.youngdatafan.portal.model.management.**.mapper")
@ComponentScan(value = "com.youngdatafan.portal.model.management")
public class ModelManagerApplication {

    @Resource
    DefaultDatasource defaultDatasource;

    @Resource
    DatasourceMapper datasourceMapper;

    public static final KettleTwoWayPasswordEncoder PASSWORD_ENCODER = new KettleTwoWayPasswordEncoder();



    public static void main(String[] args) {
        new SpringApplicationBuilder(ModelManagerApplication.class).run(args);
    }


    @PostConstruct
    public void initDatasource() {
        DatasourceDTO datasourceDTO = datasourceMapper.getDatasourceByNameAndUserId(defaultDatasource.getDsName());
        if (StringUtils.isEmpty(datasourceDTO)) {
            defaultDatasource.setCreateTime(new Date());
            defaultDatasource.setUpdateTime(new Date());
            defaultDatasource.setDatasourceId(UUID.randomUUID().toString());
            String encode = PASSWORD_ENCODER.encode(defaultDatasource.getDsPassword());
            defaultDatasource.setDsPassword(encode);
            datasourceMapper.insertDefaultDataSource(defaultDatasource);
        }

    }
}
