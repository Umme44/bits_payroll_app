import { LeaveType } from 'app/shared/model/enumerations/leave-type.model';

export interface ILeaveAllocation {
  id?: number;
  year?: number;
  leaveType?: LeaveType;
  allocatedDays?: number;
}

export class LeaveAllocation implements ILeaveAllocation {
  constructor(public id?: number, public year?: number, public leaveType?: LeaveType, public allocatedDays?: number) {}
}
