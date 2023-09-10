package cn.com.emirage.novel.core.util.crawl.pageprocessor;


import cn.com.emirage.novel.core.util.crawl.config.CrawlConstant;
import cn.com.emirage.novel.dao.entity.BookInfo;
import cn.com.emirage.novel.dao.entity.crawl.CrawlSource;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ChenBoyun
 * @date 2023-09-04 17:02
 */

public class ChapterProcessor implements PageProcessor {





    public CrawlSource crawlSource;


    public ChapterProcessor(CrawlSource crawlSource) {
        this.crawlSource = crawlSource;
    }

    @Override
    public void process(Page page) {
        BookInfo bookInfo = new BookInfo();
        bookInfo.setPicUrl(crawlSource.getPicUrlPre()+page.getHtml().xpath(crawlSource.getPicUrlPatten()).get());
        bookInfo.setBookName(page.getHtml().xpath(crawlSource.getBookNamePatten()).get());
        bookInfo.setAuthorName(page.getHtml().xpath(crawlSource.getAuthorNamePatten()).get());
        bookInfo.setBookDesc(page.getHtml().xpath(crawlSource.getDescPatten()).get());
        bookInfo.setBookStatus(0);
        bookInfo.setUpdateTime(LocalDateTime.now());
        bookInfo.setIsVip(0);
        bookInfo.setCreateTime(LocalDateTime.now());
        long authorId = 0L;
        bookInfo.setAuthorId(authorId);
        bookInfo.setScore(6);
        long visitCount = 9L;
        bookInfo.setVisitCount(visitCount);

        List<String> list = page.getHtml().xpath(crawlSource.getBookIndexContentUrlPatten()).all();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(crawlSource.getBookIndexContentPre() + list.get(i), i );
        }
        page.putField("chapterUrlList", map);
        page.putField("bookInfo",bookInfo);
    }

    @Override
    public Site getSite() {
        return site;
    }

    private Site site = Site.me().setCharset("utf8")
        .setTimeOut(CrawlConstant.CHAPTER_TIME_OUT)
        .setRetryTimes(CrawlConstant.CHAPTER_RETRY_TIMES)
        .setCycleRetryTimes(CrawlConstant.CHAPTER_CYCLE_RETRY_TIMES);
}
