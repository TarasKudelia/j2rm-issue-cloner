package com.taraskudelia.taskmanager.jira;

import com.taraskudelia.taskmanager.IssueData;
import com.taraskudelia.taskmanager.util.LogPrinter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Connects to the Jira and fetches issues.
 *
 * @author Taras Kudelia
 * @since 19/09/2019
 */
@Slf4j
public class JiraController {

    public JiraController(String cookie, String host) {
        if (StringUtils.isBlank(cookie)) {
            LogPrinter.exitWithError(log, "No cookie received for Jira controller.");
        } else if (StringUtils.isBlank(host)) {
            LogPrinter.exitWithError(log, "No host received for Jira controller.");
        }
        requestHeaders.put("Cookie", cookie);
        requestHeaders.put("Host", host);
    }

    /**
     * Set of the HTTP headers for the Jira auth process.
     */
    private Map<String, String> requestHeaders = new HashMap<>();
    {
        requestHeaders.put("Content-Type", "text/html;charset=UTF-8");
        requestHeaders.put("Accept", "text/json");
        requestHeaders.put("Cache-Control", "no-cache");
        requestHeaders.put("Accept-Encoding", "deflate");
        requestHeaders.put("Connection", "keep-alive");
        requestHeaders.put("cache-control", "no-cache");
        requestHeaders.put("User-Agent", "Mozilla/5.0");
    }

    /**
     * Fetches data from the issue page and package it into IssueData object.
     *
     * @param baseUrl  - base url to the jira resource without the issue key.
     * @param issueKey - key of the issue to process
     * @return IssueData of the given jira issue, found by the baseUrl
     * @throws IOException if the process of the getting response from the
     */
    public IssueData getIssueByKeyFrom(String baseUrl, String suffix, String issueKey) throws IOException {
        final String fullUrl = baseUrl + suffix;
        final String issueUrl = fullUrl + issueKey;
        JiraHtmlParser parser = new JiraHtmlParser(sendGetIssueRequest(fullUrl, issueKey), issueUrl, issueKey);
        IssueData issueData = parser.getIssueData();
        parser.clear();

        return issueData;
    }

    /**
     * Sends GET request to the given URL to receive HTML page.
     *
     * @param baseUrl  - base URL of the issue page.
     * @param issueKey - issue key
     * @return HTML page as String.
     * @throws IOException if the request fails or page is empty.
     */
    private String sendGetIssueRequest(String baseUrl, String issueKey) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(baseUrl + issueKey).get();
        requestHeaders.forEach(builder::addHeader);
        Request request = builder.build();
        return client.newCall(request).execute().body().string();
    }

}
