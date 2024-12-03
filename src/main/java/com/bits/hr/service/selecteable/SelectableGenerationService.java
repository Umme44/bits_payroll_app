package com.bits.hr.service.selecteable;

import com.bits.hr.domain.Band;
import com.bits.hr.domain.EmployeeSalary;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * A Service for list of Object to SelectableDTO conversion
 */

@Service
public class SelectableGenerationService {

    public List<SelectableDTO> getListOfDictionaryDsDTO(List<Object> entityList) {
        List<SelectableDTO> selectableDTOList = new ArrayList<>();
        try {
            if (entityList.size() == 0) {
                return selectableDTOList;
            }

            if (entityList.get(0) instanceof Band) {
                selectableDTOList = getBandList(entityList);
            }
            return selectableDTOList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return selectableDTOList;
        }
    }

    private List<SelectableDTO> getBandList(List<Object> entityList) {
        List<SelectableDTO> selectableDTOList = new ArrayList<>();
        entityList.forEach(band -> {
            SelectableDTO selectableDTO = new SelectableDTO(((Band) band).getId(), ((Band) band).getBandName());
            selectableDTOList.add(selectableDTO);
        });

        return selectableDTOList;
    }
}
