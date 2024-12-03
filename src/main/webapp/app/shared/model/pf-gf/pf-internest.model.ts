export interface IPfInterest {
  ownInterest?: number;
  companyInterest?: number;
}

export class PfInterest {
  constructor(public ownInterest?: number, public companyInterest?: number) {}
}
