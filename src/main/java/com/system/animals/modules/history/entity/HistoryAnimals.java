package com.system.animals.modules.history.entity;

import java.util.Set;

import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.modules.citation.entity.Citation;
import com.system.animals.shared.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "history_animals")
@Entity
public class HistoryAnimals extends BaseEntity {

    @Column(nullable = false)
    private String description;

    @Builder.Default
    private boolean enable = true;

    @OneToOne
    @JoinColumn(name = "animals_id")
    private Animals animals;

    @OneToMany(mappedBy = "historyAnimals", fetch = FetchType.LAZY)
    private Set<Citation> citations;

}
