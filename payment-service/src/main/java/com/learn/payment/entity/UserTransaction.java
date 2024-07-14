package com.learn.payment.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_TRANSACTION_TBL")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTransaction {

    @Id
    private Integer orderId;
    private Integer userId;
    private Integer amount;
}
