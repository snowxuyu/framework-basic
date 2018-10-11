package org.framework.basic.system;

import lombok.Data;

import java.util.List;

/**分页公式  (currentPage-1)*pageSize, pageSize
 * Created by snow on 2015/7/24.
 */
@Data
public class Page {
    /**
     * 分页公式： （（当前页数-1）x 分页大小）， 分页大小
     */

    private List<?> data;  //page存放的数据

    private Integer pageSize; //分页的大小

    private Integer currentPage; //当前页

    private Integer totalRecord; //总共有多少条记录

    private Integer pageCount; //多少页


    public Integer getPageCount() {
        if ( currentPage <= 0) {
            this.setCurrentPage(1);
        }

        return this.getTotalRecord()%this.getPageSize() == 0 ? (this.getTotalRecord()/this.getPageSize()) : (this.getTotalRecord()/this.getPageSize() + 1);
    }

}
