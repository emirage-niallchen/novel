package cn.com.emirage.novel.dto.req.crawl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author ChenBoyun
 * @date 2023-08-23 10:07
 */
@Data
public class BatchTaskReqDto {
    @Schema(description = "爬虫源Id")
    private Integer sourceId;
    @Schema(description = "爬虫源名字")
    private String sourceName;
    @Schema(description = "爬取网址")
    private String sourceCatUrl;
}
