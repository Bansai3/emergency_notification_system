package com.vfedotov.core.dto;

public record FileDto(
        byte[] file,
        String name
) {}
