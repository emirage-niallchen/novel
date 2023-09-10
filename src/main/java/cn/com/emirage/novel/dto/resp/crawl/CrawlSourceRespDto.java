package cn.com.emirage.novel.dto.resp.crawl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenBoyun
 * @date 2023-08-23 01:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrawlSourceRespDto {


    @Schema(description = "爬虫ID")
    private Integer id;
    @Schema(description = "站点名，爬虫源名称")
    private String sourceName;
    @Schema(description = "开启状态")
    private Integer sourceStatus;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
