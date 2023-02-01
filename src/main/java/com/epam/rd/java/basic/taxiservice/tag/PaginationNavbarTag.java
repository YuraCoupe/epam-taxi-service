package com.epam.rd.java.basic.taxiservice.tag;

import com.epam.rd.java.basic.taxiservice.config.LabelManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class PaginationNavbarTag extends SimpleTagSupport {
    private int page;
    private int pageCount;

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void doTag() throws IOException {
        JspWriter out = getJspContext().getOut();
        out.println("<nav aria-label=\"Page navigation\">");
        out.println("<ul class=\"pagination\">\n");
        if (page == 1) {
            out.println("<li 'class=\"page-item disabled\"'>\n");
            out.println(String.format("<span class=\"page-link\">%s</span>", LabelManager.getProperty("label.page.first")));
            out.println("</li>");
            out.println("<li class=\"page-item disabled\">");
            out.println(String.format("<span class=\"page-link\">%s</span>", LabelManager.getProperty("label.page.previous")));
            out.println("</li>\n");
        } else {
            out.println("<li 'class=\"page-item\"'>\n");
            out.println("<a class=\"page-link\" href=\"?page=1\">");
            out.println(LabelManager.getProperty("label.page.first"));
            out.println("</a>");
            out.println("</li>");
            out.println("<li class=\"page-item\">");
            out.println(String.format("<a class=\"page-link\" href=\"?page=%d\">", page - 1));
            out.println(LabelManager.getProperty("label.page.previous"));
            out.println("</a>");
            out.println("</li>");
        }
        for (int i = 1; i <= pageCount; i++) {
            if (page == i) {
                out.println("<li class=\"page-item active\" aria-current=\"page\">");
            } else {
                out.println("<li class=\"page-item\">");
            }
            out.println(String.format("<a class=\"page-link\" href=\"?page=%d\">%d</a>", i, i));
            out.println("</li>");
        }
        if (page == pageCount) {
            out.println("<li class=\"page-item disabled\">");
            out.println(String.format("<span class=\"page-link\">%s</span>", LabelManager.getProperty("label.page.next")));
            out.println("</li>\n");
            out.println("<li class=\"page-item disabled\">");
            out.println(String.format("<span class=\"page-link\">%s</span>", LabelManager.getProperty("label.page.last")));
            out.println("</li>\n");
        } else {
            out.println("<li class=\"page-item\">");
            out.println(String.format("<a class=\"page-link\" href=\"?page=%d\">", page + 1));
            out.println(LabelManager.getProperty("label.page.next"));
            out.println("</a>");
            out.println("</li>");
            out.println("<li class=\"page-item\">");
            out.println(String.format("<a class=\"page-link\" href=\"?page=%d\">", pageCount));
            out.println(LabelManager.getProperty("label.page.last"));
            out.println("</a>");
            out.println("</li>");
        }
        out.println("</ul>");
        out.println("</nav>");
    }
}
