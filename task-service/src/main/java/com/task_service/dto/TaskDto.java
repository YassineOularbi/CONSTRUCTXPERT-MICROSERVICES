package com.task_service.dto;

import com.task_service.enums.Priority;
import com.task_service.enums.Status;
import lombok.*;

import java.sql.Date;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TaskDto {
    private String title;
    private String type;
    private Date startDate;
    private Date endDate;
    private String description;
    private Priority priority;
    private Status status;
    private Long projectId;
    private ArrayList<Long> rIds;
}
