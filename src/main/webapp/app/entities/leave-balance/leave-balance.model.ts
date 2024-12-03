import { IEmployee } from 'app/entities/employee/employee.model';
import { LeaveType } from 'app/entities/enumerations/leave-type.model';
import { LeaveAmountType } from 'app/entities/enumerations/leave-amount-type.model';

export interface ILeaveBalance {

  id: number;
  leaveType?: LeaveType | null;
  openingBalance?: number | null;
  closingBalance?: number | null;
  consumedDuringYear?: number | null;
  year?: number | null;
  amount?: number | null;
  leaveAmountType?: LeaveAmountType | null;
  employee?: Pick<IEmployee, 'id'> | null;
  employeeId?: number | null;

  pin?:string | null;
  fullName?: string | null;
  designationName?: string | null;
  departmentName?: string | null;
}

export type NewLeaveBalance = Omit<ILeaveBalance, 'id'> & { id: null };
