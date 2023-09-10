package cn.com.emirage.novel.dao.entity.crawl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ChenBoyun
 * @date 2023-08-23 00:00
 */
@TableName("crawl_batch_task")
@Data
public class BatchTask {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "爬取的Url")
    private String sourceCatUrl;
    @Schema(description = "爬虫源ID")
    private Integer sourceId;
    @Schema(description = "爬虫源名字")
    private String sourceName;
    @Schema(description = "成功爬取数量")
    private Integer crawlCountSuccess;
    @Schema(description = "任务状态 0 错误 ；1 运行中； 2 完成； 3 暂停")
    private Integer taskStatus;
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
}
