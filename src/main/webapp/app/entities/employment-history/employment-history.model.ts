import dayjs from 'dayjs/esm';
import { IDesignation } from 'app/entities/designation/designation.model';
import { IDepartment } from 'app/entities/department/department.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IUnit } from 'app/entities/unit/unit.model';
import { IBand } from 'app/entities/band/band.model';
import { EventType } from 'app/entities/enumerations/event-type.model';

export interface IEmploymentHistory {
  id: number;
  referenceId?: string | null;
  pin?: string | null;
  eventType?: EventType | null;
  effectiveDate?: dayjs.Dayjs | null;
  previousMainGrossSalary?: number | null;
  currentMainGrossSalary?: number | null;
  previousWorkingHour?: string | null;
  changedWorkingHour?: string | null;
  isModifiable?: boolean | null;
  previousDesignation?: Pick<IDesignation, 'id'> | null;
  changedDesignation?: Pick<IDesignation, 'id'> | null;
  previousDepartment?: Pick<IDepartment, 'id'> | null;
  changedDepartment?: Pick<IDepartment, 'id'> | null;
  previousReportingTo?: Pick<IEmployee, 'id'> | null;
  changedReportingTo?: Pick<IEmployee, 'id'> | null;
  employee?: Pick<IEmployee, 'id'> | null;
  previousUnit?: Pick<IUnit, 'id'> | null;
  changedUnit?: Pick<IUnit, 'id'> | null;
  previousBand?: Pick<IBand, 'id'> | null;
  changedBand?: Pick<IBand, 'id'> | null;
}

export type NewEmploymentHistory = Omit<IEmploymentHistory, 'id'> & { id: null };
