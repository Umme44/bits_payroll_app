package com.bits.hr.service;

import com.bits.hr.service.config.GetConfigValueByKeyService;
import com.bits.hr.service.dto.AllowanceNameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllowanceNameService {

    @Autowired
    private GetConfigValueByKeyService getConfigValueByKeyService;

    public AllowanceNameDTO getAllowanceName() {
        AllowanceNameDTO allowanceNameDTO = new AllowanceNameDTO();
        allowanceNameDTO.setAllowance01Name(getConfigValueByKeyService.getAllowance01Name());
        allowanceNameDTO.setAllowance02Name(getConfigValueByKeyService.getAllowance02Name());
        allowanceNameDTO.setAllowance03Name(getConfigValueByKeyService.getAllowance03Name());
        allowanceNameDTO.setAllowance04Name(getConfigValueByKeyService.getAllowance04Name());
        allowanceNameDTO.setAllowance05Name(getConfigValueByKeyService.getAllowance05Name());
        allowanceNameDTO.setAllowance06Name(getConfigValueByKeyService.getAllowance06Name());

        return allowanceNameDTO;
    }
}
