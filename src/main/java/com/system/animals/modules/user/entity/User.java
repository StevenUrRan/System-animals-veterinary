package com.system.animals.modules.user.entity;

import java.util.Set;

import com.system.animals.shared.base.BaseEntity;
import com.system.animals.shared.enums.TypeDocument;
import com.system.animals.shared.enums.UserGender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_nit", columnList = "nit")
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(nullable = false, length = 80)
    private String usernam;

    @Column(nullable = false, length = 80, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, unique = true)
    private Long nit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeDocument document;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @Column(nullable = false)
    private boolean enable;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> role;

}
