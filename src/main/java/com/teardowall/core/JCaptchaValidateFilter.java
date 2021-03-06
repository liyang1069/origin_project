package com.teardowall.core;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class JCaptchaValidateFilter extends AccessControlFilter {
  private boolean jcaptchaEbabled = false;// 是否开启验证码支持
  private String jcaptchaParam = "jcaptchaCode";// 前台提交的验证码参数名
  private String failureKeyAttribute = "shiroLoginFailure"; // 验证失败后存储到的属性名

  public void setJcaptchaEbabled(boolean jcaptchaEbabled) {
    this.jcaptchaEbabled = jcaptchaEbabled;
  }

  public void setJcaptchaParam(String jcaptchaParam) {
    this.jcaptchaParam = jcaptchaParam;
  }

  public void setFailureKeyAttribute(String failureKeyAttribute) {
    this.failureKeyAttribute = failureKeyAttribute;
  }

  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    // 1、设置验证码是否开启属性，页面可以根据该属性来决定是否显示验证码
    request.setAttribute("jcaptchaEbabled", jcaptchaEbabled);

    HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
    // 2、判断验证码是否禁用 或不是表单提交（允许访问）
    if (jcaptchaEbabled == false || !"post".equalsIgnoreCase(httpServletRequest.getMethod())) {
      return true;
    }
    // 3、此时是表单提交，验证验证码是否正确
    return JCaptcha.validateResponse(httpServletRequest, httpServletRequest.getParameter(jcaptchaParam));
  }

  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    // 如果验证码失败了，存储失败key属性
    //request.setAttribute(failureKeyAttribute, CommonUtil.i18nStr(request, "验证码错误", "Verification Code Error"));
    request.setAttribute(failureKeyAttribute, "验证码错误");
    return true;
  }
}
