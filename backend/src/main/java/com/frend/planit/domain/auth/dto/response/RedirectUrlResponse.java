package com.frend.planit.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RedirectUrlResponse {

    private String redirectUri;
}