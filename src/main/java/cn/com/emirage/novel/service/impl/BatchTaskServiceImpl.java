package cn.com.emirage.novel.service.impl;

import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.core.constant.DatabaseConsts;
import cn.com.emirage.novel.dao.entity.crawl.BatchTask;
import cn.com.emirage.novel.dao.entity.crawl.CrawlSource;
import cn.com.emirage.novel.dao.mapper.BatchTaskMapper;
import cn.com.emirage.novel.dao.mapper.CrawlSourceMapper;
import cn.com.emirage.novel.dto.req.crawl.BatchTaskReqDto;
import cn.com.emirage.novel.dto.resp.crawl.BatchTaskRespDto;
import cn.com.emirage.novel.service.BatchTaskService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ChenBoyun
 * @date 2023-08-23 01:20
 */
@Service
@RequiredArgsConstructor
public class BatchTaskServiceImpl implements BatchTaskService {


    private final BatchTaskMapper batchTaskMapper;
    private final CrawlSourceMapper crawlSourceMapper;

    @Override
    public RestResp<Void> addBatchTask(BatchTaskReqDto dto) {
        BatchTask batchTask = new BatchTask();
        batchTask.setSourceId(dto.getSourceId());
        batchTask.setSourceName(dto.getSourceName());
        batchTask.setSourceCatUrl(dto.getSourceCatUrl());
        batchTask.setCrawlCountSuccess(0);
        batchTask.setTaskStatus(1);
        batchTask.setStartTime(LocalDateTime.now());
        int id = crawlSourceMapper.selectOne(new QueryWrapper<CrawlSource>().eq(
            DatabaseConsts.CrawlSourceTable.COLUMN_SOURCE_NAME,dto.getSourceName()
        )).getId();
        batchTask.setSourceId(id);
        batchTaskMapper.insert(batchTask);
        //此处开始爬取
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> deleteBatchTask(Integer id) {
        return RestResp.ok();
    }

    @Override
    public RestResp<BatchTaskRespDto> getBatchTaskById(Integer id) {
        return null;
    }

    @Override
    public RestResp<List<BatchTaskRespDto>> listBatchTask() {
        QueryWrapper<BatchTask> queryWrapper = new QueryWrapper<>();
        List<BatchTaskRespDto> list = batchTaskMapper.selectList(queryWrapper).stream().map(v ->
            BatchTaskRespDto.builder()
                .sourceName(v.getSourceName())
                .crawlCountSuccess(v.getCrawlCountSuccess())
                .taskStatus(v.getTaskStatus())
                .startTime(v.getStartTime())
                .endTime(v.getEndTime())
                .build()).toList();
        return RestResp.ok(list);
    }

    @Override
    public RestResp<Void> openOrCloseBatchTask(Integer Id, Byte taskStatus) {
        return RestResp.ok();
    }
}
