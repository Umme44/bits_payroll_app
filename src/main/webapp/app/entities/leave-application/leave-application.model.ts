import dayjs from 'dayjs/esm';
import { LeaveType } from 'app/entities/enumerations/leave-type.model';

export interface ILeaveApplication {
  id: number;
  applicationDate?: dayjs.Dayjs | null;
  leaveType?: LeaveType | null;
  description?: string | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  approveDate?: dayjs.Dayjs | null;
  isLineManagerApproved?: boolean | null;
  isHRApproved?: boolean | null;
  isRejected?: boolean | null;
  rejectionComment?: string | null;
  isHalfDay?: boolean | null;
  durationInDay?: number | null;
  phoneNumberOnLeave?: string | null;
  addressOnLeave?: string | null;
  reason?: string | null;
  sanctionedAt?: dayjs.Dayjs | null;
  employeeId?: number | null;

  // employee?: Pick<IEmployee, 'id'> | null;
  // sanctionedBy?: Pick<IUser, 'id' | 'login'> | null;
  // pin?: string | null;
  // fullName?: string | null;
  // designationName?: string | null;

  sanctionedByLogin?: string;
  sanctionedById?: number;
  isChecked?: Boolean;
  sanctionedBy?: number;
  pin?: string;
  fullName?: string;
  designationName?: string;
  departmentName?: string;
  unitName?: string;

}

export type NewLeaveApplication = Omit<ILeaveApplication, 'id'> & { id: null };
