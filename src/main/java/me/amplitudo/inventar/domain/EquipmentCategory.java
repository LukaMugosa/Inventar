package me.amplitudo.inventar.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A EquipmentCategory.
 */
@Entity
@Table(name = "equipment_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EquipmentCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "equipmentCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Equipment> equipments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public EquipmentCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public EquipmentCategory createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public EquipmentCategory updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Equipment> getEquipments() {
        return equipments;
    }

    public EquipmentCategory equipments(Set<Equipment> equipment) {
        this.equipments = equipment;
        return this;
    }

    public EquipmentCategory addEquipments(Equipment equipment) {
        this.equipments.add(equipment);
        equipment.setEquipmentCategory(this);
        return this;
    }

    public EquipmentCategory removeEquipments(Equipment equipment) {
        this.equipments.remove(equipment);
        equipment.setEquipmentCategory(null);
        return this;
    }

    public void setEquipments(Set<Equipment> equipment) {
        this.equipments = equipment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EquipmentCategory)) {
            return false;
        }
        return id != null && id.equals(((EquipmentCategory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
