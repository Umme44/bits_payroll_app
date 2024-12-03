package com.bits.hr.service.userPfStatement;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.service.userPfStatement.dto.UserPfStatementDTO;
import java.time.LocalDate;
import java.util.Optional;

/**
 *Service for common User Pf Statement Generate
 */
public interface UserPfStatementService {
    UserPfStatementDTO getPfStatement(PfAccount pfAccount, LocalDate date);

    Boolean checkValidityOfUserPfStatement();
}
