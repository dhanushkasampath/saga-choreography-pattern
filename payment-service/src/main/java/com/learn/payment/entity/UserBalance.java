package com.learn.payment.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_BALANCE_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBalance {

    @Id
    private Integer userId;
    private Integer price;
}
