export interface UserTaxAcknowledgementValidation {
  isValid?: boolean;
  validationMessage?: string;
}

export class UserTaxAcknowledgementValidation implements UserTaxAcknowledgementValidation {
  constructor(public isValid?: boolean, public validationMessage?: string) {}
}
