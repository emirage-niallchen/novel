package cn.com.emirage.novel.core.util.crawl.pageprocessor;

import cn.com.emirage.novel.core.util.crawl.config.CrawlConstant;
import cn.com.emirage.novel.dao.entity.BookChapter;
import cn.com.emirage.novel.dao.entity.BookContent;
import cn.com.emirage.novel.dao.entity.BookInfo;
import cn.com.emirage.novel.dao.entity.crawl.CrawlSource;
import cn.com.emirage.novel.dao.mapper.BookChapterMapper;
import cn.com.emirage.novel.dao.mapper.BookContentMapper;
import cn.com.emirage.novel.dao.mapper.BookInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenBoyun
 * @date 2023-09-07 11:46
 */
@Component
public class SingleTaskPageProcessor implements PageProcessor {

    private CrawlSource crawlSource;


    public void setCrawlSource(CrawlSource crawlSource){
        this.crawlSource=crawlSource;
    }


    private  static BookChapterMapper bookChapterMapper;
    private static BookContentMapper bookContentMapper;
    private static BookInfoMapper bookInfoMapper;

    @Autowired
    public void setBookContentMapper(BookContentMapper bookContentMapper){
        SingleTaskPageProcessor.bookContentMapper=bookContentMapper;
    }

    @Autowired
    public void setBookCategoryMapper(BookChapterMapper bookChapterMapper){
        SingleTaskPageProcessor.bookChapterMapper=bookChapterMapper;
    }

    @Autowired
    public void setBookInfoMapper(BookInfoMapper bookInfoMapper){
        SingleTaskPageProcessor.bookInfoMapper = bookInfoMapper;
    }

    @Override
    public void process(Page page) {
//获得url,添加所有章节信息，保存内容，如果为空，说明是目录页，那么跳过，但是保存bookinfo,如果不是空的那么，保存章节信息，在pipeline里面存储。
        String url = page.getUrl().get();

        if(!url.endsWith("html")){
            indexProcess(page);
        }else{
            contentProcess(page);
        }
    }

    public void indexProcess(Page page){
        //存储bookinfo的数据，然后,爬取所有的url,放进队列,每个请求有bookId以及对应的章节号。

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
        bookInfoMapper.insert(bookInfo);
        //每个请求都加上对应的编号，以及书籍的Id
        List<String> list = page.getHtml().xpath(crawlSource.getBookIndexContentUrlPatten()).links().all();
        for (int i = 0; i < list.size(); i++) {
            String contentUrl =  list.get(i);
            Request request = new Request();
            request.putExtra("bookId",bookInfo.getId());
            request.putExtra("chapterNum",i);
            request.setUrl(contentUrl);
            page.addTargetRequest(request);
        }

        page.setSkip(true);


    }
    public void contentProcess(Page page){
        //保存这个请求的内容信息
        int chapterNum = page.getRequest().getExtra("chapterNum");
        long bookId = page.getRequest().getExtra("bookId");
        BookContent bookContent = new BookContent();
        BookChapter bookChapter = new BookChapter();

        List<String> contentList = page.getHtml().xpath(crawlSource.getContentPatten()).all();
        StringBuilder contentBuilder = new StringBuilder();
        for (String str : contentList){
            contentBuilder.append(str);
        }
        String content = contentBuilder.toString();

        bookChapter.setUpdateTime(LocalDateTime.now());
        bookChapter.setCreateTime(LocalDateTime.now());
        bookChapter.setChapterNum(chapterNum);
        bookChapter.setBookId(bookId);
        bookChapter.setChapterName(page.getHtml().xpath(crawlSource.getContentTitlePatten()).get());
        bookChapter.setIsVip(0);
        bookChapter.setWordCount(content.length());

        bookChapterMapper.insert(bookChapter);

        long chapterId = bookChapter.getId();

        bookContent.setContent(content);
        bookContent.setChapterId(chapterId);
        bookContent.setUpdateTime(LocalDateTime.now());
        bookContent.setCreateTime(LocalDateTime.now());

        bookContentMapper.insert(bookContent);

    }


    @Override
    public Site getSite() {
        return site;
    }

    private Site site = Site.me().setCharset("GBK")
        .setTimeOut(CrawlConstant.CHAPTER_TIME_OUT)
        .setRetryTimes(CrawlConstant.CHAPTER_RETRY_TIMES)
        .setCycleRetryTimes(CrawlConstant.CHAPTER_CYCLE_RETRY_TIMES);
}
