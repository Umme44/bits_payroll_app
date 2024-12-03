package com.bits.hr.service;

import org.springframework.web.multipart.MultipartFile;

public interface BillableAugmentedIntegrationService {
    boolean importBillableAugmentedIntegrationXl(MultipartFile file) throws Exception;
}
