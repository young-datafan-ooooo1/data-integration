package com.youngdatafan.portal.system.management.summary.model;

import lombok.Data;

/**
 * ProjectStatusSum.
 */
@Data
public class ProjectStatusSum {

    private String projecType;

    private int total;

    private int online;

    private int offline;

    private int checking;

    private int refuse;

    private int cancel;

    private int canceling;

    private int wfb;

}
