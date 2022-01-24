package com.lperezve.ghco.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewTradesResponseDTO {

    Integer tradesProcessed;

    Integer addedTrades;

    Integer updatedTrades;

    public static NewTradesResponseDTO from(Integer processed, Integer added, Integer updated) {
        return NewTradesResponseDTO.builder()
                .tradesProcessed(processed)
                .addedTrades(added)
                .updatedTrades(updated)
                .build();
    }
}
