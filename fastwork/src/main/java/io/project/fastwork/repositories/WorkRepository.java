package io.project.fastwork.repositories;

import io.project.fastwork.domains.StatusWork;
import io.project.fastwork.domains.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work,Long> {
    @Query("update Work  w set w.workStatus=:status where w.id=:id_work")
    Work workChangeStatus(@Param("status") StatusWork statusWork,@Param("id_work")Long id_work);
    @Query("select w from Work w where w.workName like %:work_name%")
    List<Work>findWorkByName(@Param("work_name")String work_name);
    @Query("select w from Work  w where w.workType.id=:workTypeId")
    List<Work>findWorkByType(@Param("workTypeId")Long idWorkType);
    @Query("select w from Work w where w.id=:work_id")
    Work getWorkById(@Param("work_id")Long work_id);

}
