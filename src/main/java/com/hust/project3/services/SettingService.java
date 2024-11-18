package com.hust.project3.services;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.setting.SettingRequestDTO;
import com.hust.project3.entities.Setting;
import com.hust.project3.exceptions.BadRequestException;
import com.hust.project3.exceptions.NotFoundException;
import org.springframework.data.domain.Page;

public interface SettingService {
    Page<Setting> getSettingsByProperties(SettingRequestDTO dto, PagingRequestDTO pagingRequestDTO);

    Setting getSettingById(Long id) throws NotFoundException;

    Setting addNormalUserLimit(String jwt, SettingRequestDTO dto) throws BadRequestException;

    Setting addVipUserLimit(String jwt, SettingRequestDTO dto) throws BadRequestException;

    Setting updateNormalUserLimit(String jwt, SettingRequestDTO dto) throws NotFoundException;

    Setting updateVipUserLimit(String jwt, SettingRequestDTO dto) throws NotFoundException;
}
