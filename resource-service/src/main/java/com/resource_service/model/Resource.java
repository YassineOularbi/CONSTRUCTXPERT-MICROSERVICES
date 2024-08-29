package com.resource_service.model;

import com.resource_service.enums.Type;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Table(name = "resource")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "acquisition_date", nullable = false)
    private Date acquisitionDate;

    @Column(name = "picture", nullable = false)
    private String picture;

    @Column(name = "quantity", nullable = false)
    private String quantity;

    @Column(name = "availability", nullable = false)
    private Boolean availability;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;
}
