export interface ISalaryDeduction {
  id: number;
  deductionAmount?: number | null;
  deductionYear?: number | null;
  deductionMonth?: number | null;
  deductionTypeId?: number | null;
  deductionTypeName?: string | null;

  employeeId?: number | null;
  fullName?: string | null;
  pin?: string | null;
  designationName?: string | null;
  departmentName?: string | null;
  unitName?: string | null;
}

export type NewSalaryDeduction = Omit<ISalaryDeduction, 'id'> & { id: null };

export class SalaryDeduction implements ISalaryDeduction {
  constructor(
    public id: number,
    public deductionAmount?: number | null,
    public deductionYear?: number | null,
    public deductionMonth?: number | null,
    public deductionTypeId?: number | null,
    public deductionTypeName?: string | null,

    public employeeId?: number | null,
    public fullName?: string | null,
    public pin?: string | null,
    public designationName?: string | null,
    public departmentName?: string | null,
    public unitName?: string | null
  ) {}
}
