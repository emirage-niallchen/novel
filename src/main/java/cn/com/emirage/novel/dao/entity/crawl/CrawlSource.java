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
 * @date 2023-08-22 23:32
 */
@TableName("crawl_source")
@Data
public class CrawlSource  implements Serializable {

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
    private static final long serialVersionUID = 1L;

    @Schema(description = "爬虫ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @Schema(description = "站点名，爬虫源名称")
    private String sourceName;
    @Schema(description = "小说分类列表页面Url  通过这个能识别是哪个类别的")
    private String bookListUrl;
    @Schema(description = "分类")
    private String catId1;
    @Schema(description = " 分类")
    private String catId2;
    @Schema(description = "分类 ")
    private String catId3;
    @Schema(description = "分类 ")
    private String catId4;
    @Schema(description = "分类 ")
    private String catId5;
    @Schema(description = "分类 ")
    private String catId6;
    @Schema(description = "分类 ")
    private String catId7;
    @Schema(description = " 书籍Id的匹配表达式 示例：https://www.147xs.org/book/137830/ 表达式为：(\\\\d+)")
    private String bookIdPatten;
    @Schema(description = "详情页url正则表达式  表达式为：https://www.147xs.org/book/(//d+)/ ")
    private String bookDetailUrl;
    @Schema(description = "小说名匹配路径  /html/body/div/div[4]/div[2]/div[1]/h1 ")
    private String bookNamePatten;
    @Schema(description = "作者名匹配路径 ")
    private String authorNamePatten;
    @Schema(description = " 面图片路径匹配规则")
    private String picUrlPatten;
    @Schema(description = " 面图片路径前缀")
    private String picUrlPre;
    @Schema(description = "点击数量匹配路径")
    private String visitCountPatten;
    @Schema(description = "简介路径匹配规则")
    private String descPatten;
    @Schema(description = "更新路径匹配规则")
    private String updateTimePatten;
    @Schema(description = "小说目录页正则表达式")
    private String bookIndexUrl;
    @Schema(description = "小说阅读内容页正则表达式")
    private String bookContentUrl;
    @Schema(description = "小说目录页中章节Url匹配规则")
    private String bookIndexContentUrlPatten;
    @Schema(description = "小说目录页中章节Url前缀")
    private String bookIndexContentPre;
    @Schema(description = "具体内容匹配路径")
    private String contentPatten;
    @Schema(description = "具体内容标题匹配路径")
    private String contentTitlePatten;
    @Schema(description = "状态")
    private Integer sourceStatus;
    @Schema(description = "")
    private LocalDateTime createTime;
    @Schema(description = "")
    private LocalDateTime updateTime;
}
