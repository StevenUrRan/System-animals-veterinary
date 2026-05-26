package com.system.animals.modules.animals.entity;

import com.system.animals.shared.base.BaseEntity;
import com.system.animals.shared.enums.AnimalGender;
import com.system.animals.shared.enums.TypeAnimals;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "animals", indexes = {
        @Index(name = "idx_animals_nit", columnList = "nit"),
        @Index(name = "idx_animals_name", columnList = "name"),
        @Index(name = "idx_animals_age", columnList = "age")
})
public class Animals extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalGender gender;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeAnimals type;

    @Column(nullable = false)
    private Long nit;

    @Column(name = "other_type_animals")
    private String otherTypeAnimals;

}
