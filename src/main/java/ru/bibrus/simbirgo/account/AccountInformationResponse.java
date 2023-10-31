package ru.bibrus.simbirgo.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountInformationResponse {

    private long id;

    private String username;

    private Role role;

    private double balance;

}