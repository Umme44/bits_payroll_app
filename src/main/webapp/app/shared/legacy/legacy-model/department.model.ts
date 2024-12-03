export interface IDepartment {
  id?: number;
  departmentName?: string;
  departmentHeadId?: number;
  departmentHeadFullName?: string;
  departmentHeadPin?: string;
}

export class Department implements IDepartment {
  constructor(public id?: number, public departmentName?: string, public departmentHeadId?: number) {}
}
