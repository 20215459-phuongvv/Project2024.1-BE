package com.hust.project3.services.impl;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.setting.SettingRequestDTO;
import com.hust.project3.entities.Setting;
import com.hust.project3.entities.User;
import com.hust.project3.enums.RoleEnum;
import com.hust.project3.enums.SettingKeyEnum;
import com.hust.project3.exceptions.BadRequestException;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.repositories.SettingRepository;
import com.hust.project3.repositories.UserRepository;
import com.hust.project3.security.JwtTokenProvider;
import com.hust.project3.services.SettingService;
import com.hust.project3.specification.SettingSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    @Override
    public Page<Setting> getSettingsByProperties(SettingRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Pageable pageable = PageRequest.of(pagingRequestDTO.getPage(), pagingRequestDTO.getSize(), Sort.by("id").descending());
        Specification<Setting> spec = SettingSpecification.byCriteria(dto);
        return settingRepository.findAll(spec, pageable);
    }

    @Override
    public Setting getSettingById(Long id) throws NotFoundException {
        return settingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Setting not found"));
    }

    @Override
    public Setting addNormalUserLimit(String jwt, SettingRequestDTO dto) throws BadRequestException {
        if (settingRepository.existsByKey(SettingKeyEnum.NORMAL_USER_LIMIT.name())) {
            throw new BadRequestException("Normal User Limit existed");
        }
        Setting setting = Setting.builder()
                .key(SettingKeyEnum.NORMAL_USER_LIMIT.name())
                .value(dto.getValue())
                .updatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt))
                .build();
        return settingRepository.save(setting);
    }

    @Override
    public Setting addVipUserLimit(String jwt, SettingRequestDTO dto) throws BadRequestException {
        if (settingRepository.existsByKey(SettingKeyEnum.VIP_USER_LIMIT.name())) {
            throw new BadRequestException("Vip User Limit existed");
        }
        Setting setting = Setting.builder()
                .key(SettingKeyEnum.VIP_USER_LIMIT.name())
                .value(dto.getValue())
                .updatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt))
                .build();
        return settingRepository.save(setting);
    }

    @Override
    public Setting updateNormalUserLimit(String jwt, SettingRequestDTO dto) throws NotFoundException {
        Optional<Setting> settingOptional = settingRepository.findByKey(SettingKeyEnum.NORMAL_USER_LIMIT.name());
        if (settingOptional.isEmpty()) {
            throw new NotFoundException("Normal User Limit not found");
        }
        Setting setting = settingOptional.get();
        setting.setValue(dto.getValue());
        setting.setUpdatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt));
        return settingRepository.save(setting);
    }

    @Override
    public Setting updateVipUserLimit(String jwt, SettingRequestDTO dto) throws NotFoundException {
        Optional<Setting> settingOptional = settingRepository.findByKey(SettingKeyEnum.VIP_USER_LIMIT.name());
        if (settingOptional.isEmpty()) {
            throw new NotFoundException("Vip User Limit not found");
        }
        Setting setting = settingOptional.get();
        setting.setValue(dto.getValue());
        setting.setUpdatedBy(jwtTokenProvider.getEmailFromJwtToken(jwt));
        return settingRepository.save(setting);
    }

    @Override
    public Setting getUserSettings(String jwt) throws NotFoundException {
        User user = userRepository.findByEmail(jwtTokenProvider.getEmailFromJwtToken(jwt))
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (Objects.equals(user.getRole(), RoleEnum.USER.name())) {
            return settingRepository.findByKey(SettingKeyEnum.NORMAL_USER_LIMIT.name())
                    .orElseThrow(() -> new NotFoundException("Setting not found"));
        } else if (Objects.equals(user.getRole(), RoleEnum.VIP_USER.name())) {
            return settingRepository.findByKey(SettingKeyEnum.VIP_USER_LIMIT.name())
                    .orElseThrow(() -> new NotFoundException("Setting not found"));
        }
        return null;
    }
}
