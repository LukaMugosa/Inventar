package me.amplitudo.inventar.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link me.amplitudo.inventar.domain.EquipmentServicing} entity. This class is used
 * in {@link me.amplitudo.inventar.web.rest.EquipmentServicingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /equipment-servicings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EquipmentServicingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private InstantFilter dateSent;

    private InstantFilter eta;

    private LongFilter employeeId;

    private LongFilter equipmentId;

    private LongFilter repairerId;

    public EquipmentServicingCriteria() {
    }

    public EquipmentServicingCriteria(EquipmentServicingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.dateSent = other.dateSent == null ? null : other.dateSent.copy();
        this.eta = other.eta == null ? null : other.eta.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
        this.equipmentId = other.equipmentId == null ? null : other.equipmentId.copy();
        this.repairerId = other.repairerId == null ? null : other.repairerId.copy();
    }

    @Override
    public EquipmentServicingCriteria copy() {
        return new EquipmentServicingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getDateSent() {
        return dateSent;
    }

    public void setDateSent(InstantFilter dateSent) {
        this.dateSent = dateSent;
    }

    public InstantFilter getEta() {
        return eta;
    }

    public void setEta(InstantFilter eta) {
        this.eta = eta;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public LongFilter getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(LongFilter equipmentId) {
        this.equipmentId = equipmentId;
    }

    public LongFilter getRepairerId() {
        return repairerId;
    }

    public void setRepairerId(LongFilter repairerId) {
        this.repairerId = repairerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EquipmentServicingCriteria that = (EquipmentServicingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(dateSent, that.dateSent) &&
            Objects.equals(eta, that.eta) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(equipmentId, that.equipmentId) &&
            Objects.equals(repairerId, that.repairerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        dateSent,
        eta,
        employeeId,
        equipmentId,
        repairerId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EquipmentServicingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (dateSent != null ? "dateSent=" + dateSent + ", " : "") +
                (eta != null ? "eta=" + eta + ", " : "") +
                (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
                (equipmentId != null ? "equipmentId=" + equipmentId + ", " : "") +
                (repairerId != null ? "repairerId=" + repairerId + ", " : "") +
            "}";
    }

}
