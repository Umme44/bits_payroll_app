import dayjs from 'dayjs/esm';
import {EventType} from 'app/shared/model/enumerations/event-type.model';
import {EmployeeCategory} from 'app/shared/model/enumerations/employee-category.model';

export interface IEmploymentHistory {
  id?: number;
  referenceId?: string;
  pin?: string;
  eventType?: EventType;
  effectiveDate?: dayjs.Dayjs;
  previousMainGrossSalary?: number;
  currentMainGrossSalary?: number;
  previousWorkingHour?: string;
  changedWorkingHour?: string;
  isModifiable?: boolean;
  previousDesignationId?: number;
  changedDesignationId?: number;
  previousDepartmentId?: number;
  changedDepartmentId?: number;
  previousReportingToId?: number;
  changedReportingToId?: number;

  employeeId?: number;
  fullName?: string;
  employeeCategory?: EmployeeCategory;
  currentDesignationName?: string;
  currentDepartmentName?: string;
  currentBandName?: string;
  currentUnitName?: string;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
  location?: string;
  currentReportingToName?: string;
  currentReportingToPIN?: string;

  previousUnitId?: number;
  changedUnitId?: number;
  previousBandId?: number;
  changedBandId?: number;

  previousDesignationName?: string;
  changedDesignationName?: string;
  previousDepartmentName?: string;
  changedDepartmentName?: string;
  previousReportingToName?: string;
  changedReportingToName?: string;
  employeeName?: string;
  previousUnitName?: string;
  changedUnitName?: string;
  previousBandName?: string;
  changedBandName?: string;
}

export class EmploymentHistory implements IEmploymentHistory {
  constructor(
    public id?: number,
    public referenceId?: string,
    public pin?: string,
    public eventType?: EventType,
    public effectiveDate?: dayjs.Dayjs,
    public previousMainGrossSalary?: number,
    public currentMainGrossSalary?: number,
    public previousWorkingHour?: string,
    public changedWorkingHour?: string,
    public isModifiable?: boolean,
    public previousDesignationId?: number,
    public changedDesignationId?: number,
    public previousDepartmentId?: number,
    public changedDepartmentId?: number,
    public previousReportingToId?: number,
    public changedReportingToId?: number,

    public employeeId?: number,
    public fullName?: string,
    public employeeCategory?: EmployeeCategory,
    public currentDesignationName?: string,
    public currentDepartmentName?: string,
    public currentBandName?: string,
    public currentUnitName?: string,
    public dateOfJoining?: dayjs.Dayjs,
    public dateOfConfirmation?: dayjs.Dayjs,
    public location?: string,
    public currentReportingToName?: string,
    public currentReportingToPIN?: string,
    public previousUnitId?: number,
    public changedUnitId?: number,
    public previousBandId?: number,
    public changedBandId?: number,
    public previousDesignationName?: string,
    public changedDesignationName?: string,
    public previousDepartmentName?: string,
    public changedDepartmentName?: string,
    public previousReportingToName?: string,
    public changedReportingToName?: string,
    public employeeName?: string,
    public previousUnitName?: string,
    public changedUnitName?: string,
    public previousBandName?: string,
    public changedBandName?: string
  ) {
    this.isModifiable = this.isModifiable || false;
  }
}
