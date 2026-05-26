package com.system.animals.modules.citation.entity;

import java.time.LocalDateTime;
import java.util.Set;

import com.system.animals.modules.animals.entity.Animals;
import com.system.animals.modules.history.entity.HistoryAnimals;
import com.system.animals.modules.veterinary.entity.Veterinary;
import com.system.animals.shared.base.BaseEntity;
import com.system.animals.shared.enums.TypeState;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "citation", indexes = {
        @Index(name = "idx_citation_state", columnList = "state"),
        @Index(name = "idx_citation_time_date", columnList = "time_date")
})
public class Citation extends BaseEntity {

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeState state;

    @Column(nullable = false)
    private String note;

    @Column(name = "time_date", nullable = false)
    private LocalDateTime timeDate;

    @Column(nullable = false)
    private boolean enable = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinary_id", nullable = false)
    private Veterinary veterinary;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "animals_citation", joinColumns = @JoinColumn(name = "citation_id"), inverseJoinColumns = @JoinColumn(name = "animals_id"))
    private Set<Animals> animals;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_animals_id")
    private HistoryAnimals historyAnimals;

}
