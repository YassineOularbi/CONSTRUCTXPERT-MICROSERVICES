package com.resource_service.dto;

import com.resource_service.enums.Type;
import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ResourceDto {
    private String title;
    private String provider;
    private Date acquisitionDate;
    private String picture;
    private String quantity;
    private Boolean availability;
    private Type type;
}
