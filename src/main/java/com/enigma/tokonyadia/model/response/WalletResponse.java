package com.enigma.tokonyadia.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {
    private String id;
    private String phoneNumber;
    private Long balance;
}
