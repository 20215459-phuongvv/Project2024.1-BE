package com.hust.project3.dtos.setting;

import lombok.Data;

@Data
public class SettingRequestDTO {
    private Long id;
    private String key;
    private String value;
}
