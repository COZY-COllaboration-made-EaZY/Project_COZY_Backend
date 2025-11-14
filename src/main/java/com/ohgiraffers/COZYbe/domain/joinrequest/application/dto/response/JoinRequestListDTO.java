package com.ohgiraffers.COZYbe.domain.joinrequest.application.dto.response;

import java.util.List;

public record JoinRequestListDTO(
        List<JoinRequestDTO> requests
) {
}
