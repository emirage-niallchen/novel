package cn.com.emirage.novel.dao.entity.crawl;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author ChenBoyun
 * @date 2023-08-22 23:58
 */
@TableName("crawl_single_task")
@Data
public class SingleTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @Schema(description = "爬虫源ID")
    private Integer sourceId;
    @Schema(description = "爬虫源名称")
    private String sourceName;
    @Schema(description = "这个小说的网址")
    private String sourceBookUrl;
    @Schema(description = "小说的类别")
    private Long catId;
    @Schema(description = "小说名称")
    private String bookName;
    @Schema(description = "作者名字")
    private String authorName;
    @Schema(description = "任务状态")//0 失败 1 成功  2  排队中
    private Integer taskStatus;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
