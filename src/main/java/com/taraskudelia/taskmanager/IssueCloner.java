package com.taraskudelia.taskmanager;

import com.taraskudelia.taskmanager.configuration.ConfigurationModel;
import com.taraskudelia.taskmanager.jira.JiraController;
import com.taraskudelia.taskmanager.redmine.RedmineController;
import com.taraskudelia.taskmanager.util.LogPrinter;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Slf4j
public class IssueCloner {

    /**
     * YAML file with the required connection data
     */
    private static ConfigurationModel config;

    /**
     * Entry point.
     * @param args - command-line arguments. Expected arguments is :
     *                  [0] yaml file location
     *                  [1] issue_key like "OL-3730" or "ol-4040"
     */
    public static void main(String[] args) {
        validateArguments(args);

        loadYaml(args[0]);
        final String issueKey = args[1];

        // GET the ticket
        IssueData issueData = null;
        JiraController jiraController = new JiraController(config.getJira().getCookie(), config.getJira().getHost());
        try {
            issueData = jiraController.getIssueByKeyFrom(config.getJira().getUrl().getBase(),
                                                         config.getJira().getUrl().getSuffix(), issueKey);
        } catch (IOException e) {
            LogPrinter.exitWithError(log, e.getMessage());
        }

        // Connect to the Redmine
        RedmineController redmineController = new RedmineController();
        redmineController.connectToRedmine(config.getRedmine().getUrl(), config.getRedmine().getApiKey(), config.isDebug());

        // Create and publish redmine issue
        Issue createdIssue = null;
        try {
            createdIssue = redmineController.createIssue(issueData, config.getRedmine().getProjectName());
            createdIssue = redmineController.publishIssue(createdIssue);
        } catch (RedmineException re) {
            LogPrinter.exitWithError(log, re.getMessage());
        }
        log.info("Done!");
        log.info(createdIssue.toString());
    }

    /**
     * Tries to load yaml file from the given path.
     * @param yamlLocation - absolute path ti the preference *.yaml file
     */
    private static void loadYaml(String yamlLocation) {
        File yamlFile = new File(yamlLocation);
        if (!yamlFile.exists()) {
            LogPrinter.exitWithError(log, "Can not find " + yamlLocation + " settings file.");
        }

        // Load yaml file
        Yaml yaml = new Yaml();
        try (InputStream in = Files.newInputStream(yamlFile.toPath()) ) {
            config = yaml.loadAs(in, ConfigurationModel.class);
        } catch (IOException e) {
            LogPrinter.exitWithError(log, "Can not parse Yaml file.");
        }
    }

    /**
     * Goes through command-line arguments and validates their structure. If error was detected - prints readable
     * message and exit the application.
     *
     * @param args - command-line arguments.
     */
    private static void validateArguments(String[] args) {
        // Wrong number of args
        if (args.length != 2) {
            for (String arg : args) {
                log.info(arg);
            }
            LogPrinter.exitWithError(log, "Wrong number of arguments.");
        }

        int parsedKey = -1;
        final String yamlFileLocation = args[0];
        final String issueKey = args[1];

        // load yaml
        File yamlFile = new File(yamlFileLocation);
        if (!yamlFile.exists()) {
            LogPrinter.exitWithError(log, "Can not find " + yamlFileLocation + " settings file.");
        }
        try {
            final String number = issueKey.substring(issueKey.lastIndexOf("-") + 1);
            parsedKey = Integer.parseInt(number);
        } catch (NumberFormatException ignore) {
            LogPrinter.exitWithError(log, "First argument is in a wrong format.");
        }
        if (parsedKey <= 0) {
            LogPrinter.exitWithError(log, "Only positive issue_key allowed.");
        }
    }

}
