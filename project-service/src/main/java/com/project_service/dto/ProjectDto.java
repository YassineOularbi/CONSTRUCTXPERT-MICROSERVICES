package com.project_service.dto;

import com.project_service.enums.Status;
import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ProjectDto {
    private String name;
    private String geolocation;
    private Date dateStart;
    private Date dateEnd;
    private Status status;
    private String description;
    private Integer room;
    private Integer bath;
    private Integer garage;
    private Integer terrace;
    private String wallMaterial;
    private String foundationType;
    private String roofingType;
    private Double areaSize;
    private Double budget;
    private String planFloor;
    private String picture;
}
