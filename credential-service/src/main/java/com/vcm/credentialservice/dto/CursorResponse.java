package com.vcm.credentialservice.dto;

import java.util.List;

public record CursorResponse<T>(
        List<T> data,
        Long nextCursor,
        int size
) {}
