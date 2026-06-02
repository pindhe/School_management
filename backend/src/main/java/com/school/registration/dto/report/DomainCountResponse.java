package com.school.registration.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomainCountResponse {

    private String domain;
    private long count;
}
