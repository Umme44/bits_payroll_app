import dayjs from 'dayjs/esm';
import { IDepartment } from 'app/entities/department/department.model';
import { IDesignation } from 'app/entities/designation/designation.model';
import { IUnit } from 'app/entities/unit/unit.model';
import { IUser } from 'app/entities/user/user.model';
import { IRecruitmentRequisitionForm } from 'app/entities/recruitment-requisition-form/recruitment-requisition-form.model';
import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';
import { EmployeePinStatus } from 'app/entities/enumerations/employee-pin-status.model';

export interface IEmployeePin {
  id: number;
  pin?: string | null;
  fullName?: string | null;
  employeeCategory?: EmployeeCategory | null;
  isSupportStuff?: boolean;
  employeePinStatus?: EmployeePinStatus | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;

  departmentId?: number;
  departmentName?: string;
  designationId?: number;
  designationName?: string;
  unitId?: number;
  unitName?: string;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  recruitmentRequisitionFormId?: null | number;
  rrfNumber?: string | null;
  // department?: Pick<IDepartment, 'id'> | null;
  // designation?: Pick<IDesignation, 'id'> | null;
  // unit?: Pick<IUnit, 'id'> | null;
  // updatedBy?: Pick<IUser, 'id' | 'login'> | null;
  // createdBy?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewEmployeePin = Omit<IEmployeePin, 'id'> & { id: null };
