package com.jayton.admissionoffice.util.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class Paginator extends SimpleTagSupport {
    private String url;
    private long currentPage;
    private long totalPagesCount;
    private long linksCount;

    @Override
    public void doTag() throws JspException, IOException {
        long countOfPagesBefore = currentPage - 1;
        long countOfPagesAfter = totalPagesCount - currentPage;

        JspWriter writer = getJspContext().getOut();
        writer.append("<ul class=\"paginator\">");
        if(countOfPagesBefore > 0) {
            appendLink(1);
        }
        if(countOfPagesBefore > 1) {
            appendLink(currentPage - 1);
        }
        writer.append("<li class=\"paginator\"><button class=\"paginatorCur\">");
        writer.append(String.valueOf(currentPage));
        writer.append("</button></li>");
        if(countOfPagesAfter > 1) {
            appendLink(currentPage + 1);
        }
        if(countOfPagesAfter > 0) {
            appendLink(totalPagesCount);
        }
        writer.append("</ul>");
    }

    public void appendLink(long pageNumber) throws IOException {
        JspWriter writer = getJspContext().getOut();
        writer.append("<li class=\"paginator\"><button class=\"paginator\" onclick=\"location.href='")
                .append(url)
                .append("&page=")
                .append(String.valueOf(pageNumber))
                .append("&count=")
                .append(String.valueOf(linksCount))
                .append("'\">")
                .append(String.valueOf(pageNumber))
                .append("</button></li>");
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalPagesCount(long totalPagesCount) {
        this.totalPagesCount = totalPagesCount;
    }

    public void setLinksCount(long linksCount) {
        this.linksCount = linksCount;
    }
}