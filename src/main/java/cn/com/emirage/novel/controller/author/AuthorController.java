package cn.com.emirage.novel.controller.author;


import cn.com.emirage.novel.core.auth.UserHolder;
import cn.com.emirage.novel.core.common.req.PageReqDto;
import cn.com.emirage.novel.core.common.resp.PageRespDto;
import cn.com.emirage.novel.core.common.resp.RestResp;
import cn.com.emirage.novel.core.constant.ApiRouterConsts;
import cn.com.emirage.novel.core.constant.SystemConfigConsts;
import cn.com.emirage.novel.dto.req.AuthorRegisterReqDto;
import cn.com.emirage.novel.dto.req.BookAddReqDto;
import cn.com.emirage.novel.dto.req.ChapterAddReqDto;
import cn.com.emirage.novel.dto.req.ChapterUpdateReqDto;
import cn.com.emirage.novel.dto.resp.BookChapterRespDto;
import cn.com.emirage.novel.dto.resp.BookInfoRespDto;
import cn.com.emirage.novel.dto.resp.ChapterContentRespDto;
import cn.com.emirage.novel.service.AuthorService;
import cn.com.emirage.novel.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * 作家后台-作家模块 API 控制器
 *
 * @author xiongxiaoyang
 * @date 2022/5/23
 */
@Tag(name = "AuthorController", description = "作家后台-作者模块")
@SecurityRequirement(name = SystemConfigConsts.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final BookService bookService;

    /**
     * 作家注册接口
     */
    @Operation(summary = "作家注册接口")
    @PostMapping("register")
    public RestResp<Void> register(@Valid @RequestBody AuthorRegisterReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return authorService.register(dto);
    }

    /**
     * 查询作家状态接口
     */
    @Operation(summary = "作家状态查询接口")
    @GetMapping("status")
    public RestResp<Integer> getStatus() {
        return authorService.getStatus(UserHolder.getUserId());
    }

    /**
     * 小说发布接口
     */
    @Operation(summary = "小说发布接口")
    @PostMapping("book")
    public RestResp<Void> publishBook(@Valid @RequestBody BookAddReqDto dto) {
        return bookService.saveBook(dto);
    }

    /**
     * 小说发布列表查询接口
     */
    @Operation(summary = "小说发布列表查询接口")
    @GetMapping("books")
    public RestResp<PageRespDto<BookInfoRespDto>> listBooks(@ParameterObject PageReqDto dto) {
        return bookService.listAuthorBooks(dto);
    }

    /**
     * 小说章节发布接口
     */
    @Operation(summary = "小说章节发布接口")
    @PostMapping("book/chapter/{bookId}")
    public RestResp<Void> publishBookChapter(
        @Parameter(description = "小说ID") @PathVariable("bookId") Long bookId,
        @Valid @RequestBody ChapterAddReqDto dto) {
        dto.setBookId(bookId);
        return bookService.saveBookChapter(dto);
    }

    /**
     * 小说章节删除接口
     */
    @Operation(summary = "小说章节删除接口")
    @DeleteMapping("book/chapter/{chapterId}")
    public RestResp<Void> deleteBookChapter(
        @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return bookService.deleteBookChapter(chapterId);
    }

    /**
     * 小说章节查询接口
     */
    @Operation(summary = "小说章节查询接口")
    @GetMapping("book/chapter/{chapterId}")
    public RestResp<ChapterContentRespDto> getBookChapter(
        @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return bookService.getBookChapter(chapterId);
    }

    /**
     * 小说章节更新接口
     */
    @Operation(summary = "小说章节更新接口")
    @PutMapping("book/chapter/{chapterId}")
    public RestResp<Void> updateBookChapter(
        @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId,
        @Valid @RequestBody ChapterUpdateReqDto dto) {
        return bookService.updateBookChapter(chapterId, dto);
    }

    /**
     * 小说章节发布列表查询接口
     */
    @Operation(summary = "小说章节发布列表查询接口")
    @GetMapping("book/chapters/{bookId}")
    public RestResp<PageRespDto<BookChapterRespDto>> listBookChapters(
        @Parameter(description = "小说ID") @PathVariable("bookId") Long bookId,
        @ParameterObject PageReqDto dto) {
        return bookService.listBookChapters(bookId, dto);
    }

}
