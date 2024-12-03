export interface IEmployeeSpecializedSearch {
  id?: number;
  fullName?: String;
  pin?: String;
  officialEmail?: String;
  officialContactNo?: String;
  whatsappId?: String;
  skypeId?: String;
  designationName?: String;
  departmentName?: String;
  unitName?: String;
  reportingToId?: number;
  reportingToName?: String;
  organizationalHierarchy?: IEmployeeSpecializedSearch[];
  peopleReportingTo?: IEmployeeSpecializedSearch[];
}

export class EmployeeSpecializedSearch implements IEmployeeSpecializedSearch {
  constructor(
    public id?: number,
    public fullName?: String,
    public pin?: String,
    public officialEmail?: String,
    public whatsappId?: String,
    public skypeId?: String,
    public designationName?: String,
    public departmentName?: String,
    public unitName?: String,
    public reportingToId?: number,
    public reportingToName?: String,

    public organizationalHierarchy?: IEmployeeSpecializedSearch[],
    public peopleReportingTo?: IEmployeeSpecializedSearch[]
  ) {}
}
