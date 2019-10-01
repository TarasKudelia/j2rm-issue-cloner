# J2RM-issue-cloner

### What?
Small tool for the duplicating Jira issues to the Redmine server. 

##### Supported issue fields:
- start date
- title
- description
- category
- assignee

### Why?
I'm not taking pleasure by doing same stuff every day by hand :smirk:


### How?
To use it all you need to do is:
- compile with Maven by `clean compile assembly:single` command
- run by `java - jar j2rm-issue-cloner.jar path/to/settings/file kira_issue_key`.

Sample `example.yaml` file with the structure you can find inside `src/main/resources`.
 
 --
 
 ###TODO:
 - add support for the `priority` field
 - add support for the `estimated time` field
 - add support for the `due date` field
 - add support for the `status` field
 - add fetching attachments