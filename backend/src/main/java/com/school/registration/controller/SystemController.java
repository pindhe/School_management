package com.school.registration.controller;

import com.school.registration.dto.system.SystemInfoResponse;
import com.school.registration.service.SystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
public class SystemController {

    private final SystemService systemService;

    @GetMapping("/info")
    public SystemInfoResponse info() {
        return systemService.getInfo();
    }
}
