package org.framework.basic.server.servlet.impl;

import org.framework.basic.server.servlet.config.FilterConfiguration;
import org.framework.basic.server.servlet.config.ServletConfiguration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.LinkedList;

public class FilterChainImpl implements FilterChain {

	private LinkedList<FilterConfiguration> filterConfigurations;

	private ServletConfiguration servletConfiguration;

	public FilterChainImpl(ServletConfiguration servletConfiguration) {
		this.servletConfiguration = servletConfiguration;
	}

	public void addFilterConfiguration(FilterConfiguration config) {

		if (this.filterConfigurations == null) {
            this.filterConfigurations = new LinkedList<FilterConfiguration>();
        }

		this.filterConfigurations.add(config);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response)
			throws IOException, ServletException {
		FilterConfiguration config = filterConfigurations != null ? filterConfigurations.poll() : null;
		if (config != null) {
			config.getHttpComponent().doFilter(request, response, this);
		}

		if (this.servletConfiguration != null) {
			this.servletConfiguration.getHttpComponent().service(request, response);
		}
	}

	public boolean isValid() {
		return this.servletConfiguration != null || (this.filterConfigurations != null && !this.filterConfigurations.isEmpty());
	}

	public ServletConfiguration getServletConfiguration() {
		return servletConfiguration;
	}

}
