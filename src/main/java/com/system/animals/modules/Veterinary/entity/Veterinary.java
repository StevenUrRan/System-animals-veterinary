package com.system.animals.modules.Veterinary.entity;

import java.math.BigDecimal;

import com.system.animals.modules.user.entity.User;
import com.system.animals.shared.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "veterinary", indexes = {
        @Index(name = "idx_veterinary_salary", columnList = "salary"),
        @Index(name = "idx_veterinary_age", columnList = "age"),
        @Index(name = "idx_veterinary_years_of_experience", columnList = "years_of_experience")
})
@Entity
public class Veterinary extends BaseEntity {

    @Column(nullable = false)
    private Integer age;

    @Column(name = "years_of_experience", nullable = false)
    private Integer yearsOfExperience;

    @Column(nullable = false)
    private BigDecimal salary;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private boolean enable;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

}
