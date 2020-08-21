package cn.huanzi.qch.baseadmin.common.pojo;

import cn.huanzi.qch.baseadmin.util.CopyUtil;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import javax.persistence.Query;
import java.util.List;

/**
 * Pagination object(refer to JqGrid)
 */
@Data
public class PageInfo<M> {
    private int page;// current page number
    private int pageSize;// size of page
    private String sidx;// sortBy
    private String sord;// asc or desc

    private List<M> rows;// result of pagination
    private int records;// amount of records
    private int total;// amount of pages

    /**
     * Get the universal page object
     */
    public static <M> PageInfo<M> of(Page page, Class<M> entityModelClass) {
        int records = (int) page.getTotalElements();
        int pageSize = page.getSize();
        int total = records % pageSize == 0 ? records / pageSize : records / pageSize + 1;

        PageInfo<M> pageInfo = new PageInfo<>();
        pageInfo.setPage(page.getNumber() + 1);// Page number
        pageInfo.setPageSize(pageSize);// Page size
        pageInfo.setRows(CopyUtil.copyList(page.getContent(), entityModelClass));// result of pagination
        pageInfo.setRecords(records);// amount of records
        pageInfo.setTotal(total);// amount of pages
        return pageInfo;
    }

    /**
     * Get the JPA Pagination object
     */
    public static Page readPage(Query query, Pageable pageable, Query countQuery) {
        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return PageableExecutionUtils.getPage(query.getResultList(), pageable, () -> executeCountQuery(countQuery));
    }

    private static Long executeCountQuery(Query countQuery) {
        Assert.notNull(countQuery, "Query can't be null!");

        List<Number> totals = countQuery.getResultList();
        Long total = 0L;
        for (Number number : totals) {
            if (number != null) {
                total += number.longValue();
            }
        }
        return total;
    }
}
