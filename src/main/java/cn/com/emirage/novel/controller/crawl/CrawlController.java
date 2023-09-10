package cn.com.emirage.novel.controller.crawl;


import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.core.constant.ApiRouterConsts;
import cn.com.emirage.novel.dao.entity.crawl.CrawlSource;
import cn.com.emirage.novel.dto.req.crawl.BatchTaskReqDto;
import cn.com.emirage.novel.dto.req.crawl.CrawlSourceReqDto;
import cn.com.emirage.novel.dto.req.crawl.SingleTaskReqDto;
import cn.com.emirage.novel.dto.resp.crawl.BatchTaskRespDto;
import cn.com.emirage.novel.dto.resp.crawl.CrawlSourceRespDto;
import cn.com.emirage.novel.dto.resp.crawl.SingleTaskRespDto;
import cn.com.emirage.novel.service.BatchTaskService;
import cn.com.emirage.novel.service.CrawlSourceService;
import cn.com.emirage.novel.service.SingleTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ChenBoyun
 * @date 2023-08-20 00:36
 */
@Tag(name = "CrawlController", description = "爬虫管理模块")
@RestController
@RequestMapping(ApiRouterConsts.API_CRAWL_URL_PREFIX)
@RequiredArgsConstructor
public class CrawlController {


    private final CrawlSourceService crawlSourceService;
    private final SingleTaskService singleTaskService;
    private final BatchTaskService batchTaskService;
    /**
     *查询爬虫源列表
     */
    @Operation(summary = "查询爬虫源列表")
    @GetMapping("listCrawlSource")
    public RestResp<List<CrawlSourceRespDto>> listCrawlSource(){
        return crawlSourceService.listCrawlSource();
    }

    /**
     *根据Id查询爬虫源
     */
    @Operation(summary = "根据Id查询爬虫源")
    @GetMapping("getCrawlSourceById/{sourceId}")
    public RestResp<CrawlSource> getCrawlSourceById(
        @Parameter(description = "ID") @PathVariable("sourceId")  Integer sourceId){
        return crawlSourceService.getCrawlSourceById(sourceId);
    }
    /**
     *增加爬虫源
     */
    @PostMapping("addCrawlSource")
    @Operation(summary = "增加爬虫源")
    public RestResp<Void> addCrawlSource(@Valid @RequestBody CrawlSourceReqDto dto){
        return crawlSourceService.addCrawlSource(dto);
    }
    /**
     *修改爬虫源
     */
    @PostMapping("updateCrawlSource")
    @Operation(summary = "修改爬虫源")
    public RestResp<Void> updateCrawlSource(CrawlSourceReqDto dto){
        return crawlSourceService.updateCrawlSource(dto);
    }
    /**
     *关闭或打开爬虫源
     */
    @PostMapping("openOrCloseCrawl")
    @Operation(summary = "关闭或打开爬虫源")
    public RestResp<Void> openOrCloseCrawl(Integer id,Byte status){
        return crawlSourceService.openOrCloseCrawlSource(id,status);
    }

    //=============================单本采集任务====================
    /**
     *增加单本采集任务
     */
    @PostMapping("addSingleTask")
    @Operation(summary = "增加单本采集任务")
    public RestResp<Void> addSingleTask(@Valid @RequestBody SingleTaskReqDto dto){
        return singleTaskService.addSingleTask(dto);
    }
    /**
     *删除单本采集任务
     */
    @PostMapping("deleteSingleTask")
    @Operation(summary = "删除单本采集任务")
    public RestResp<Void> deleteSingleTask(@Valid @RequestBody  Long id){
        return singleTaskService.deleteSingleTask(id);
    }

    /**
     *查询采集任务列表
     */
    @Operation(summary = "查询单本采集任务列表")
    @GetMapping("listSingleTask")
    public RestResp<List<SingleTaskRespDto>> listSingleTask(){
        return singleTaskService.listSingleTask();
    }
    //=============================批量采集任务====================
    /**
     *增加批量采集任务
     */
    @PostMapping("addBatchTask")
    @Operation(summary = "增加批量采集任务")
    public RestResp<Void> addBatchTask(@Valid @RequestBody BatchTaskReqDto dto){
        return batchTaskService.addBatchTask(dto);
    }
    /**
     *删除批量采集任务
     */
    @PostMapping("deleteBatchTask")
    @Operation(summary = "删除批量采集任务")
    public RestResp<Void> deleteBatchTask(Integer id){
        return batchTaskService.deleteBatchTask(id);
    }
    /**
     *查询批量采集任务列表
     */
    @Operation(summary = "查询批量采集任务列表")
    @GetMapping("listBatchTask")
    public RestResp<List<BatchTaskRespDto>> listBatchTask(){
        return batchTaskService.listBatchTask();
    }
    /**
     *开启或关闭批量采集任务
     */
    @Operation(summary = "开启或关闭批量采集任务")
    @GetMapping("openOrCloseBatchTask")
    public RestResp<Void> openOrCloseBatchTask(Integer id,Byte status){
        return batchTaskService.openOrCloseBatchTask(id,status);
    }
}
