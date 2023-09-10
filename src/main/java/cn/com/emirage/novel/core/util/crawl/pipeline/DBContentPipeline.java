package cn.com.emirage.novel.core.util.crawl.pipeline;

import cn.com.emirage.novel.dao.entity.BookChapter;
import cn.com.emirage.novel.dao.entity.BookContent;
import cn.com.emirage.novel.dao.mapper.BookChapterMapper;
import cn.com.emirage.novel.dao.mapper.BookContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author ChenBoyun
 * @date 2023-09-04 17:21
 */
@Component
public class DBContentPipeline implements Pipeline {




    private  static BookChapterMapper bookChapterMapper;
    private static BookContentMapper bookContentMapper;

    @Autowired
    public void setBookContentMapper(BookContentMapper bookContentMapper){
        DBContentPipeline.bookContentMapper=bookContentMapper;
    }

    @Autowired
    public void setBookCategoryMapper(BookChapterMapper bookChapterMapper){
        DBContentPipeline.bookChapterMapper=bookChapterMapper;
    }



    @Override
    public void process(ResultItems resultItems, Task task) {
        BookChapter bookChapter = resultItems.get("bookChapter");
        BookContent bookContent = resultItems.get("bookCon");
        bookChapterMapper.insert(bookChapter);
        bookContent.setChapterId(bookChapter.getId());
        bookContentMapper.insert(bookContent);
    }
}
