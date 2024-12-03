export interface ITotalPf {
  totalEmployeePortion?: number;
  totalEmployerPortion?: number;
  total?: number;
}

export class TotalPf implements ITotalPf {
  constructor(public totalEmployeePortion?: number, public totalEmployerPortion?: number, public total?: number) {}
}
