import dayjs from 'dayjs/esm';

export interface IIndividualArrearSalary {
  id: number;
  effectiveDate?: dayjs.Dayjs | null;
  existingBand?: string | null;
  newBand?: string | null;
  existingGross?: number | null;
  newGross?: number | null;
  increment?: number | null;
  arrearSalary?: number | null;
  arrearPfDeduction?: number | null;
  taxDeduction?: number | null;
  netPay?: number | null;
  pfContribution?: number | null;
  title?: string | null;
  titleEffectiveFrom?: string | null;
  arrearRemarks?: string | null;
  festivalBonus?: number | null;
  employeeId?: number;

  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;

  // employee?: Pick<IEmployee, 'id'> | null;
}

export type NewIndividualArrearSalary = Omit<IIndividualArrearSalary, 'id'> & { id: null };
