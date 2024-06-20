# J2RM-issue-cloner

### What?
Small tool for the duplicating Jira issues to the Redmine server. 


### Why?
I'm not taking pleasure in doing same stuff every day by hand :smirk:


### How?
To use it all you need to do is:
- compile with Maven by `clean compile assembly:single` command
- run by `java - jar j2rm-issue-cloner.jar path/to/settings/file jira_issue_key`.

Example `.yaml` file with the requireed structure you can find inside `src/main/resources`.
