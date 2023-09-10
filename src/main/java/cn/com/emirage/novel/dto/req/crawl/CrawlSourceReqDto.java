package cn.com.emirage.novel.dto.req.crawl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author ChenBoyun
 * @date 2023-08-23 09:33
 */
@Data
public class CrawlSourceReqDto {

    /**
     * @return null
     * @author 陈博云
     * @date 2023/8/29 14:47
     *       'sourceName': '',
     *       'bookListUrl': '',
     *       'catId1': '',
     *       'catId2': '',
     *       'catId3': '',
     *       'catId4': '',
     *       'catId5': '',
     *       'catId6': '',
     *       'catId7': '',
     *       'bookIdPatten': '',
     *       'bookDetailUrl': '',
     *       'bookNamePatten': '',
     *       'authorNamePatten': '',
     *       'picUrlPatten': '',
     *       'visitCountPatten': '',
     *       'descPatten': '',
     *       'upadateTimePatten': '',
     *       'bookIndexUrl': '',
     *       'bookContentUrl': '',
     *       'contentPatten': '',
     */

    @Schema(description = "站点名，爬虫源名称")
    private String sourceName;
    @Schema(description = " ")
    private String bookListUrl;
    @Schema(description = " ")
    private String catId1;
    @Schema(description = " ")
    private String catId2;
    @Schema(description = " ")
    private String catId3;
    @Schema(description = " ")
    private String catId4;
    @Schema(description = " ")
    private String catId5;
    @Schema(description = " ")
    private String catId6;
    @Schema(description = " ")
    private String catId7;
    @Schema(description = " ")
    private String bookIdPatten;
    @Schema(description = " ")
    private String bookDetailUrl;
    @Schema(description = " ")
    private String bookNamePatten;
    @Schema(description = " ")
    private String authorNamePatten;
    @Schema(description = " ")
    private String picUrlPatten;
    @Schema(description = "匹配规则")
    private String visitCountPatten;
    @Schema(description = "开启状态")
    private String descPatten;
    @Schema(description = "创建时间")
    private String updateTimePatten;
    @Schema(description = "更新时间")
    private String bookIndexUrl;
    @Schema(description = "更新时间")
    private String bookContentUrl;
    @Schema(description = "小说目录页中章节Url匹配规则")
    private String bookIndexContentUrlPatten;
    @Schema(description = "小说目录页中章节Url前缀")
    private String bookIndexContentPre;
    @Schema(description = "更新时间")
    private String contentPatten;
    @Schema(description = "具体内容标题匹配路径")
    private String contentTitlePatten;
    @Schema(description = " 面图片路径前缀")
    private String picUrlPre;
}
