package cn.com.emirage.novel.core.util.crawl.pageprocessor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author ChenBoyun
 * @date 2023-09-04 17:18
 */
public class BookProcessor implements PageProcessor {

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return PageProcessor.super.getSite();
    }
}
