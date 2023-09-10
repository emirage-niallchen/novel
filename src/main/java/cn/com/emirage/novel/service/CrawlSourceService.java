package cn.com.emirage.novel.service;



import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.dao.entity.crawl.CrawlSource;
import cn.com.emirage.novel.dto.req.crawl.CrawlSourceReqDto;
import cn.com.emirage.novel.dto.resp.crawl.CrawlSourceRespDto;

import java.util.List;

/**
 *没有删除是因为，一对多的关系，如果删除了，正在执行的任务可能会有些问题
 *
 * @author 陈博云
 * @date 2023/8/23 1:31
 */
public interface CrawlSourceService {
    //查询爬虫源列表

    RestResp<List<CrawlSourceRespDto>> listCrawlSource();

    //查询爬虫源，根据ID

    RestResp<CrawlSource> getCrawlSourceById(Integer id);

    //增加
    RestResp<Void> addCrawlSource(CrawlSourceReqDto dto);

    //修改
    RestResp<Void> updateCrawlSource(CrawlSourceReqDto dto);

    //开启或关闭
    RestResp<Void>openOrCloseCrawlSource(Integer sourceId,Byte sourceStatus);
}
