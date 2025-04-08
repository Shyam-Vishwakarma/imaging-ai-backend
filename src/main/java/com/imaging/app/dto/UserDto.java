package com.imaging.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private String userId;
    private String email;
    private String name;
    private String picture;
    private String authProvider;
}
