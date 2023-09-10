package cn.com.emirage.novel.service;



import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.dto.req.crawl.BatchTaskReqDto;
import cn.com.emirage.novel.dto.resp.crawl.BatchTaskRespDto;

import java.util.List;

public interface BatchTaskService {
/**
 * @author 陈博云
 * @date 2023/8/23 10:11
 * 增加
 */
RestResp<Void> addBatchTask(BatchTaskReqDto dto);
/**
 * @author 陈博云
 * @date 2023/8/23 10:11
 *删除
 */
RestResp<Void> deleteBatchTask(Integer id);
/**
 * @author 陈博云
 * @date 2023/8/23 10:11
 *查询
 */
RestResp<BatchTaskRespDto> getBatchTaskById(Integer id);
/**
 * @author 陈博云
 * @date 2023/8/23 10:11
 *列表查询
 */
RestResp<List<BatchTaskRespDto>> listBatchTask();
/**
 * @author 陈博云
 * @date 2023/8/23 10:11
 *开启/关闭
 */

RestResp<Void> openOrCloseBatchTask(Integer Id,Byte taskStatus);
}
