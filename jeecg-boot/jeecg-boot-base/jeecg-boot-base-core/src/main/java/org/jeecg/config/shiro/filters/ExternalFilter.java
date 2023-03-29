package org.jeecg.config.shiro.filters;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.jeecg.common.constant.CommonConstant;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author : nadir
 * @create 2023/3/29 11:35
 */
@Slf4j
public class ExternalFilter extends BasicHttpAuthenticationFilter {
    private static final String X_ACCESS_TOKEN = "0lq5JWR0b+FNTGzTcBDDGpacX05EG9FbGokUK/LTQBtqJNd43isCZ6pRfqQ5sQ6y/0XKhk43bdIrafSQGO8KSLg0dnlJnaGLWVxnYCJ4dCabISdTyFozvMgtLFdVfgI/ikhclkjFiCBI6ujoTplY3ED+uRr3h2DiF7Epfj0S4s/ELq/ZUHH7fXlYzAgA+D6OVdvNTXVzfKX0XhGb94l0RzBTOQLKn2yNjau4M1WUlkaiVtMpDmGFYrI0jn/pNq8W";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        return X_ACCESS_TOKEN.equals(httpServletRequest.getHeader(CommonConstant.X_ACCESS_TOKEN));
    }
}
