package cn.com.emirage.novel.core.util.crawl.config;

/**
 * @author ChenBoyun
 * @date 2023-09-04 17:45
 */

public  final  class CrawlConstant {
    public static final int BOOK_TIME_OUT = 10000;
    public static final int CHAPTER_TIME_OUT = 10000;
    public static final int CONTENT_TIME_OUT = 10000;

    public static final int BOOK_RETRY_TIMES= 5;
    public static final int CHAPTER_RETRY_TIMES= 5;
    public static final int CONTENT_RETRY_TIMES= 5;

    public static final int BOOK_CYCLE_RETRY_TIMES = 3;
    public static final int CHAPTER_CYCLE_RETRY_TIMES = 5;
    public static final int CONTENT_CYCLE_RETRY_TIMES = 5;

    public static final int BOOK_THREAD_NUM = 5;
    public  static  int CHAPTER_THREAD_NUM = 5;
    public static final int CONTENT_THREAD_NUM = 5;

    public static final int BOOK_SLEEP_TIME = 100;
    public static final int CHAPTER_SLEEP_TIME = 100;
    public static final int CONTENT_SLEEP_TIME = 100;

}
