package io.project.fastwork.services.api;

import io.project.fastwork.domains.TypeWork;
import io.project.fastwork.domains.Work;
import io.project.fastwork.services.exception.TypeWorkNotFound;
import io.project.fastwork.services.exception.WorkAlreadyExists;
import io.project.fastwork.services.exception.WorkInvalidDataValues;
import io.project.fastwork.services.exception.WorkNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

public interface WorkServiceApi {
    Work saveWork(Work savedWork) throws WorkAlreadyExists, WorkInvalidDataValues;
    Work updateWork(Work updatedWork) throws WorkInvalidDataValues, WorkAlreadyExists;
    Work closeWork(Work closedWork) throws WorkNotFound;
    Work openWork(Work openedWork) throws WorkNotFound;
    Work getWorkById(Long work_id) throws WorkNotFound;
    Work exceptionWork(Work exceptionWork) throws WorkNotFound;
    List<Work>findWorkByName(String nameWork);
    List<Work>findWorkByTypeWork(TypeWork typeWork);
    List<Work>findAllWork();
    List<Work>findAllOpenedWork();
    List<Work>findAllClosedWork();
    List<Work>findAllExceptionWork();

}
