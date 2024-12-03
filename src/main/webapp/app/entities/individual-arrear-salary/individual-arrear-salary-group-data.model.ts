import dayjs from 'dayjs/esm';

export interface IIndividualArrearSalaryGroupData {
  effectiveDate?: dayjs.Dayjs;
  title?: string;
  effectiveFrom?: string;
}

export class IndividualArrearSalaryGroupData implements IIndividualArrearSalaryGroupData {
  constructor(public effectiveDate?: dayjs.Dayjs, public title?: string, public effectiveFrom?: string) {}
}
