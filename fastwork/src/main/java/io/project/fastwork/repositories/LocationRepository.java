package io.project.fastwork.repositories;

import io.project.fastwork.domains.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
    @Query("select l from Location l where l.locationPoints.x>=:x and l.locationPoints.y<=:y")
    List<Location>findLocationByNearby(@Param("x")double x, @Param("y")double y);
    @Query("select l from Location l where l.locationCity=:city")
    List<Location>findLocationByCity(@Param("city")String city);

    @Query("select l from Location l where l.locationWork.id=:work_id")
    Location getByWorkId(@Param("work_id")Long work_id);

}
