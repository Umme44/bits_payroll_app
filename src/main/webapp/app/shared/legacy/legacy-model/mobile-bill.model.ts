export interface IMobileBill {
  id?: number;
  month?: number;
  amount?: number;
  year?: number;
  employeeId?: number;

  fullName?: string;
  pin?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
}

export class MobileBill implements IMobileBill {
  constructor(
    public id?: number,
    public month?: number,
    public amount?: number,
    public year?: number,
    public employeeId?: number,
    public fullName?: string,
    public pin?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string
  ) {}
}
