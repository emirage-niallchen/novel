package cn.com.emirage.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.com.emirage.novel.core.constant.CacheConsts;
import cn.com.emirage.novel.core.constant.DatabaseConsts;
import cn.com.emirage.novel.dao.entity.NewsInfo;
import cn.com.emirage.novel.dao.mapper.NewsInfoMapper;
import cn.com.emirage.novel.dto.resp.NewsInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新闻 缓存管理类
 *
 * @author xiongxiaoyang
 * @date 2022/5/12
 */
@Component
@RequiredArgsConstructor
public class NewsCacheManager {

    private final NewsInfoMapper newsInfoMapper;

    /**
     * 最新新闻列表查询，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.LATEST_NEWS_CACHE_NAME)
    public List<NewsInfoRespDto> listLatestNews() {
        // 从新闻信息表中查询出最新发布的两条新闻
        QueryWrapper<NewsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
            .last(DatabaseConsts.SqlEnum.LIMIT_2.getSql());
        return newsInfoMapper.selectList(queryWrapper).stream().map(v -> NewsInfoRespDto.builder()
            .id(v.getId())
            .categoryId(v.getCategoryId())
            .categoryName(v.getCategoryName())
            .title(v.getTitle())
            .sourceName(v.getSourceName())
            .updateTime(v.getUpdateTime())
            .build()).toList();
    }

}
