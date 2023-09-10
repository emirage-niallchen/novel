package cn.com.emirage.novel.dto.req.crawl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author ChenBoyun
 * @date 2023-08-23 10:07
 */
@Data
public class SingleTaskReqDto {

    @Schema(description = "爬虫源名称")
    private String sourceName;
    @Schema(description = "这个小说的网址")
    private String sourceBookUrl;
    @Schema(description = "小说的类别")
    private String catId;
    @Schema(description = "小说名称")
    private String bookName;
    @Schema(description = "作者名字")
    private String authorName;

}
