package com.imaging.app.model;

import com.imaging.app.enums.ImageStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private String userId;
    @Setter private String imageUrl;
    @Setter private String generatedImageUrl;
    private String styleId;
    @Setter private ImageStatus status;

    private LocalDateTime createdAt;
    @Setter private LocalDateTime updatedAt;
}
