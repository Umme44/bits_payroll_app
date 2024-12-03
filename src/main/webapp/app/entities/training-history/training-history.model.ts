import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface ITrainingHistory {
  id: number;
  trainingName?: string | null;
  coordinatedBy?: string | null;
  dateOfCompletion?: dayjs.Dayjs | null;
  employeeId?: number;
  pin?: number;
  employeeName?: number;
  designationName?: number;
  departmentName?: number;
  unitName?: number;
}

export type NewTrainingHistory = Omit<ITrainingHistory, 'id'> & { id: null };
