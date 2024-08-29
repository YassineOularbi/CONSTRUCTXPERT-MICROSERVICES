package com.project_service.model;

import com.project_service.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "geolocation", nullable = false)
    private String geolocation;

    @Column(name = "date_start", nullable = false)
    private Date dateStart;

    @Column(name = "date_end", nullable = false)
    private Date dateEnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "room", nullable = false)
    private Integer room;

    @Column(name = "bath", nullable = false)
    private Integer bath;

    @Column(name = "garage", nullable = false)
    private Integer garage;

    @Column(name = "terrace", nullable = false)
    private Integer terrace;

    @Column(name = "wall_material", nullable = false)
    private String wallMaterial;

    @Column(name = "foundation_type", nullable = false)
    private String foundationType;

    @Column(name = "roofing_type", nullable = false)
    private String roofingType;

    @Column(name = "area_size", nullable = false)
    private Double areaSize;

    @Column(name = "budget", nullable = false)
    private Double budget;

    @Column(name = "plan_floor", nullable = false)
    private String planFloor;

    @Column(name = "picture", nullable = false)
    private String picture;
}
