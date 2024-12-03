package com.bits.hr.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankBranchMapperTest {

    private BankBranchMapper bankBranchMapper;

    @BeforeEach
    public void setUp() {
        bankBranchMapper = new BankBranchMapperImpl();
    }
}
