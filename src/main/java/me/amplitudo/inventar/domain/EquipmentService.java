package me.amplitudo.inventar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A EquipmentService.
 */
@Entity
@Table(name = "equipment_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EquipmentService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "date_sent", nullable = false)
    private Instant dateSent;

    @NotNull
    @Column(name = "eta", nullable = false)
    private Instant eta;

    @ManyToOne
    @JsonIgnoreProperties(value = "equipmentServices", allowSetters = true)
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(value = "equipmentServices", allowSetters = true)
    private Equipment equipment;

    @ManyToOne
    @JsonIgnoreProperties(value = "equipmentServices", allowSetters = true)
    private Repairer repairer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public EquipmentService title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public EquipmentService description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateSent() {
        return dateSent;
    }

    public EquipmentService dateSent(Instant dateSent) {
        this.dateSent = dateSent;
        return this;
    }

    public void setDateSent(Instant dateSent) {
        this.dateSent = dateSent;
    }

    public Instant getEta() {
        return eta;
    }

    public EquipmentService eta(Instant eta) {
        this.eta = eta;
        return this;
    }

    public void setEta(Instant eta) {
        this.eta = eta;
    }

    public Employee getEmployee() {
        return employee;
    }

    public EquipmentService employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public EquipmentService equipment(Equipment equipment) {
        this.equipment = equipment;
        return this;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Repairer getRepairer() {
        return repairer;
    }

    public EquipmentService repairer(Repairer repairer) {
        this.repairer = repairer;
        return this;
    }

    public void setRepairer(Repairer repairer) {
        this.repairer = repairer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentService)) {
            return false;
        }
        return id != null && id.equals(((EquipmentService) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentService{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateSent='" + getDateSent() + "'" +
            ", eta='" + getEta() + "'" +
            "}";
    }
}
