/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.47
 * Generated at: 2018-03-15 22:57:57 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.views.layout;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class sidebar_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("<aside class=\"sidebar\">\r\n");
      out.write("                    <div class=\"sidebar-container\">\r\n");
      out.write("                        <div class=\"sidebar-header\">\r\n");
      out.write("                            <div class=\"brand\">\r\n");
      out.write("                                <div class=\"logo\">\r\n");
      out.write("                                    \r\n");
      out.write("                                </div> SBS </div>\r\n");
      out.write("                        </div>\r\n");
      out.write("                        <nav class=\"menu\">\r\n");
      out.write("                            <ul class=\"sidebar-menu metismenu\" id=\"sidebar-menu\">\r\n");
      out.write("                                <li id=\"scheduleMng\">\r\n");
      out.write("                                    <a href=\"current_schedule\">\r\n");
      out.write("                                        <i class=\"fa fa-calendar\"></i> Schedules </a>\r\n");
      out.write("                                </li>\r\n");
      out.write("                                <li id=\"bookingMng\">\r\n");
      out.write("                                    <a href=\"admin_booking\">\r\n");
      out.write("                                        <i class=\"fa fa-list\"></i> Bookings </a>\r\n");
      out.write("                                </li>\r\n");
      out.write("                                <li id=\"bookingRequestMng\">\r\n");
      out.write("                                    <a href=\"admin_booking_request\">\r\n");
      out.write("                                        <i class=\"fa fa-envelope-open\"></i> Booking Requests</a>\r\n");
      out.write("                                </li>\r\n");
      out.write("                                <li id=\"busMng\">\r\n");
      out.write("                                    <a href=\"bus_management\">\r\n");
      out.write("                                        <i class=\"fa fa-bus\"></i> Manage Buses </a>\r\n");
      out.write("                                </li>\r\n");
      out.write("                                <li id=\"locationMng\">\r\n");
      out.write("                                    <a href=\"location_management\">\r\n");
      out.write("                                        <i class=\"fa fa-institution\"></i> Manage Locations </a>\r\n");
      out.write("                                </li>\r\n");
      out.write("                                <li id=\"reportMng\" >\r\n");
      out.write("                                    <a href=\"report\">\r\n");
      out.write("                                        <i class=\"fa fa-bar-chart\"></i> Reports </a>\r\n");
      out.write("                                </li>\r\n");
      out.write("                                <li >\r\n");
      out.write("                                    <a href=\"customer_home\">\r\n");
      out.write("                                        <i class=\"fa fa-bar-chart\"></i> Reports </a>\r\n");
      out.write("                                </li>\r\n");
      out.write("                                \r\n");
      out.write("                               \r\n");
      out.write("                                \r\n");
      out.write("                            </ul>\r\n");
      out.write("                        </nav>\r\n");
      out.write("                    </div>\r\n");
      out.write("                    <footer class=\"sidebar-footer\">\r\n");
      out.write("                        <ul class=\"sidebar-menu metismenu\" id=\"customize-menu\">\r\n");
      out.write("                            <li>\r\n");
      out.write("                                <ul>\r\n");
      out.write("                                    <li class=\"customize\">\r\n");
      out.write("                                        \r\n");
      out.write("                                        <div class=\"customize-item\">\r\n");
      out.write("                                            \r\n");
      out.write("                                        </div>\r\n");
      out.write("                                    </li>\r\n");
      out.write("                                </ul>\r\n");
      out.write("                                Powered by: KIT\r\n");
      out.write("                            </li>\r\n");
      out.write("                        </ul>\r\n");
      out.write("                    </footer>\r\n");
      out.write("                    \r\n");
      out.write("                </aside>\r\n");
      out.write("                <div class=\"sidebar-overlay\" id=\"sidebar-overlay\"></div>\r\n");
      out.write("                <div class=\"sidebar-mobile-menu-handle\" id=\"sidebar-mobile-menu-handle\"></div>\r\n");
      out.write("                <div class=\"mobile-menu-handle\"></div>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}