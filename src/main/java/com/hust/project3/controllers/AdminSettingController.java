package com.hust.project3.controllers;

import com.hust.project3.dtos.PagingRequestDTO;
import com.hust.project3.dtos.Result;
import com.hust.project3.dtos.ResultMeta;
import com.hust.project3.dtos.setting.SettingRequestDTO;
import com.hust.project3.entities.Setting;
import com.hust.project3.exceptions.BadRequestException;
import com.hust.project3.exceptions.NotFoundException;
import com.hust.project3.services.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/settings")
public class AdminSettingController {
    private final SettingService settingService;
    @GetMapping
    public Result getSettingsByProperties(SettingRequestDTO dto, PagingRequestDTO pagingRequestDTO) {
        Page<Setting> page = settingService.getSettingsByProperties(dto, pagingRequestDTO);
        return Result.ok(page.getContent(), ResultMeta.of(page));
    }

    @GetMapping("/{id}")
    public Result getSettingById(@PathVariable("id") Long id) throws NotFoundException {
        return Result.ok(settingService.getSettingById(id));
    }

    @PostMapping("/normal-user-limit")
    public Result addNormalUserLimit(@RequestHeader("Authorization") String jwt,
                                     @RequestBody SettingRequestDTO dto) throws BadRequestException {
        return Result.ok(settingService.addNormalUserLimit(jwt, dto));
    }

    @PostMapping("/vip-user-limit")
    public Result addVipUserLimit(@RequestHeader("Authorization") String jwt,
                                  @RequestBody SettingRequestDTO dto) throws BadRequestException {
        return Result.ok(settingService.addVipUserLimit(jwt, dto));
    }

    @PutMapping("/normal-user-limit")
    public Result updateNormalUserLimit(@RequestHeader("Authorization") String jwt,
                                        @RequestBody SettingRequestDTO dto) throws NotFoundException {
        return Result.ok(settingService.updateNormalUserLimit(jwt, dto));
    }

    @PutMapping("/vip-user-limit")
    public Result updateVipUserLimit(@RequestHeader("Authorization") String jwt,
                                        @RequestBody SettingRequestDTO dto) throws NotFoundException {
        return Result.ok(settingService.updateVipUserLimit(jwt, dto));
    }
}
