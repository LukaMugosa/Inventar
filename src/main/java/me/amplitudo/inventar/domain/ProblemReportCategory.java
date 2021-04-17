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
 * A ProblemReportCategory.
 */
@Entity
@Table(name = "problem_report_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProblemReportCategory implements Serializable {

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

    @OneToMany(mappedBy = "problemReportCategory")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ProblemReport> problems = new HashSet<>();

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

    public ProblemReportCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public ProblemReportCategory createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public ProblemReportCategory updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<ProblemReport> getProblems() {
        return problems;
    }

    public ProblemReportCategory problems(Set<ProblemReport> problemReports) {
        this.problems = problemReports;
        return this;
    }

    public ProblemReportCategory addProblems(ProblemReport problemReport) {
        this.problems.add(problemReport);
        problemReport.setProblemReportCategory(this);
        return this;
    }

    public ProblemReportCategory removeProblems(ProblemReport problemReport) {
        this.problems.remove(problemReport);
        problemReport.setProblemReportCategory(null);
        return this;
    }

    public void setProblems(Set<ProblemReport> problemReports) {
        this.problems = problemReports;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProblemReportCategory)) {
            return false;
        }
        return id != null && id.equals(((ProblemReportCategory) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProblemReportCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
