import { LeaveType } from 'app/entities/enumerations/leave-type.model';

export interface ILeaveAllocation {
  id: number;
  year?: number | null;
  leaveType?: LeaveType | null;
  allocatedDays?: number | null;
}

export type NewLeaveAllocation = Omit<ILeaveAllocation, 'id'> & { id: null };
