package com.taraskudelia.taskmanager.redmine;

import com.sun.javafx.binding.StringFormatter;
import com.taraskudelia.taskmanager.IssueData;
import com.taraskudelia.taskmanager.util.DateUtil;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.ProjectManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueCategory;
import com.taskadapter.redmineapi.bean.IssueFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Connects to the Redmine for the issues publish.
 *
 * @author Taras Kudelia
 * @since 19/09/2019
 */
public class RedmineController {

    private boolean isDebug;

    private RedmineManager redmineManager;
    private IssueManager issueManager;
    private ProjectManager projectManager;

    /**
     * Uses Redmine REST API to authenticate on Redmine
     *
     * @param uri          - URI of the Redmine server
     * @param apiAccessKey - personal API access key
     */
    public void connectToRedmine(String uri, String apiAccessKey, boolean isDebug) {
        redmineManager = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
        issueManager = redmineManager.getIssueManager();
        projectManager = redmineManager.getProjectManager();
        this.isDebug = isDebug;
    }

    /**
     * Create Redmine issue and fill it with data.
     *
     * @param issueData   - data of an issue from the Jira.
     * @param projectName - String name of the redmine project
     * @return constructed redmine Issue.
     */
    public Issue createIssue(IssueData issueData, String projectName) throws RedmineException {
        // Default data
        String status = "In progress";

        Date startDate = Calendar.getInstance().getTime();
        int assigneeId = redmineManager.getUserManager().getCurrentUser().getId();
        int projectId = projectManager.getProjectByKey(projectName).getId();
        final IssueCategory category = getIssueCategory(startDate, projectId);
        final String issueKey = issueData.getIssueKey();
        final String title = isDebug ? "test" + new Random().nextInt() : issueData.getTitle();
        final String formedSubject = StringFormatter.format("%s - %s", issueKey, title).getValue();

        // Create issue
        Issue issueToCreate = IssueFactory.create(projectId, formedSubject);
        issueToCreate.setDescription(issueData.getDescription());
        issueToCreate.setStatusName(status);
        issueToCreate.setStartDate(startDate);
        issueToCreate.setAssigneeId(assigneeId);
        // Optional
        if (category != null) {
            issueToCreate.setCategory(category);
        }

        return issueToCreate;
    }

    private IssueCategory getIssueCategory(Date startDate, int projectId) throws RedmineException {
        List<IssueCategory> categories = issueManager.getCategories(projectId);
        final IssueCategory[] category = {null};
        String dateStr = startDate.toString();
        String year = dateStr.substring(dateStr.lastIndexOf("20"));
        final int monthNumber = Calendar.getInstance().get(Calendar.MONTH);
        String month = DateUtil.getMonthName(monthNumber);
        categories.forEach(cat -> {
            final String name = cat.getName();
            if (!StringUtils.isBlank(name) && name.equals(year + " " + month)) {
                category[0] = cat;
            }
        });
        return category[0];
    }

    /**
     * Tries to publish created Redmine issue.
     */
    public Issue publishIssue(Issue issueToPublish) throws RedmineException {
        return isDebug ? issueToPublish : issueManager.createIssue(issueToPublish);
    }
}
