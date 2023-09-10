package cn.com.emirage.novel.service.impl;

import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.dao.entity.crawl.CrawlSource;
import cn.com.emirage.novel.dao.mapper.CrawlSourceMapper;
import cn.com.emirage.novel.dto.req.crawl.CrawlSourceReqDto;
import cn.com.emirage.novel.dto.resp.crawl.CrawlSourceRespDto;
import cn.com.emirage.novel.service.CrawlSourceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenBoyun
 * @date 2023-08-23 01:20
 */
@Service
@RequiredArgsConstructor
public class CrawlSourceServiceImpl implements CrawlSourceService {

    private final CrawlSourceMapper crawlSourceMapper;
    @Override
    public RestResp<List<CrawlSourceRespDto>> listCrawlSource() {
        QueryWrapper<CrawlSource> queryWrapper = new QueryWrapper<>();
        List<CrawlSourceRespDto> list = new ArrayList<>();
        list = crawlSourceMapper.selectList(queryWrapper).stream().map(v ->
            CrawlSourceRespDto.builder()
                .id(v.getId())
                .sourceName(v.getSourceName())
                .sourceStatus(v.getSourceStatus())
                .createTime(v.getCreateTime())
                .updateTime(v.getUpdateTime())
                .build()).toList();
        return RestResp.ok(list);
    }

    @Override
    public RestResp<CrawlSource> getCrawlSourceById(Integer id) {
        return RestResp.ok(crawlSourceMapper.selectById(id));
    }

    @Override
    public RestResp<Void> addCrawlSource(CrawlSourceReqDto dto) {
        // 保存作家注册信息
        CrawlSource crawlSource = new CrawlSource();
        crawlSource.setSourceName(dto.getSourceName());
        crawlSource.setBookListUrl(dto.getBookListUrl());
        crawlSource.setCatId1(dto.getCatId1());
        crawlSource.setCatId2(dto.getCatId2());
        crawlSource.setCatId3(dto.getCatId3());
        crawlSource.setCatId4(dto.getCatId4());
        crawlSource.setCatId5(dto.getCatId5());
        crawlSource.setCatId6(dto.getCatId6());
        crawlSource.setCatId7(dto.getCatId7());
        crawlSource.setBookIdPatten(dto.getBookIdPatten());
        crawlSource.setBookDetailUrl(dto.getBookDetailUrl());
        crawlSource.setBookNamePatten(dto.getBookNamePatten());
        crawlSource.setAuthorNamePatten(dto.getAuthorNamePatten());
        crawlSource.setPicUrlPatten(dto.getPicUrlPatten());
        crawlSource.setVisitCountPatten(dto.getVisitCountPatten());
        crawlSource.setDescPatten(dto.getDescPatten());
        crawlSource.setUpdateTimePatten(dto.getUpdateTimePatten());
        crawlSource.setBookIndexUrl(dto.getBookIndexUrl());
        crawlSource.setBookContentUrl(dto.getBookContentUrl());
        crawlSource.setSourceStatus(1);
        crawlSource.setCreateTime(LocalDateTime.now());
        crawlSource.setUpdateTime(LocalDateTime.now());
        crawlSource.setPicUrlPre(dto.getPicUrlPre());
        crawlSource.setBookIndexContentUrlPatten(dto.getBookIndexContentUrlPatten());
        crawlSource.setBookIndexContentPre(dto.getBookIndexContentPre());
        crawlSource.setContentTitlePatten(dto.getContentTitlePatten());
        crawlSourceMapper.insert(crawlSource);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> updateCrawlSource(CrawlSourceReqDto dto) {
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> openOrCloseCrawlSource(Integer sourceId, Byte sourceStatus) {
        return RestResp.ok();
    }
}
