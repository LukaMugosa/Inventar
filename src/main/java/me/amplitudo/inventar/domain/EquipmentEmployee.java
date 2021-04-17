package me.amplitudo.inventar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import me.amplitudo.inventar.domain.enumeration.EquipmentStatus;

/**
 * A EquipmentEmployee.
 */
@Entity
@Table(name = "equipment_employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EquipmentEmployee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_of_rent", nullable = false)
    private Instant dateOfRent;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EquipmentStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = "equipmentEmployees", allowSetters = true)
    private Equipment equipment;

    @ManyToOne
    @JsonIgnoreProperties(value = "equipmentEmployees", allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateOfRent() {
        return dateOfRent;
    }

    public EquipmentEmployee dateOfRent(Instant dateOfRent) {
        this.dateOfRent = dateOfRent;
        return this;
    }

    public void setDateOfRent(Instant dateOfRent) {
        this.dateOfRent = dateOfRent;
    }

    public Boolean isActive() {
        return active;
    }

    public EquipmentEmployee active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public EquipmentEmployee status(EquipmentStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public EquipmentEmployee equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Employee getEmployee() {
        return employee;
    }

    public EquipmentEmployee employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentEmployee)) {
            return false;
        }
        return id != null && id.equals(((EquipmentEmployee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentEmployee{" +
            "id=" + getId() +
            ", dateOfRent='" + getDateOfRent() + "'" +
            ", active='" + isActive() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
