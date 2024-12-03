import { Visibility } from 'app/shared/model/enumerations/visibility.model';

export interface ISalaryGeneratorMaster {
  id?: number;
  year?: string;
  month?: string;
  isGenerated?: boolean;
  isMobileBillImported?: boolean;
  isPFLoanRepaymentImported?: boolean;
  isAttendanceImported?: boolean;
  isSalaryDeductionImported?: boolean;
  isFinalized?: boolean;
  visibility?: Visibility;
  isChecked?: boolean;
}

export class SalaryGeneratorMaster implements ISalaryGeneratorMaster {
  constructor(
    public id?: number,
    public year?: string,
    public month?: string,
    public isGenerated?: boolean,
    public isMobileBillImported?: boolean,
    public isPFLoanRepaymentImported?: boolean,
    public isAttendanceImported?: boolean,
    public isSalaryDeductionImported?: boolean,
    public isFinalized?: boolean,
    public visibility?: Visibility,
    public isChecked?: boolean
  ) {
    this.isGenerated = this.isGenerated || false;
    this.isMobileBillImported = this.isMobileBillImported || false;
    this.isPFLoanRepaymentImported = this.isPFLoanRepaymentImported || false;
    this.isAttendanceImported = this.isAttendanceImported || false;
    this.isSalaryDeductionImported = this.isSalaryDeductionImported || false;
    this.isFinalized = this.isFinalized || false;
  }
}
