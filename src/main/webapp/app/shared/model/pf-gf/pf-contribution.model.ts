export interface IPfContribution {
  year?: number;
  ownContribution?: number;
  companyContribution?: number;
}

export class PfContribution implements IPfContribution {
  constructor(public year?: number, public ownContribution?: number, public companyContribution?: number) {}
}
