package me.amplitudo.inventar.repository;

import me.amplitudo.inventar.domain.ProblemReportCategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProblemReportCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProblemReportCategoryRepository extends JpaRepository<ProblemReportCategory, Long>, JpaSpecificationExecutor<ProblemReportCategory> {
}
