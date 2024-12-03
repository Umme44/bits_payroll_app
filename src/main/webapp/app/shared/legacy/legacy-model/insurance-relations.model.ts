export interface IInsuranceRelations {
  relations?: string[];
}

export class InsuranceRelations implements IInsuranceRelations {
  constructor(public relations?: string[]) {}
}
