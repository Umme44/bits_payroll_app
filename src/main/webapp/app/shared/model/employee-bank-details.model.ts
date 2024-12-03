export interface IEmployeeBankDetails {
  bankName?: string;
  bankAccountNumber?: string;
  bankBranch?: string;
}

export class EmployeeBankDetails implements IEmployeeBankDetails {
  constructor(public bankName?: string, public bankAccountNumber?: string, public bankBranch?: string) {}
}
