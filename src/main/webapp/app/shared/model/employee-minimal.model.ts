import { Gender } from './enumerations/gender.model';
import dayjs from 'dayjs/esm';

export interface IEmployeeMinimal {
  id?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;
  bandName?: string;
  personalContactNo?: string;
  gender?: Gender;
  officialEmail?: string;
  officialContactNo?: string;
  whatsappId?: string;
  skypeId?: string;
  tinNumber?: string;
  taxCircle?: string;
  taxZone?: string;
  getByteStreamFromFilePath?: ArrayBuffer;
  dateOfBirth?: dayjs.Dayjs;
}

export class EmployeeMinimal implements IEmployeeMinimal {
  constructor(
    public id?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string,
    public bandName?: string,
    public personalContactNo?: string,
    public gender?: Gender,
    public officialEmail?: string,
    public officialContactNo?: string,
    public whatsappId?: string,
    public skypeId?: string,
    public tinNumber?: string,
    public taxCircle?: string,
    public taxZone?: string,
    public dateOfBirth?: dayjs.Dayjs
  ) {}
}
