export interface ISalaryGenerationModel {
  year?: number;
  departmentId?: number;
  designationId?: number;
  month?: number;
}

export class SalaryGenerationModel implements ISalaryGenerationModel {
  constructor(public year?: number, public month?: number, public designationId?: number, public departmentId?: number) {}
}
