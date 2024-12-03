import { IAttendance, NewAttendance } from './attendance.model';

export const sampleWithRequiredData: IAttendance = {
  id: 67192,
};

export const sampleWithPartialData: IAttendance = {
  id: 9030,
  month: 3,
  absentDays: 91085,
  fractionDays: 52861,
};

export const sampleWithFullData: IAttendance = {
  id: 37405,
  year: 2021,
  month: 2,
  absentDays: 73076,
  fractionDays: 66552,
  compensatoryLeaveGained: 92912,
};

export const sampleWithNewData: NewAttendance = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
