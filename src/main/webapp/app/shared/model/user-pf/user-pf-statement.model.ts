import dayjs from 'dayjs/esm';

export interface IUserPfStatement {
  fullName?: string;
  pin?: string;
  designationName?: string;
  departmentName?: string;

  previousClosingBalanceDate?: dayjs.Dayjs;

  openingBalance?: number;
  openingBalanceDate?: dayjs.Dayjs;

  previousYear?: number;
  previousYearMemberPfContribution?: number;
  previousYearCompanyPfContribution?: number;

  openingAndPreviousYearContributionInTotal?: number;

  selectedYear?: number;
  selectedYearMemberPfContribution?: number;
  selectedYearCompanyPfContribution?: number;
  selectedYearTotalPfContribution?: number;

  tillSelectedMonthYearPfMemberInterest?: number;
  tillSelectedMonthYearPfCompanyInterest?: number;
  totalTillSelectedMonthYearPfCompanyInterest?: number;

  totalClosingBalance?: number;
}
