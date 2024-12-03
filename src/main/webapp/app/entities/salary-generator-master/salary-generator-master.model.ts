import { Visibility } from 'app/entities/enumerations/visibility.model';

export interface ISalaryGeneratorMaster {
  id: number;
  year?: string | null;
  month?: string | null;
  isGenerated?: boolean | null;
  isMobileBillImported?: boolean | null;
  isPFLoanRepaymentImported?: boolean | null;
  isAttendanceImported?: boolean | null;
  isSalaryDeductionImported?: boolean | null;
  isFinalized?: boolean | null;
  visibility?: Visibility | null;
  isChecked?: boolean | null;
}

export type NewSalaryGeneratorMaster = Omit<ISalaryGeneratorMaster, 'id'> & { id: null };
