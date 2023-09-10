package cn.com.emirage.novel.service.impl;

import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.core.constant.DatabaseConsts;
import cn.com.emirage.novel.core.util.crawl.pageprocessor.SingleTaskPageProcessor;
import cn.com.emirage.novel.dao.entity.BookCategory;
import cn.com.emirage.novel.dao.entity.crawl.CrawlSource;
import cn.com.emirage.novel.dao.entity.crawl.SingleTask;
import cn.com.emirage.novel.dao.mapper.BookCategoryMapper;
import cn.com.emirage.novel.dao.mapper.CrawlSourceMapper;
import cn.com.emirage.novel.dao.mapper.SingleTaskMapper;
import cn.com.emirage.novel.dto.req.crawl.SingleTaskReqDto;
import cn.com.emirage.novel.dto.resp.crawl.SingleTaskRespDto;
import cn.com.emirage.novel.service.SingleTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenBoyun
 * @date 2023-08-23 01:20
 */
@Service
@RequiredArgsConstructor
public class SingleTaskServiceImpl implements SingleTaskService {

    private final SingleTaskMapper singleTaskMapper;

    private  final BookCategoryMapper bookCategoryMapper;

    private final CrawlSourceMapper crawlSourceMapper;

    private final SingleTaskPageProcessor singleTaskPageProcessor;

    @Override
    public RestResp<Void> addSingleTask(SingleTaskReqDto dto) {
        SingleTask singleTask = new SingleTask();
        singleTask.setSourceName(dto.getSourceName());
        singleTask.setSourceBookUrl(dto.getSourceBookUrl());
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCategoryTable.COLUMN_NAME,dto.getCatId());
        QueryWrapper<CrawlSource> crawlSourceQueryWrapper = new QueryWrapper<>();
        crawlSourceQueryWrapper.eq(DatabaseConsts.CrawlSourceTable.COLUMN_SOURCE_NAME,dto.getSourceName());
        CrawlSource crawlSource = crawlSourceMapper.selectOne(crawlSourceQueryWrapper);
        singleTask.setSourceId(crawlSource.getId());
        long id = bookCategoryMapper.selectOne(queryWrapper).getId();
        singleTask.setCatId(id);
        singleTask.setBookName(dto.getBookName());
        singleTask.setAuthorName(dto.getAuthorName());
        singleTask.setTaskStatus(2);
        singleTask.setCreateTime(LocalDateTime.now());
        singleTaskMapper.insert(singleTask);



        singleTaskPageProcessor.setCrawlSource(crawlSource);

        Spider.create(singleTaskPageProcessor)
                .addUrl(dto.getSourceBookUrl())
                .thread(5)
                .run();

        singleTask.setTaskStatus(1);
        singleTaskMapper.updateById(singleTask);
        //此处开始爬取
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteSingleTask(Long id) {
        singleTaskMapper.deleteById(id);
        return RestResp.ok();
    }

    @Override
    public RestResp<SingleTaskRespDto> getSingleTaskById(Integer id) {
        SingleTaskRespDto dto = new SingleTaskRespDto();
        return RestResp.ok(dto);
    }

    @Override
    public RestResp<List<SingleTaskRespDto>> listSingleTask() {
        QueryWrapper<SingleTask> queryWrapper = new QueryWrapper<>();
        List<SingleTaskRespDto> list = singleTaskMapper.selectList(queryWrapper).stream().map(v ->
                SingleTaskRespDto.builder()
                    .sourceName(v.getSourceName())
                    .bookName(v.getBookName())
                    .authorName(v.getAuthorName())
                    .taskStatus(v.getTaskStatus())
                    .createTime(v.getCreateTime())
                    .id(v.getId())
                    .build()).toList();
        return RestResp.ok(list);
    }
}
