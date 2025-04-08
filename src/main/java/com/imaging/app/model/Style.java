package com.imaging.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Style {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long styleId;
    private String name;
    private String description;
    private String prompt;
}
