package com.taraskudelia.taskmanager.jira;

import com.taraskudelia.taskmanager.IssueData;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Provides functionality for the parsing HTML body of the Jira issue.
 *
 * @author Taras Kudelia
 * @since 19/09/2019
 */
public class JiraHtmlParser {

    /* JSoup selectors */
    private final static String TITLE_SELECTOR = "h1[data-test-id=issue.views.issue-base.foundation.summary.heading]";
    private final static String DESCRIPTION_SELECTOR = "div.ak-renderer-document";

    private String issueKey;
    private String url;
    private String title = null;
    private String description = null;

    private String responseBody;

    public JiraHtmlParser(String responseBody, String url, String issueKey) {
        this.url = url;
        this.issueKey = issueKey;
        this.responseBody = responseBody;
    }

    /**
     * @return title of the issue. May return null if operation fails.
     * @implNote may return null
     */
    public String getTitle(Document page) {
        if (title == null) {
            Elements titleElements = page.select(TITLE_SELECTOR);
            if (titleElements.size() > 0 && titleElements.first().hasText()) {
                title = titleElements.first().text();
            }
        }
        return title;
    }

    /**
     * @return description of the issue. May return null if operation fails.
     * @implNote may return null
     */
    public String getDescription(Document page) {
        if (description == null) {
            Elements descriptionElements = page.select(DESCRIPTION_SELECTOR);
            if (descriptionElements.size() > 0) {
                Element descriptionElement = descriptionElements.first();
                final Elements children = descriptionElement.children();
                if (children.size() > 0) {
                    StringBuilder builder = new StringBuilder();
                    children.forEach(child -> {
                        if (child.hasText()) {
                            builder.append(child.text()).append(System.lineSeparator());
                        }
                    });
                    final String fromBuilder = builder.toString();
                    if (!StringUtils.isBlank(fromBuilder)) {
                        description = fromBuilder;
                    }
                }
            }
        }
        return description;
    }

    /**
     * @return constructed IssueData object with the parsed data from the page
     */
    public IssueData getIssueData() {
        Document page = Jsoup.parse(responseBody);
        final String description = url + System.lineSeparator() + getDescription(page);
        return IssueData.Builder.newInstance()
                                .setUrl(url)
                                .setIssueKey(issueKey)
                                .setTitle(getTitle(page))
                                .setDescription(description)
                                .build();
    }

    /**
     * Clears all saved data in the parser
     */
    public void clear() {
        issueKey = null;
        url = null;
        title = null;
        description = null;
        responseBody = null;
    }
}
