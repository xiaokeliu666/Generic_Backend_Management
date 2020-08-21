package cn.huanzi.qch.baseadmin.common.pojo;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * PageCondition
 */
@Data
public class PageCondition {
    private int page = 1;// Current page number
    private int rows = 10;// Size of page
    private String sidx;// sort field
    private String sord;// sort method

    /**
     * Get JPA pagination object
     */
    public Pageable getPageable() {
        //Illegal page number handler
        if (page < 0) {
            page = 1;
        }
        //Illegal page size handler
        if (rows < 0) {
            rows = 10;
        }
        return PageRequest.of(page - 1, rows);
    }
}
