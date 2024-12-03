import { IEmployee } from 'app/entities/employee/employee.model';
import { IFestival } from 'app/entities/festival/festival.model';
import dayjs from 'dayjs/esm';
import { EmployeeCategory } from '../enumerations/employee-category.model';
export interface IFestivalBonusDetails {
  id: number;
  bonusAmount?: number | null;
  remarks?: string | null;
  isHold?: boolean | null;
  basic?: number | null;
  gross?: number | null;
  employee?: Pick<IEmployee, 'id'> | null;
  festival?: Pick<IFestival, 'id'> | null;
  employeeId?: number | null;
  festivalId?: number | null;
  fullName?: string | null;
  pin?: string | null;
  designationName?: string | null;
  departmentName?: string | null;
  unitName?: string | null;
  bandName?: string | null;
  doj?: dayjs.Dayjs | null;
  doc?: dayjs.Dayjs | null;
  contractPeriodEndDate?: dayjs.Dayjs | null;
  contractPeriodExtendedTo?: dayjs.Dayjs | null;
  employeeCategory?: EmployeeCategory;
  title?: string | null;
  festivalName?: string | null;
  festivalDate?: dayjs.Dayjs | null;
  bonusDisbursementDate?: dayjs.Dayjs | null;
  yearlyFestivalOrderNo?: number | null;
  isChecked?: boolean;
}

export type NewFestivalBonusDetails = Omit<IFestivalBonusDetails, 'id'> & { id: null };
