package com.system.animals.shared.enums;

import lombok.Getter;

@Getter
public enum TypeDocument {

    CC("Cédula de Ciudadanía", "Documento de identidad principal para ciudadanos mayores de edad."),
    CE("Cédula de Extranjería", "Documento de identificación para extranjeros residentes."),
    PASSPORT("Pasaporte", "Documento de identificación internacional para viajes y extranjeros."),
    NIT("Número de Identificación Tributaria", "Identificador para empresas, organizaciones o personas jurídicas."),
    TI("Tarjeta de Identidad",
            "Documento de identidad para menores de edad (útil si manejas practicantes o pasantes)."),
    PEP("Permiso Especial de Permanencia",
            "Documento de identificación temporal para situaciones migratorias específicas.");

    private final String displayName;
    private final String description;

    TypeDocument(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
}