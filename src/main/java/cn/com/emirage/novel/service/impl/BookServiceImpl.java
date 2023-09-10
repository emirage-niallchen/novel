package cn.com.emirage.novel.service.impl;

import cn.com.emirage.novel.core.annotation.Key;
import cn.com.emirage.novel.core.annotation.Lock;
import cn.com.emirage.novel.core.auth.UserHolder;
import cn.com.emirage.novel.core.common.constant.ErrorCodeEnum;
import cn.com.emirage.novel.core.common.req.PageReqDto;
import cn.com.emirage.novel.core.common.resp.PageRespDto;
import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.core.constant.DatabaseConsts;
import cn.com.emirage.novel.dao.entity.*;
import cn.com.emirage.novel.dao.mapper.*;
import cn.com.emirage.novel.dto.AuthorInfoDto;
import cn.com.emirage.novel.dto.req.BookAddReqDto;
import cn.com.emirage.novel.dto.req.ChapterAddReqDto;
import cn.com.emirage.novel.dto.req.ChapterUpdateReqDto;
import cn.com.emirage.novel.dto.req.UserCommentReqDto;
import cn.com.emirage.novel.dto.resp.*;
import cn.com.emirage.novel.manager.cache.*;
import cn.com.emirage.novel.manager.dao.UserDaoManager;
import cn.com.emirage.novel.manager.mq.AmqpMsgManager;
import cn.com.emirage.novel.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 小说模块 服务实现类
 *
 * @author xiongxiaoyang
 * @date 2022/5/14
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookCategoryCacheManager bookCategoryCacheManager;

    private final BookCategoryMapper bookCategoryMapper;

    private final BookRankCacheManager bookRankCacheManager;

    private final BookInfoCacheManager bookInfoCacheManager;

    private final BookChapterCacheManager bookChapterCacheManager;

    private final BookContentCacheManager bookContentCacheManager;

    private final AuthorInfoCacheManager authorInfoCacheManager;

    private final BookInfoMapper bookInfoMapper;

    private final BookChapterMapper bookChapterMapper;

    private final BookContentMapper bookContentMapper;

    private final BookCommentMapper bookCommentMapper;

    private final UserDaoManager userDaoManager;

    private final AmqpMsgManager amqpMsgManager;

    private static final Integer REC_BOOK_COUNT = 4;

    @Override
    public RestResp<List<BookRankRespDto>> listVisitRankBooks() {
        return RestResp.ok(bookRankCacheManager.listVisitRankBooks());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listNewestRankBooks() {
        return RestResp.ok(bookRankCacheManager.listNewestRankBooks());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listUpdateRankBooks() {
        return RestResp.ok(bookRankCacheManager.listUpdateRankBooks());
    }

    @Override
    public RestResp<BookInfoRespDto> getBookById(Long bookId) {
        return RestResp.ok(bookInfoCacheManager.getBookInfo(bookId));
    }

    @Override
    public RestResp<BookChapterAboutRespDto> getLastChapterAbout(Long bookId) {
        // 查询小说信息
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookId);

        // 查询最新章节信息
        BookChapterRespDto bookChapter = bookChapterCacheManager.getChapter(
            bookInfo.getLastChapterId());

        // 查询章节内容
        String content = bookContentCacheManager.getBookContent(bookInfo.getLastChapterId());

        // 查询章节总数
        QueryWrapper<BookChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId);
        Long chapterTotal = bookChapterMapper.selectCount(chapterQueryWrapper);

        // 组装数据并返回
        return RestResp.ok(BookChapterAboutRespDto.builder()
            .chapterInfo(bookChapter)
            .chapterTotal(chapterTotal)
            .contentSummary(content.substring(0, 30))
            .build());
    }

    @Override
    public RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId)
        throws NoSuchAlgorithmException {
        Long categoryId = bookInfoCacheManager.getBookInfo(bookId).getCategoryId();
        List<Long> lastUpdateIdList = bookInfoCacheManager.getLastUpdateIdList(categoryId);
        List<BookInfoRespDto> respDtoList = new ArrayList<>();
        List<Integer> recIdIndexList = new ArrayList<>();
        int count = 0;
        Random rand = SecureRandom.getInstanceStrong();
        while (count < REC_BOOK_COUNT) {
            int recIdIndex = rand.nextInt(lastUpdateIdList.size());
            if (!recIdIndexList.contains(recIdIndex)) {
                recIdIndexList.add(recIdIndex);
                bookId = lastUpdateIdList.get(recIdIndex);
                BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookId);
                respDtoList.add(bookInfo);
                count++;
            }
        }
        return RestResp.ok(respDtoList);
    }

    @Override
    public RestResp<Void> addVisitCount(Long bookId) {
        bookInfoMapper.addVisitCount(bookId);
        return RestResp.ok();
    }

    @Override
    public RestResp<Long> getPreChapterId(Long chapterId) {
        // 查询小说ID 和 章节号
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();

        // 查询上一章信息并返回章节ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
            .lt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
            .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
            .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        return RestResp.ok(
            Optional.ofNullable(bookChapterMapper.selectOne(queryWrapper))
                .map(BookChapter::getId)
                .orElse(null)
        );
    }

    @Override
    public RestResp<Long> getNextChapterId(Long chapterId) {
        // 查询小说ID 和 章节号
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();

        // 查询下一章信息并返回章节ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
            .gt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
            .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
            .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        return RestResp.ok(
            Optional.ofNullable(bookChapterMapper.selectOne(queryWrapper))
                .map(BookChapter::getId)
                .orElse(null)
        );
    }

    @Override
    public RestResp<List<BookChapterRespDto>> listChapters(Long bookId) {
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
            .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM);
        return RestResp.ok(bookChapterMapper.selectList(queryWrapper).stream()
            .map(v -> BookChapterRespDto.builder()
                .id(v.getId())
                .chapterName(v.getChapterName())
                .isVip(v.getIsVip())
                .build()).toList());
    }

    @Override
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection) {
        return RestResp.ok(bookCategoryCacheManager.listCategory(workDirection));
    }

    @Override
    public RestResp<List<BookCategoryRespDto>> listCategoryAll() {
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        return RestResp.ok(
            bookCategoryMapper.selectList(queryWrapper).stream().map(v ->
                BookCategoryRespDto.builder()
                    .id(v.getId())
                    .name(v.getName())
                    .build()).toList()
        );
    }

    @Lock(prefix = "userComment")
    @Override
    public RestResp<Void> saveComment(
        @Key(expr = "#{userId + '::' + bookId}") UserCommentReqDto dto) {
        // 校验用户是否已发表评论
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID, dto.getUserId())
            .eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID, dto.getBookId());
        if (bookCommentMapper.selectCount(queryWrapper) > 0) {
            // 用户已发表评论
            return RestResp.fail(ErrorCodeEnum.USER_COMMENTED);
        }
        BookComment bookComment = new BookComment();
        bookComment.setBookId(dto.getBookId());
        bookComment.setUserId(dto.getUserId());
        bookComment.setCommentContent(dto.getCommentContent());
        bookComment.setCreateTime(LocalDateTime.now());
        bookComment.setUpdateTime(LocalDateTime.now());
        bookCommentMapper.insert(bookComment);
        return RestResp.ok();
    }

    @Override
    public RestResp<BookCommentRespDto> listNewestComments(Long bookId) {
        // 查询评论总数
        QueryWrapper<BookComment> commentCountQueryWrapper = new QueryWrapper<>();
        commentCountQueryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID, bookId);
        Long commentTotal = bookCommentMapper.selectCount(commentCountQueryWrapper);
        BookCommentRespDto bookCommentRespDto = BookCommentRespDto.builder()
            .commentTotal(commentTotal).build();
        if (commentTotal > 0) {

            // 查询最新的评论列表
            QueryWrapper<BookComment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID, bookId)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                .last(DatabaseConsts.SqlEnum.LIMIT_5.getSql());
            List<BookComment> bookComments = bookCommentMapper.selectList(commentQueryWrapper);

            // 查询评论用户信息，并设置需要返回的评论用户名
            List<Long> userIds = bookComments.stream().map(BookComment::getUserId).toList();
            List<UserInfo> userInfos = userDaoManager.listUsers(userIds);
            Map<Long, UserInfo> userInfoMap = userInfos.stream()
                .collect(Collectors.toMap(UserInfo::getId, Function.identity()));
            List<BookCommentRespDto.CommentInfo> commentInfos = bookComments.stream()
                .map(v -> BookCommentRespDto.CommentInfo.builder()
                    .id(v.getId())
                    .commentUserId(v.getUserId())
                    .commentUser(userInfoMap.get(v.getUserId()).getUsername())
                    .commentUserPhoto(userInfoMap.get(v.getUserId()).getUserPhoto())
                    .commentContent(v.getCommentContent())
                    .commentTime(v.getCreateTime()).build()).toList();
            bookCommentRespDto.setComments(commentInfos);
        } else {
            bookCommentRespDto.setComments(Collections.emptyList());
        }
        return RestResp.ok(bookCommentRespDto);
    }

    @Override
    public RestResp<Void> deleteComment(Long userId, Long commentId) {
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), commentId)
            .eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID, userId);
        bookCommentMapper.delete(queryWrapper);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> updateComment(Long userId, Long id, String content) {
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), id)
            .eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID, userId);
        BookComment bookComment = new BookComment();
        bookComment.setCommentContent(content);
        bookCommentMapper.update(bookComment, queryWrapper);
        return RestResp.ok();
    }

    @Override
    public RestResp<Void> saveBook(BookAddReqDto dto) {
        // 校验小说名是否已存在
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookTable.COLUMN_BOOK_NAME, dto.getBookName());
        if (bookInfoMapper.selectCount(queryWrapper) > 0) {
            return RestResp.fail(ErrorCodeEnum.AUTHOR_BOOK_NAME_EXIST);
        }
        BookInfo bookInfo = new BookInfo();
        // 设置作家信息
        AuthorInfoDto author = authorInfoCacheManager.getAuthor(UserHolder.getUserId());
        bookInfo.setAuthorId(author.getId());
        bookInfo.setAuthorName(author.getPenName());
        // 设置其他信息
        bookInfo.setWorkDirection(dto.getWorkDirection());
        bookInfo.setCategoryId(dto.getCategoryId());
        bookInfo.setCategoryName(dto.getCategoryName());
        bookInfo.setBookName(dto.getBookName());
        bookInfo.setPicUrl(dto.getPicUrl());
        bookInfo.setBookDesc(dto.getBookDesc());
        bookInfo.setIsVip(dto.getIsVip());
        bookInfo.setScore(0);
        bookInfo.setCreateTime(LocalDateTime.now());
        bookInfo.setUpdateTime(LocalDateTime.now());
        // 保存小说信息
        bookInfoMapper.insert(bookInfo);
        return RestResp.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResp<Void> saveBookChapter(ChapterAddReqDto dto) {
        // 校验该作品是否属于当前作家
        BookInfo bookInfo = bookInfoMapper.selectById(dto.getBookId());
        if (!Objects.equals(bookInfo.getAuthorId(), UserHolder.getAuthorId())) {
            return RestResp.fail(ErrorCodeEnum.USER_UN_AUTH);
        }
        // 1) 保存章节相关信息到小说章节表
        //  a) 查询最新章节号
        int chapterNum = 0;
        QueryWrapper<BookChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, dto.getBookId())
            .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
            .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookChapter bookChapter = bookChapterMapper.selectOne(chapterQueryWrapper);
        if (Objects.nonNull(bookChapter)) {
            chapterNum = bookChapter.getChapterNum() + 1;
        }
        //  b) 设置章节相关信息并保存
        BookChapter newBookChapter = new BookChapter();
        newBookChapter.setBookId(dto.getBookId());
        newBookChapter.setChapterName(dto.getChapterName());
        newBookChapter.setChapterNum(chapterNum);
        newBookChapter.setWordCount(dto.getChapterContent().length());
        newBookChapter.setIsVip(dto.getIsVip());
        newBookChapter.setCreateTime(LocalDateTime.now());
        newBookChapter.setUpdateTime(LocalDateTime.now());
        bookChapterMapper.insert(newBookChapter);

        // 2) 保存章节内容到小说内容表
        BookContent bookContent = new BookContent();
        bookContent.setContent(dto.getChapterContent());
        bookContent.setChapterId(newBookChapter.getId());
        bookContent.setCreateTime(LocalDateTime.now());
        bookContent.setUpdateTime(LocalDateTime.now());
        bookContentMapper.insert(bookContent);

        // 3) 更新小说表最新章节信息和小说总字数信息
        //  a) 更新小说表关于最新章节的信息
        BookInfo newBookInfo = new BookInfo();
        newBookInfo.setId(dto.getBookId());
        newBookInfo.setLastChapterId(newBookChapter.getId());
        newBookInfo.setLastChapterName(newBookChapter.getChapterName());
        newBookInfo.setLastChapterUpdateTime(LocalDateTime.now());
        newBookInfo.setWordCount(bookInfo.getWordCount() + newBookChapter.getWordCount());
        newBookChapter.setUpdateTime(LocalDateTime.now());
        bookInfoMapper.updateById(newBookInfo);
        //  b) 清除小说信息缓存
        bookInfoCacheManager.evictBookInfoCache(dto.getBookId());
        //  c) 发送小说信息更新的 MQ 消息
        amqpMsgManager.sendBookChangeMsg(dto.getBookId());
        return RestResp.ok();
    }

    @Override
    public RestResp<PageRespDto<BookInfoRespDto>> listAuthorBooks(PageReqDto dto) {
        IPage<BookInfo> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookTable.AUTHOR_ID, UserHolder.getAuthorId())
            .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        IPage<BookInfo> bookInfoPage = bookInfoMapper.selectPage(page, queryWrapper);
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(),
            bookInfoPage.getRecords().stream().map(v -> BookInfoRespDto.builder()
                .id(v.getId())
                .bookName(v.getBookName())
                .picUrl(v.getPicUrl())
                .categoryName(v.getCategoryName())
                .wordCount(v.getWordCount())
                .visitCount(v.getVisitCount())
                .updateTime(v.getUpdateTime())
                .build()).toList()));
    }

    @Override
    public RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(Long bookId, PageReqDto dto) {
        IPage<BookChapter> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
            .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM);
        IPage<BookChapter> bookChapterPage = bookChapterMapper.selectPage(page, queryWrapper);
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(),
            bookChapterPage.getRecords().stream().map(v -> BookChapterRespDto.builder()
                .id(v.getId())
                .chapterName(v.getChapterName())
                .chapterUpdateTime(v.getUpdateTime())
                .isVip(v.getIsVip())
                .build()).toList()));
    }


    @Override
    public RestResp<PageRespDto<UserCommentRespDto>> listComments(Long userId, PageReqDto pageReqDto) {
        IPage<BookComment> page = new Page<>();
        page.setCurrent(pageReqDto.getPageNum());
        page.setSize(pageReqDto.getPageSize());
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID, userId)
            .orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName());
        IPage<BookComment> bookCommentPage = bookCommentMapper.selectPage(page, queryWrapper);
        List<BookComment> comments = bookCommentPage.getRecords();
        if (!CollectionUtils.isEmpty(comments)) {
            List<Long> bookIds = comments.stream().map(BookComment::getBookId).toList();
            QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
            bookInfoQueryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(), bookIds);
            Map<Long, BookInfo> bookInfoMap = bookInfoMapper.selectList(bookInfoQueryWrapper).stream()
                .collect(Collectors.toMap(BookInfo::getId, Function.identity()));
            return RestResp.ok(PageRespDto.of(pageReqDto.getPageNum(), pageReqDto.getPageSize(), page.getTotal(),
                comments.stream().map(v -> UserCommentRespDto.builder()
                    .commentContent(v.getCommentContent())
                    .commentBook(bookInfoMap.get(v.getBookId()).getBookName())
                    .commentBookPic(bookInfoMap.get(v.getBookId()).getPicUrl())
                    .commentTime(v.getCreateTime())
                    .build()).toList()));

        }
        return RestResp.ok(PageRespDto.of(pageReqDto.getPageNum(), pageReqDto.getPageSize(), page.getTotal(),
            Collections.emptyList()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResp<Void> deleteBookChapter(Long chapterId) {
        // 1.查询章节信息
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        // 2.查询小说信息
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(chapter.getBookId());
        // 3.删除章节信息
        bookChapterMapper.deleteById(chapterId);
        // 4.删除章节内容
        QueryWrapper<BookContent> bookContentQueryWrapper = new QueryWrapper<>();
        bookContentQueryWrapper.eq(DatabaseConsts.BookContentTable.COLUMN_CHAPTER_ID, chapterId);
        bookContentMapper.delete(bookContentQueryWrapper);
        // 5.更新小说信息
        BookInfo newBookInfo = new BookInfo();
        newBookInfo.setId(chapter.getBookId());
        newBookInfo.setUpdateTime(LocalDateTime.now());
        newBookInfo.setWordCount(bookInfo.getWordCount() - chapter.getChapterWordCount());
        if (Objects.equals(bookInfo.getLastChapterId(), chapterId)) {
            // 设置最新章节信息
            QueryWrapper<BookChapter> bookChapterQueryWrapper = new QueryWrapper<>();
            bookChapterQueryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, chapter.getBookId())
                .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
            BookChapter bookChapter = bookChapterMapper.selectOne(bookChapterQueryWrapper);
            Long lastChapterId = 0L;
            String lastChapterName = "";
            LocalDateTime lastChapterUpdateTime = null;
            if (Objects.nonNull(bookChapter)) {
                lastChapterId = bookChapter.getId();
                lastChapterName = bookChapter.getChapterName();
                lastChapterUpdateTime = bookChapter.getUpdateTime();
            }
            newBookInfo.setLastChapterId(lastChapterId);
            newBookInfo.setLastChapterName(lastChapterName);
            newBookInfo.setLastChapterUpdateTime(lastChapterUpdateTime);
        }
        bookInfoMapper.updateById(newBookInfo);
        // 6.清理章节信息缓存
        bookChapterCacheManager.evictBookChapterCache(chapterId);
        // 7.清理章节内容缓存
        bookContentCacheManager.evictBookContentCache(chapterId);
        // 8.清理小说信息缓存
        bookInfoCacheManager.evictBookInfoCache(chapter.getBookId());
        // 9.发送小说信息更新的 MQ 消息
        amqpMsgManager.sendBookChangeMsg(chapter.getBookId());
        return RestResp.ok();
    }

    @Override
    public RestResp<ChapterContentRespDto> getBookChapter(Long chapterId) {
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        String bookContent = bookContentCacheManager.getBookContent(chapterId);
        return RestResp.ok(
            ChapterContentRespDto.builder()
                .chapterName(chapter.getChapterName())
                .chapterContent(bookContent)
                .isVip(chapter.getIsVip())
                .build());
    }

    @Transactional
    @Override
    public RestResp<Void> updateBookChapter(Long chapterId, ChapterUpdateReqDto dto) {
        // 1.查询章节信息
        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);
        // 2.查询小说信息
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(chapter.getBookId());
        // 3.更新章节信息
        BookChapter newChapter = new BookChapter();
        newChapter.setId(chapterId);
        newChapter.setChapterName(dto.getChapterName());
        newChapter.setWordCount(dto.getChapterContent().length());
        newChapter.setIsVip(dto.getIsVip());
        newChapter.setUpdateTime(LocalDateTime.now());
        bookChapterMapper.updateById(newChapter);
        // 4.更新章节内容
        BookContent newContent = new BookContent();
        newContent.setContent(dto.getChapterContent());
        newContent.setUpdateTime(LocalDateTime.now());
        QueryWrapper<BookContent> bookContentQueryWrapper = new QueryWrapper<>();
        bookContentQueryWrapper.eq(DatabaseConsts.BookContentTable.COLUMN_CHAPTER_ID, chapterId);
        bookContentMapper.update(newContent, bookContentQueryWrapper);
        // 5.更新小说信息
        BookInfo newBookInfo = new BookInfo();
        newBookInfo.setId(chapter.getBookId());
        newBookInfo.setUpdateTime(LocalDateTime.now());
        newBookInfo.setWordCount(
            bookInfo.getWordCount() - chapter.getChapterWordCount() + dto.getChapterContent().length());
        if (Objects.equals(bookInfo.getLastChapterId(), chapterId)) {
            // 更新最新章节信息
            newBookInfo.setLastChapterName(dto.getChapterName());
            newBookInfo.setLastChapterUpdateTime(LocalDateTime.now());
        }
        bookInfoMapper.updateById(newBookInfo);
        // 6.清理章节信息缓存
        bookChapterCacheManager.evictBookChapterCache(chapterId);
        // 7.清理章节内容缓存
        bookContentCacheManager.evictBookContentCache(chapterId);
        // 8.清理小说信息缓存
        bookInfoCacheManager.evictBookInfoCache(chapter.getBookId());
        // 9.发送小说信息更新的 MQ 消息
        amqpMsgManager.sendBookChangeMsg(chapter.getBookId());
        return RestResp.ok();
    }

    @Override
    public RestResp<BookContentAboutRespDto> getBookContentAbout(Long chapterId) {
        log.debug("userId:{}", UserHolder.getUserId());
        // 查询章节信息
        BookChapterRespDto bookChapter = bookChapterCacheManager.getChapter(chapterId);

        // 查询章节内容
        String content = bookContentCacheManager.getBookContent(chapterId);

        // 查询小说信息
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookChapter.getBookId());

        // 组装数据并返回
        return RestResp.ok(BookContentAboutRespDto.builder()
            .bookInfo(bookInfo)
            .chapterInfo(bookChapter)
            .bookContent(content)
            .build());
    }
}
