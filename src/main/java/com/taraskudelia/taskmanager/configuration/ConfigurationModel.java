package com.taraskudelia.taskmanager.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Taras Kudelia
 * @since 26.09.19
 */
@Getter
@Setter
@NoArgsConstructor
public class ConfigurationModel {
    boolean debug;
    Jira jira;
    Redmine redmine;
}
