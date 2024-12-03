export interface IEmployeeStaticFile {
  id?: number;
  filePath?: string;
  employeeId?: number;
  pin?: string;
  fullName?: string;
  getByteStreamFromFilePath?: ArrayBuffer;
}

export class EmployeeStaticFile implements IEmployeeStaticFile {
  constructor(public id?: number, public filePath?: string, public employeeId?: number, public pin?: string, public fullName?: string) {}
}
