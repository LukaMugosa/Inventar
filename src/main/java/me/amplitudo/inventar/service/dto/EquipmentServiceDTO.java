package me.amplitudo.inventar.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link me.amplitudo.inventar.domain.EquipmentService} entity.
 */
public class EquipmentServiceDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private Instant dateSent;

    @NotNull
    private Instant eta;


    private Long employeeId;

    private Long equipmentId;

    private Long repairerId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateSent() {
        return dateSent;
    }

    public void setDateSent(Instant dateSent) {
        this.dateSent = dateSent;
    }

    public Instant getEta() {
        return eta;
    }

    public void setEta(Instant eta) {
        this.eta = eta;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Long getRepairerId() {
        return repairerId;
    }

    public void setRepairerId(Long repairerId) {
        this.repairerId = repairerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentServiceDTO)) {
            return false;
        }

        return id != null && id.equals(((EquipmentServiceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentServiceDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateSent='" + getDateSent() + "'" +
            ", eta='" + getEta() + "'" +
            ", employeeId=" + getEmployeeId() +
            ", equipmentId=" + getEquipmentId() +
            ", repairerId=" + getRepairerId() +
            "}";
    }
}
