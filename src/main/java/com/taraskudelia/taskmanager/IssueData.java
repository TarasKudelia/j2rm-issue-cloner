package com.taraskudelia.taskmanager;

import lombok.Getter;

/**
 * Holds the data from the Jira issue.
 *
 * @author Taras Kudelia
 * @since 19/09/2019
 */
public class IssueData {

    @Getter private String url;
    @Getter private String title;
    @Getter private String issueKey;
    @Getter private String description;

    private IssueData(Builder builder) {
        this.url = builder.url;
        this.title = builder.title;
        this.issueKey = builder.issueKey;
        this.description = builder.description;
    }

    public static class Builder {

        private String url;
        private String title;
        private String issueKey;
        private String description;

        // Constructors
        public static Builder newInstance() {
            return new Builder();
        }
        private Builder() {}

        // Setters
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public Builder setIssueKey(String issueKey) {
            this.issueKey = issueKey;
            return this;
        }
        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        // Build
        public IssueData build() {
            return new IssueData(this);
        }
    }

    @Override
    public String toString() {
        String lineSeparator = System.lineSeparator();
        return lineSeparator + lineSeparator + "Url:         " + url +
               lineSeparator + lineSeparator + "Title:       " + title +
               lineSeparator + lineSeparator + "Issue key:   " + issueKey +
               lineSeparator + lineSeparator + "Description: " + description;
    }
}
