import { IAttendanceTimeSheet } from './attendance-time-sheet.model';
import { IEmployee } from '../legacy/legacy-model/employee.model';

export interface IMyTeamEmployee {
  employee?: IEmployee;
  attendances?: IAttendanceTimeSheet[];
}

export class MyTeamEmployee implements IMyTeamEmployee {
  constructor(public employee?: IEmployee, public attendances?: IAttendanceTimeSheet[]) {}
}
