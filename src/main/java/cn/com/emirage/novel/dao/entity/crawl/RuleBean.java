package cn.com.emirage.novel.dao.entity.crawl;

import lombok.Data;

import java.util.Map;

/**
 * @author ChenBoyun
 * @date 2023-08-21 11:42
 */

//主要需要区别的就是，url的规则和HTML中的规则，在HTML中可以直接抓取。

@Data
public class RuleBean {

    /**
     * 分类列表页URL规则
     * 可以有{catId}用来替换路径，也可以没有
     * {page}用来匹配，只要结尾是数字+html 就是内容页面链接
     * */
    private String bookListUrl;

    //小说类别分类
    private Map<String,String> catIdRule;

    //是连载还是完结了，之后通过这个来判断是否加入更新队列
    private Map<String,Byte> bookStatusRule;

    //匹配Book替换掉bookDetailUrl里面的BookId就是精确的BookDetail了
    private String bookIdPatten;

    //详情页url规则
    private String bookDetailUrl;
    //名字规则
    private String bookNamePatten;
    //作者规则
    private String authorNamePatten;
    //封面路径规则
    private String picUrlPatten;
    //更新状态规则
    private String statusPatten;
    //点击数
    private String visitCountPatten;
    //简介规则
    private String descPatten;
    //更新时间规则
    private String upadateTimePatten;
    //更新时间格式
    private String upadateTimeFormatPatten;
    //小说目录页的URL规则
    private String bookIndexUrl;
    //目录页目录ID正则表达式   如 book/132224
    private String indexIdPatten;
    //目录页目录名正则表达式
    private String indexNamePatten;
    //小说内容页的URL规则
    private String bookContentUrl;
    //小说内容匹配规则
    private String contentPatten;
    //小说图片前缀
    private String picUrlPrefix;



}
