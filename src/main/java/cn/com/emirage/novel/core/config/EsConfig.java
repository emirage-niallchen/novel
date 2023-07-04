package cn.com.emirage.novel.core.config;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ChenBoyun
 * @date 2023-05-31 15:35
 */
@Configuration
public class EsConfig {

    /**
     * 解决 ElasticsearchClientConfigurations 修改默认 ObjectMapper 配置的问题
     */
    @Bean
    JacksonJsonpMapper jacksonJsonpMapper() {
        return new JacksonJsonpMapper();
    }

}
