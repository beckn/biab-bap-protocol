package org.beckn.one.sandbox.bap.protocol.shared.security

import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.IOException
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest


@WebFilter(urlPatterns = ["/*"], filterName = "RequestWrappingFilter")
class RequestWrappingFilter : Filter {

  override fun init(filterConfig: FilterConfig?) {}

  override fun doFilter(request: ServletRequest?, response: ServletResponse?, filterChain: FilterChain) {
    val wrapped = if (request != null && request is HttpServletRequest) ContentCachingRequestWrapper(request) else request
    filterChain.doFilter(wrapped, response)
  }

  override fun destroy() {}
}