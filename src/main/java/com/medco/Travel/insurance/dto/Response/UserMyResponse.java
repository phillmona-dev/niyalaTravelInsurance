package com.medco.Travel.insurance.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserMyResponse {

    private long totalPages;
    private long totalElements;
    private int pageNumber;
    private List<UserResponse> content;

}