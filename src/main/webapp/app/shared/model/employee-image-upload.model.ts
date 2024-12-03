import dayjs from 'dayjs/esm';

export interface IEmployeeImageUpload {
  id?: number;
  filePath?: string;
  employeeId?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  getByteStreamFromFilePath?: ArrayBuffer;
  dateOfJoining?: dayjs.Dayjs;
}

export class EmployeeImageUpload implements IEmployeeImageUpload {
  constructor(
    public id?: number,
    public filePath?: string,
    public employeeId?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public dateOfJoining?: dayjs.Dayjs
  ) {}
}
