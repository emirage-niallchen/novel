package cn.com.emirage.novel.dto.resp.crawl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author ChenBoyun
 * @date 2023-08-23 10:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleTaskRespDto {
    @Schema(description = "任务Id")
    private Long id;
    @Schema(description = "爬虫源名称")
    private String sourceName;
    @Schema(description = "小说名称")
    private String bookName;
    @Schema(description = "作者名字")
    private String authorName;
    @Schema(description = "任务状态")//0 失败 1 成功  2  排队中
    private Integer taskStatus;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
