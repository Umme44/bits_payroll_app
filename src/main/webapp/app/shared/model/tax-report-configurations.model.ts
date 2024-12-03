export interface TaxReportConfigurationsModel {
  reportLabels?: ReportLabels;

  salaryIncomeLabels?: SalaryIncomeLabels;
}

interface ReportLabels {
  totalIncomeLabel?: string;
  exemptionRemarks?: string;
  totalTaxLiabilityLabel?: string;
  investmentLabelHeader?: string;
  investmentLabelPf?: string;
  investmentLabelOther?: string;
  investmentLabelTotal?: string;
  lessRebateLabel?: string;
  netTaxLiabilityConsideringRebate?: string;
  finalLabelAit?: string;
  finalLabelActualTax?: string;
  labelTotalPayableTax?: string;
  certifyText?: string;
  noteText?: string;
}

interface SalaryIncomeLabels {
  salaryIncomeBasic?: SalaryIncomeLabel;
  salaryIncomeHr?: SalaryIncomeLabel;
  salaryIncomeMedical?: SalaryIncomeLabel;
  salaryIncomeConveyanceAllowance?: SalaryIncomeLabel;
  salaryIncomeFestivalBonus?: SalaryIncomeLabel;
  salaryIncomePerformanceBonus?: SalaryIncomeLabel;
  salaryIncomeIncentive?: SalaryIncomeLabel;
  salaryIncomePf?: SalaryIncomeLabel;
  salaryIncomeTotal?: SalaryIncomeLabel;
}

interface SalaryIncomeLabel {
  key?: string;
  head?: string;
  subHead?: string;
  isVisibleInTaxReport?: boolean;
}
