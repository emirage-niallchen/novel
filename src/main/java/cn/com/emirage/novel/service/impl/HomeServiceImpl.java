package cn.com.emirage.novel.service.impl;

import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.dto.resp.HomeBookRespDto;
import cn.com.emirage.novel.dto.resp.HomeFriendLinkRespDto;
import cn.com.emirage.novel.manager.cache.FriendLinkCacheManager;
import cn.com.emirage.novel.manager.cache.HomeBookCacheManager;
import cn.com.emirage.novel.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页模块 服务实现类
 *
 * @author xiongxiaoyang
 * @date 2022/5/13
 */
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeBookCacheManager homeBookCacheManager;

    private final FriendLinkCacheManager friendLinkCacheManager;

    @Override
    public RestResp<List<HomeBookRespDto>> listHomeBooks() {
        return RestResp.ok(homeBookCacheManager.listHomeBooks());
    }

    @Override
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {
        return RestResp.ok(friendLinkCacheManager.listFriendLinks());
    }
}
