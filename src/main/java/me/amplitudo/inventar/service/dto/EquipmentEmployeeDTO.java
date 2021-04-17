package me.amplitudo.inventar.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import me.amplitudo.inventar.domain.enumeration.EquipmentStatus;

/**
 * A DTO for the {@link me.amplitudo.inventar.domain.EquipmentEmployee} entity.
 */
public class EquipmentEmployeeDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Instant dateOfRent;

    @NotNull
    private Boolean active;

    @NotNull
    private EquipmentStatus status;


    private Long equipmentId;

    private Long employeeId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateOfRent() {
        return dateOfRent;
    }

    public void setDateOfRent(Instant dateOfRent) {
        this.dateOfRent = dateOfRent;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentEmployeeDTO)) {
            return false;
        }

        return id != null && id.equals(((EquipmentEmployeeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentEmployeeDTO{" +
            "id=" + getId() +
            ", dateOfRent='" + getDateOfRent() + "'" +
            ", active='" + isActive() + "'" +
            ", status='" + getStatus() + "'" +
            ", equipmentId=" + getEquipmentId() +
            ", employeeId=" + getEmployeeId() +
            "}";
    }
}
