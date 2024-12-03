import { Injectable } from '@angular/core';

const onlyNumberRegex = new RegExp('^[0-9]+$', 'i');

export class IIdentityNumberValidation {
  isValid?: boolean;
  validationMsg?: string;
}

@Injectable({ providedIn: 'root' })
export class IdentityNumberValidationService {
  // service for validate nid, passport and birth registration

  // nid
  isValidNID(idNumber: string): IIdentityNumberValidation {
    const nidNumber = idNumber.trim();

    const validation: IIdentityNumberValidation = {
      isValid: true,
      validationMsg: '',
    };

    const nidLength = nidNumber.length;

    if (!onlyNumberRegex.test(nidNumber)) {
      validation.isValid = false;
      validation.validationMsg = 'Only Number is allowed';
      return validation;
    }

    if (nidLength === 10 || nidLength === 13 || nidLength === 17) {
      validation.isValid = true;
      validation.validationMsg = '';
    } else {
      validation.isValid = false;
      validation.validationMsg = 'NID format is invalid (allowed 10, 13 or 17 characters long)';
    }

    return validation;
  }

  // brn
  isValidBRN(idNumber: string): IIdentityNumberValidation {
    const brnNumber = idNumber.trim();

    const validation: IIdentityNumberValidation = {
      isValid: true,
      validationMsg: '',
    };

    const brnLength = brnNumber.length;

    if (!onlyNumberRegex.test(brnNumber)) {
      validation.isValid = false;
      validation.validationMsg = 'Only Number is allowed';
      return validation;
    }

    if (brnLength === 17) {
      validation.isValid = true;
      validation.validationMsg = '';
    } else {
      validation.isValid = false;
      validation.validationMsg = 'Birth Registration number format can 17 characters long';
    }

    return validation;
  }

  // passport
  isValidPassport(idNumber: string): IIdentityNumberValidation {
    const passportNumber = idNumber.trim();

    const validation: IIdentityNumberValidation = {
      isValid: true,
      validationMsg: '',
    };

    const passportNumberLength = passportNumber.length;

    // if (!onlyNumberRegex.test(passportNumber)) {
    //   validation.isValid = false;
    //   validation.validationMsg = 'Only Number is allowed';
    //   return validation;
    // }

    if (passportNumberLength === 13 || passportNumberLength === 17) {
      validation.isValid = true;
      validation.validationMsg = '';
    } else {
      validation.isValid = false;
      validation.validationMsg = 'Bangladeshi Passport number can 13 or 17 characters long';
    }

    return validation;
  }

  // other
  otherIDValidation(idNumber: string): IIdentityNumberValidation {
    const otherNumber = idNumber.trim();

    const validation: IIdentityNumberValidation = {
      isValid: true,
      validationMsg: '',
    };

    const numberLength = otherNumber.length;

    // if (!onlyNumberRegex.test(otherNumber)) {
    //   validation.isValid = false;
    //   validation.validationMsg = 'Only Number is allowed';
    //   return validation;
    // }

    if (numberLength < 9) {
      validation.isValid = false;
      validation.validationMsg = 'This field is required to be at least 9 digits.';
      return validation;
    } else if (numberLength > 30) {
      validation.isValid = false;
      validation.validationMsg = 'This field cannot be longer than 30 digits.';
      return validation;
    } else {
      validation.isValid = true;
      validation.validationMsg = '';
    }

    return validation;
  }
}
