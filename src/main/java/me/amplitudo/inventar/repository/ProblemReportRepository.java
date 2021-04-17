package me.amplitudo.inventar.repository;

import me.amplitudo.inventar.domain.ProblemReport;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProblemReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProblemReportRepository extends JpaRepository<ProblemReport, Long>, JpaSpecificationExecutor<ProblemReport> {
}
