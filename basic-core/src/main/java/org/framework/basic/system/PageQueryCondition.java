package org.framework.basic.system;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: snowxuyu
 * Date: 2017-9-3
 * Time: 20:08
 */
@Data
public class PageQueryCondition {

    /** 每页开始数标 */
    private Integer pageStart;
    /** 每页显示条数 */
    private Integer pageSize;
    /** 当前请求页 */
    private Integer currentPage = 1;

    public Integer getPageStar() {
        return (this.getCurrentPage() - 1) <= 0 ? 0 : (this.getCurrentPage() - 1) * this.getPageSize();
    }

    public Page getPage() {
        Page page = new Page();
        page.setCurrentPage(this.getCurrentPage());
        page.setPageSize(this.getPageSize());
        return page;
    }

}
