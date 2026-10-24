package com.lut.grade.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 通用分页对象。
 * 页面只需 ${page.list} 与 ${page.pageNo}，页码条由 JSTL 自行渲染。
 */
public class Page<T> {

    public static final int DEFAULT_PAGE_SIZE = 10;

    private int pageNo = 1;
    private int pageSize = DEFAULT_PAGE_SIZE;
    private int totalCount;
    private List<T> list = Collections.emptyList();

    public Page() {
    }

    public Page(int pageNo, int pageSize) {
        setPageNo(pageNo);
        setPageSize(pageSize);
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo < 1 ? 1 : pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount < 0 ? 0 : totalCount;
    }

    /** 总页数。 */
    public int getTotalPage() {
        if (pageSize == 0) {
            return 0;
        }
        return (totalCount + pageSize - 1) / pageSize;
    }

    /** 上一页页码。 */
    public int getPrevPage() {
        return pageNo > 1 ? pageNo - 1 : 1;
    }

    /** 下一页页码。 */
    public int getNextPage() {
        return pageNo < getTotalPage() ? pageNo + 1 : getTotalPage();
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list == null ? new ArrayList<>() : list;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    /** MySQL 分页偏移量。 */
    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
