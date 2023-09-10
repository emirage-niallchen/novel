package cn.com.emirage.novel.dto.resp.crawl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenBoyun
 * @date 2023-08-23 10:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchTaskRespDto {
    @Schema(description = "ID")
    private Long id;
    @Schema(description = "爬虫源ID")
    private Integer sourceId;
    @Schema(description = "爬虫源名字")
    private String sourceName;
    @Schema(description = "成功爬取数量")
    private Integer crawlCountSuccess;
    @Schema(description = "任务状态")
    private Integer taskStatus;
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
