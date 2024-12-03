import dayjs from 'dayjs/esm';
import { EmployeeCategory } from '../../model/enumerations/employee-category.model';

export interface IFestivalBonusDetails {
  id?: number;
  bonusAmount?: number;
  remarks?: string;
  isHold?: boolean;
  employeeId?: number;
  festivalId?: number;

  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  gross?: number;
  basic?: number;
  employeeCategory?: EmployeeCategory;
  doc?: dayjs.Dayjs;
  doj?: dayjs.Dayjs;
  contractPeriodEndDate?: dayjs.Dayjs;
  contractPeriodExtendedTo?: dayjs.Dayjs;
  bandName?: string;
  title?: string;
  festivalName?: string;
  festivalDate?: dayjs.Dayjs;
  bonusDisbursementDate?: dayjs.Dayjs;
  isChecked?: boolean;
  yearlyFestivalOrderNo?: number;
}

export class FestivalBonusDetails implements IFestivalBonusDetails {
  constructor(
    public id?: number,
    public bonusAmount?: number,
    public remarks?: string,
    public isHold?: boolean,
    public employeeId?: number,
    public festivalId?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public gross?: number,
    public basic?: number,
    public employeeCategory?: EmployeeCategory,
    public doc?: dayjs.Dayjs,
    public doj?: dayjs.Dayjs,
    public contractPeriodEndDate?: dayjs.Dayjs,
    public contractPeriodExtendedTo?: dayjs.Dayjs,
    public bandName?: string,
    public title?: string,
    public festivalName?: string,
    public festivalDate?: dayjs.Dayjs,
    public bonusDisbursementDate?: dayjs.Dayjs,

    public isChecked?: boolean
  ) {
    this.isHold = this.isHold || false;
  }
}
