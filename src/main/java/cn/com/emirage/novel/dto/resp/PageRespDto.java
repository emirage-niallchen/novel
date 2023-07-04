package cn.com.emirage.novel.dto.resp;

import lombok.Getter;

import java.util.List;

/**
 * @author ChenBoyun
 * @date 2023-05-30 11:50
 */

@Getter
public class PageRespDto<T> {
    private final long pageNum;
    private final long pageSize;
    private final long total;
    private final List<? extends T> list;
    /**
     * 该构造函数用于通用分页查询的场景
     * 接收普通分页数据和普通集合
     */
    public PageRespDto(long pageNum, long pageSize, long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    public static <T> PageRespDto<T> of(long pageNum, long pageSize, long total, List<T> list) {
        return new PageRespDto<>(pageNum, pageSize, total, list);
    }

    /**
     * 获取分页数
     * */
    public long getPages() {
        if (this.pageSize == 0L) {
            return 0L;
        } else {
            long pages = this.total / this.pageSize;
            if (this.total % this.pageSize != 0L) {
                ++pages;
            }

            return pages;
        }
    }
}
