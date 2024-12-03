import { LeaveType } from 'app/entities/enumerations/leave-type.model';
import { LeaveAmountType } from 'app/entities/enumerations/leave-amount-type.model';

import { ILeaveBalance, NewLeaveBalance } from './leave-balance.model';

export const sampleWithRequiredData: ILeaveBalance = {
  id: 97528,
};

export const sampleWithPartialData: ILeaveBalance = {
  id: 86413,
  leaveType: LeaveType['Other'],
  closingBalance: 52500,
  consumedDuringYear: 94279,
  year: 10603,
  leaveAmountType: LeaveAmountType['Year'],
};

export const sampleWithFullData: ILeaveBalance = {
  id: 29916,
  leaveType: LeaveType['Mentionable_Casual_Leave'],
  openingBalance: 89098,
  closingBalance: 45885,
  consumedDuringYear: 95467,
  year: 44057,
  amount: 45623,
  leaveAmountType: LeaveAmountType['Day'],
};

export const sampleWithNewData: NewLeaveBalance = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
