export interface IMobileBill {
  id: number;
  month?: number | null;
  amount?: number | null;
  year?: number | null;
  employeeId?: number | null;
  fullName?: string | null;
  pin?: string | null;
  designationName?: string | null;
  departmentName?: string | null;
  unitName?: string | null;
}

export type NewMobileBill = Omit<IMobileBill, 'id'> & { id: null };

export class MobileBill implements IMobileBill {
  constructor(
    public id: number,
    public month?: number | null,
    public amount?: number | null,
    public year?: number | null,
    public employeeId?: number | null,
    public fullName?: string | null,
    public pin?: string | null,
    public designationName?: string | null,
    public departmentName?: string | null,
    public unitName?: string | null
  ) {}
}
