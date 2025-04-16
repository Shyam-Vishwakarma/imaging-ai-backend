package com.imaging.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDto {
    private String userId;
    private String email;
    private String name;
    private String picture;
}
