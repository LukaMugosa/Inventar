package me.amplitudo.inventar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Equipment.
 */
@Entity
@Table(name = "equipment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @NotNull
    @Column(name = "price_per_unit", nullable = false)
    private Double pricePerUnit;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(mappedBy = "equipment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<EquipmentRequest> equipmentRequests = new HashSet<>();

    @OneToMany(mappedBy = "equipment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<EquipmentServicing> equipmentServicings = new HashSet<>();

    @OneToMany(mappedBy = "equipment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<EquipmentEmployee> equipmentEmployees = new HashSet<>();

    @OneToMany(mappedBy = "equipment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<EquipmentImage> images = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "equipments", allowSetters = true)
    private Manufacturer manufacturer;

    @ManyToOne
    @JsonIgnoreProperties(value = "equipments", allowSetters = true)
    private EquipmentCategory equipmentCategory;

    @ManyToOne
    @JsonIgnoreProperties(value = "equipments", allowSetters = true)
    private Supplier suplier;

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

    public Equipment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Equipment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStock() {
        return stock;
    }

    public Equipment stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public Equipment pricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
        return this;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Equipment createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Equipment updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<EquipmentRequest> getEquipmentRequests() {
        return equipmentRequests;
    }

    public Equipment equipmentRequests(Set<EquipmentRequest> equipmentRequests) {
        this.equipmentRequests = equipmentRequests;
        return this;
    }

    public Equipment addEquipmentRequest(EquipmentRequest equipmentRequest) {
        this.equipmentRequests.add(equipmentRequest);
        equipmentRequest.setEquipment(this);
        return this;
    }

    public Equipment removeEquipmentRequest(EquipmentRequest equipmentRequest) {
        this.equipmentRequests.remove(equipmentRequest);
        equipmentRequest.setEquipment(null);
        return this;
    }

    public void setEquipmentRequests(Set<EquipmentRequest> equipmentRequests) {
        this.equipmentRequests = equipmentRequests;
    }

    public Set<EquipmentServicing> getEquipmentServicings() {
        return equipmentServicings;
    }

    public Equipment equipmentServicings(Set<EquipmentServicing> equipmentServicings) {
        this.equipmentServicings = equipmentServicings;
        return this;
    }

    public Equipment addEquipmentServicing(EquipmentServicing equipmentServicing) {
        this.equipmentServicings.add(equipmentServicing);
        equipmentServicing.setEquipment(this);
        return this;
    }

    public Equipment removeEquipmentServicing(EquipmentServicing equipmentServicing) {
        this.equipmentServicings.remove(equipmentServicing);
        equipmentServicing.setEquipment(null);
        return this;
    }

    public void setEquipmentServicings(Set<EquipmentServicing> equipmentServicings) {
        this.equipmentServicings = equipmentServicings;
    }

    public Set<EquipmentEmployee> getEquipmentEmployees() {
        return equipmentEmployees;
    }

    public Equipment equipmentEmployees(Set<EquipmentEmployee> equipmentEmployees) {
        this.equipmentEmployees = equipmentEmployees;
        return this;
    }

    public Equipment addEquipmentEmployees(EquipmentEmployee equipmentEmployee) {
        this.equipmentEmployees.add(equipmentEmployee);
        equipmentEmployee.setEquipment(this);
        return this;
    }

    public Equipment removeEquipmentEmployees(EquipmentEmployee equipmentEmployee) {
        this.equipmentEmployees.remove(equipmentEmployee);
        equipmentEmployee.setEquipment(null);
        return this;
    }

    public void setEquipmentEmployees(Set<EquipmentEmployee> equipmentEmployees) {
        this.equipmentEmployees = equipmentEmployees;
    }

    public Set<EquipmentImage> getImages() {
        return images;
    }

    public Equipment images(Set<EquipmentImage> equipmentImages) {
        this.images = equipmentImages;
        return this;
    }

    public Equipment addImages(EquipmentImage equipmentImage) {
        this.images.add(equipmentImage);
        equipmentImage.setEquipment(this);
        return this;
    }

    public Equipment removeImages(EquipmentImage equipmentImage) {
        this.images.remove(equipmentImage);
        equipmentImage.setEquipment(null);
        return this;
    }

    public void setImages(Set<EquipmentImage> equipmentImages) {
        this.images = equipmentImages;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public Equipment manufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public EquipmentCategory getEquipmentCategory() {
        return equipmentCategory;
    }

    public Equipment equipmentCategory(EquipmentCategory equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
        return this;
    }

    public void setEquipmentCategory(EquipmentCategory equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }

    public Supplier getSuplier() {
        return suplier;
    }

    public Equipment suplier(Supplier supplier) {
        this.suplier = supplier;
        return this;
    }

    public void setSuplier(Supplier supplier) {
        this.suplier = supplier;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equipment)) {
            return false;
        }
        return id != null && id.equals(((Equipment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Equipment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", stock=" + getStock() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
