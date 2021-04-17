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

/**
 * Criteria class for the {@link me.amplitudo.inventar.domain.ProblemReport} entity. This class is used
 * in {@link me.amplitudo.inventar.web.rest.ProblemReportResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /problem-reports?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProblemReportCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private StringFilter createdAt;

    private StringFilter updatedAt;

    private LongFilter problemReportCategoryId;

    private LongFilter employeeId;

    public ProblemReportCriteria() {
    }

    public ProblemReportCriteria(ProblemReportCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.problemReportCategoryId = other.problemReportCategoryId == null ? null : other.problemReportCategoryId.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
    }

    @Override
    public ProblemReportCriteria copy() {
        return new ProblemReportCriteria(this);
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

    public StringFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(StringFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(StringFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getProblemReportCategoryId() {
        return problemReportCategoryId;
    }

    public void setProblemReportCategoryId(LongFilter problemReportCategoryId) {
        this.problemReportCategoryId = problemReportCategoryId;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProblemReportCriteria that = (ProblemReportCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(problemReportCategoryId, that.problemReportCategoryId) &&
            Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        createdAt,
        updatedAt,
        problemReportCategoryId,
        employeeId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProblemReportCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (problemReportCategoryId != null ? "problemReportCategoryId=" + problemReportCategoryId + ", " : "") +
                (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            "}";
    }

}
