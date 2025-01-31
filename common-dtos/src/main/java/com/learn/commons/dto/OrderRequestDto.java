package com.learn.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private Integer userId;
    private Integer productId;
    private Integer amount;//amount of above product
    private Integer orderId;//would be auto generated by the system
}
