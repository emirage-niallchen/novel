package cn.com.emirage.novel.service;



import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.dto.req.crawl.SingleTaskReqDto;
import cn.com.emirage.novel.dto.resp.crawl.SingleTaskRespDto;

import java.util.List;

public interface SingleTaskService {
    /**
     * @author 陈博云
     * @date 2023/8/23 10:11
     * 增加
     */
    RestResp<Void> addSingleTask(SingleTaskReqDto dto);
    /**
     * @author 陈博云
     * @date 2023/8/23 10:11
     *删除
     */
    RestResp<Void> deleteSingleTask(Long id);
    /**
     * @author 陈博云
     * @date 2023/8/23 10:11
     *查询
     */
    RestResp<SingleTaskRespDto> getSingleTaskById(Integer id);
    /**
     * @author 陈博云
     * @date 2023/8/23 10:11
     *列表查询
     */
    RestResp<List<SingleTaskRespDto>> listSingleTask();

}
