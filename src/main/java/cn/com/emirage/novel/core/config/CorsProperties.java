package cn.com.emirage.novel.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author ChenBoyun
 * @date 2023-05-30 21:12
 */
@ConfigurationProperties(prefix = "novel.cors")
@Data
public class CorsProperties {
    private List<String> allowOrigins;
}
