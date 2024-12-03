import { LeaveType } from 'app/entities/enumerations/leave-type.model';

import { ILeaveAllocation, NewLeaveAllocation } from './leave-allocation.model';

export const sampleWithRequiredData: ILeaveAllocation = {
  id: 47398,
  year: 2119,
  allocatedDays: 51103,
};

export const sampleWithPartialData: ILeaveAllocation = {
  id: 87069,
  year: 2129,
  leaveType: LeaveType['NON_MENTIONABLE_PANDEMIC_LEAVE'],
  allocatedDays: 98341,
};

export const sampleWithFullData: ILeaveAllocation = {
  id: 56867,
  year: 1937,
  leaveType: LeaveType['MENTIONABLE_CASUAL_LEAVE'],
  allocatedDays: 18379,
};

export const sampleWithNewData: NewLeaveAllocation = {
  year: 2163,
  allocatedDays: 51043,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
