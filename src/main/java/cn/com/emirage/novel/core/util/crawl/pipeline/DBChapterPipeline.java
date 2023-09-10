package cn.com.emirage.novel.core.util.crawl.pipeline;

import cn.com.emirage.novel.dao.entity.BookInfo;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChenBoyun
 * @date 2023-09-04 17:21
 */

@Component
public class DBChapterPipeline implements Pipeline {

    private Map<String, Integer> map = new HashMap<>();

    public Map<String, Integer> getMap() {
        return map;
    }

    private BookInfo bookInfo;

    public BookInfo getBookInfo(){
        return bookInfo;
    }


    @Override
    public void process(ResultItems resultItems, Task task) {
        map = resultItems.get("chapterUrlList");
        bookInfo=resultItems.get("bookInfo");
    }
}
