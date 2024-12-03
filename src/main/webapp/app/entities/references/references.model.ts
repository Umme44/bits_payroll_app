import { RelationshipWithEmployee } from 'app/entities/enumerations/relationship-with-employee.model';

export interface IReferences {
  id: number;
  name?: string | null;
  institute?: string | null;
  designation?: string | null;
  relationshipWithEmployee?: RelationshipWithEmployee | null;
  email?: string | null;
  contactNumber?: string | null;
  employeeId?: number;
  pin?: number;
  employeeName?: number;
  designationName?: number;
  departmentName?: number;
  unitName?: number;
}

export type NewReferences = Omit<IReferences, 'id'> & { id: null };
