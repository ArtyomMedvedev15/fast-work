package io.project.fastwork.services;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Work;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkServiceApi {
    Work saveWork(Work savedWork);
    Work updateWork(Work updatedWork);
    Work closeWork(Work closedWork);
    Work exceptionWork(Work exceptionWork);
    List<Work>findWorkByName(String nameWork);
    List<Work>findWorkByTypeWork(TypeWork typeWork);

}
