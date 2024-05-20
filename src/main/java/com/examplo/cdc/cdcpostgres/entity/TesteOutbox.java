package com.examplo.cdc.cdcpostgres.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "teste_outbox")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TesteOutbox {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String traceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof TesteOutbox other))
            return false;

        return id != null &&
            id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
