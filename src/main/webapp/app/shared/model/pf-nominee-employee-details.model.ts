import dayjs from 'dayjs/esm';

export interface IPfNomineeEmployeeDetailsDTO {
  id?: number;
  pin?: string;
  fullName?: string;
  presentAddress?: string;
  permanentAddress?: string;
  dateOfJoining?: dayjs.Dayjs;
  dateOfConfirmation?: dayjs.Dayjs;
  designationName?: string;
  departmentName?: string;
  bandName?: string;
  unitName?: string;
  fatherName?: string;
  motherName?: string;
  spouseName?: string;
}
