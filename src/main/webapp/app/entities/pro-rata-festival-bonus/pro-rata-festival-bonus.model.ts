import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

// export interface IProRataFestivalBonus {
//   id: number;
//   date?: dayjs.Dayjs | null;
//   amount?: number | null;
//   description?: string | null;
//   employee?: Pick<IEmployee, 'id'> | null;
// }
//
// export type NewProRataFestivalBonus = Omit<IProRataFestivalBonus, 'id'> & { id: null };

export interface IProRataFestivalBonus {
  id?: number;
  date?: dayjs.Dayjs | null;
  amount?: number;
  description?: string;
  employeeId?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
}

export class ProRataFestivalBonus implements IProRataFestivalBonus {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public amount?: number,
    public description?: string,
    public employeeId?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string
  ) {}
}
