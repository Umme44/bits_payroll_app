import { LeaveType } from 'app/shared/model/enumerations/leave-type.model';
import { LeaveAmountType } from 'app/shared/model/enumerations/leave-amount-type.model';

export interface ILeaveBalanceEndUserView {
  id?: number;
  pin?: string;
  name?: string;
  leaveType?: LeaveType;
  openingBalance: number;
  closingBalance: number;
  consumedDuringYear?: number;
  year?: number;
  amount: number;
  leaveAmountType?: LeaveAmountType;
  daysRequested: number;
  daysApproved: number;
  daysCancelled: number;
  daysRemaining: number;
  daysEffective: number;
  employeeId?: number;
}
