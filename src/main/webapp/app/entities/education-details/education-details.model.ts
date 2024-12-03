export interface IEducationDetails {
  id: number;
  nameOfDegree?: string | null;
  subject?: string | null;
  institute?: string | null;
  yearOfDegreeCompletion?: string | null;
  employeeId?: number;
  pin?: number;
  employeeName?: number;
  designationName?: number;
  departmentName?: number;
  unitName?: number;
}

export type NewEducationDetails = Omit<IEducationDetails, 'id'> & { id: null };
