package io.project.fastwork.repositories;

import io.project.fastwork.domains.WorkApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkAppllicationRepository extends JpaRepository<WorkApplication,Long> {
    @Query("select wp from WorkApplication wp where wp.work.id=:work_id")
    List<WorkApplication>findByWorkId(@Param("work_id") Long work_id);

    @Query("select wp from WorkApplication wp where wp.worker.id=:worker_id")
    List<WorkApplication>findByWorkerId(@Param("worker_id") Long worker_id);

    @Query("select wp from WorkApplication wp where wp.id=:work_application_id")
    WorkApplication getWorkApplicationById(@Param("work_application_id") Long idWorkApplication);
}
