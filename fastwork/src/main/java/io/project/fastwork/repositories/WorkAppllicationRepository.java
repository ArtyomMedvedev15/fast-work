package io.project.fastwork.repositories;

import io.project.fastwork.domains.WorkApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkAppllicationRepository extends JpaRepository<WorkApplication,Long> {
}
