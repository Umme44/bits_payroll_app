import { ITaxLiability } from 'app/shared/model/tax-liability.model';
import { IHeadSalaryIncome } from 'app/shared/model/head-salary-income.model';
import { IIncomeTaxChallan } from 'app/shared/model/income-tax-challan.model';
import { TaxReportConfigurationsModel } from 'app/shared/model/tax-report-configurations.model';

export interface IIncomeTaxStatement {
  hasConsolidatedExemption?: boolean;

  pin?: String;
  name?: String;

  designation?: String;
  department?: String;

  dateOfJoining?: string;
  dateOfConfirmation?: string;
  contactPeriodEndDate?: string;
  isFixedTermContact?: Boolean;
  tinNumber?: string;
  incomeYearStart?: number;
  incomeYearEnd?: number;
  assessmentYearStart?: number;
  assessmentYearEnd?: number;

  taxLiabilities?: ITaxLiability[];
  summationOfIncomeSlabs?: number;
  salaryIncomes?: IHeadSalaryIncome[];
  totalTaxLiabilities?: number;

  providentFundInvestment?: number;
  otherInvestment?: number;
  maxAllowedInvestment?: number;
  rebate?: number;

  netTaxLiability?: number;
  lastYearAdjustment?: number;
  deductedAmount?: number;
  refundable?: number;

  incomeTaxChallanList?: IIncomeTaxChallan[];

  taxReportConfigurations?: TaxReportConfigurationsModel;

  totalSalaryIncome?: IHeadSalaryIncome;

  signatoryPersonSignature?: string;
  signatoryPersonName?: string;
  signatoryPersonDesignation?: string;
}
