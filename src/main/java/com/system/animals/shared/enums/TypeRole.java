package com.system.animals.shared.enums;

import lombok.Getter;

@Getter
public enum TypeRole {

    ROLE_ADMIN("Administrador", "Control total del sistema, gestión de usuarios y auditoría global."),
    ROLE_VETERINARIAN("Veterinario", "Gestión de historias clínicas, tratamientos y control de salud animal."),
    ROLE_KEEPER("Cuidador", "Registro de actividades diarias, alimentación y limpieza de hábitats."),
    ROLE_MANAGER("Gestor Operativo", "Control de logística, inventario, hábitats y presupuestos."),
    ROLE_AUDITOR("Auditor", "Permiso de solo lectura para inspecciones y cumplimiento normativo."),
    ROLE_USER("Usuario Común", "Acceso al portal público, ver sus propias mascotas y solicitar citas.");

    private final String displayName;
    private final String description;

    // Constructor del Enum
    TypeRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}
