package cn.com.emirage.novel.core.util.crawl.pageprocessor;

import cn.com.emirage.novel.core.util.crawl.config.CrawlConstant;
import cn.com.emirage.novel.dao.entity.BookChapter;
import cn.com.emirage.novel.dao.entity.BookContent;
import cn.com.emirage.novel.dao.entity.crawl.CrawlSource;
import lombok.Data;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author ChenBoyun
 * @date 2023-09-04 17:04
 */

@Data
public class ContentProcessor implements PageProcessor {



    private CrawlSource crawlSource;



    private Map<String, Integer> map;
    private Long id;

    public ContentProcessor(Map<String, Integer> map,Long id,CrawlSource crawlSource) {
        this.map=map;
        this.id=id;
        this.crawlSource = crawlSource;
    }



    @Override
    public void process(Page page) {

        int chapterNum = map.get(page.getUrl().get());
        BookChapter bookChapter = new BookChapter();
        BookContent bookContent = new BookContent();
        List<String> contentList = page.getHtml().xpath(crawlSource.getContentPatten()).all();
        StringBuilder contentBuilder = new StringBuilder();
        for (String str : contentList){
            contentBuilder.append(str);
        }
        String content = contentBuilder.toString();

        bookChapter.setBookId(id);
        bookChapter.setChapterNum(chapterNum);
        bookChapter.setChapterName(page.getHtml().xpath(crawlSource.getContentTitlePatten()).get());
        bookChapter.setWordCount(content.length());
        bookChapter.setIsVip(0);
        bookChapter.setCreateTime(LocalDateTime.now());
        bookChapter.setUpdateTime(LocalDateTime.now());

        bookContent.setContent(content);
        bookContent.setCreateTime(LocalDateTime.now());
        bookContent.setUpdateTime(LocalDateTime.now());


        page.putField("wordCount",bookChapter.getWordCount());
        page.putField("bookChapter",bookChapter);
        page.putField("bookContent",bookContent);

    }


    private Site site = Site.me()
        .setRetryTimes(CrawlConstant.CONTENT_RETRY_TIMES)
        .setSleepTime(CrawlConstant.CONTENT_SLEEP_TIME)
        .setTimeOut(CrawlConstant.CONTENT_TIME_OUT);

    @Override
    public Site getSite() {
        return site;
    }
}
