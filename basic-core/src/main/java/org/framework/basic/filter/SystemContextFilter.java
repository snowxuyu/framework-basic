package org.framework.basic.filter;

import com.alibaba.druid.util.StringUtils;
import org.framework.basic.constant.Constants;
import org.framework.basic.system.SystemContext;
import org.framework.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * Created by snow on 2015/7/24.
 * 使用注解的方式，自己写的分页功能太麻烦了，统一使用分页插件
 */
//@WebFilter(filterName = "SystemContextFilter", urlPatterns = "/*", initParams = {@WebInitParam(name = "pageSize", value = "10")})
@Deprecated
public class SystemContextFilter implements Filter {

    private final Logger logger =  LoggerFactory.getLogger(getClass());

	@Override
    public void init(FilterConfig config) throws ServletException {
        try {
            Integer.parseInt(config.getInitParameter(Constants.Common.PAGE_SIZE));
        } catch (BaseException e) {
            logger.error("从web.xml配置文件中获取pageSize失败!"+e.getMessage(),e);

        }

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String currentPage = request.getParameter(Constants.Common.CURRENT_PAGE);
            if (!StringUtils.isEmpty(currentPage)) {
                SystemContext.setCurrentPage(Integer.parseInt(currentPage));
            }
            String sort = request.getParameter(Constants.Common.SORT);
            if (!StringUtils.isEmpty(sort)) {
            	 SystemContext.setSort(sort);
            }
            String order = request.getParameter(Constants.Common.ORDER);
            if (!StringUtils.isEmpty(order)) {
            	SystemContext.setOrders(order);
            }
            SystemContext.setRealPath(((HttpServletRequest) request).getSession().getServletContext().getRealPath("/"));
            chain.doFilter(request, response);
        }catch (BaseException e) {
            logger.error("分页参数  currentPage  获取失败,传入参数为:"+request.getParameter(Constants.Common.CURRENT_PAGE));
        } finally {
            SystemContext.removeCurrentPage();
            SystemContext.removePageSize();
            SystemContext.removeSort();
            SystemContext.removeOrders();
            SystemContext.removeRealPath();
        }
    }

    @Override
    public void destroy() {

    }
}
