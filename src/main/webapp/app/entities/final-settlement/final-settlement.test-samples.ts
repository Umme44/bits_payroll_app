import dayjs from 'dayjs/esm';

import { IFinalSettlement, NewFinalSettlement } from './final-settlement.model';

export const sampleWithRequiredData: IFinalSettlement = {
  id: 62069,
};

export const sampleWithPartialData: IFinalSettlement = {
  id: 20485,
  dateOfResignation: dayjs('2021-12-11'),
  noticePeriod: 88950,
  lastWorkingDay: dayjs('2021-12-11'),
  mConveyance: 60196,
  salaryPayable: 94756,
  salaryPayableRemarks: 'bandwidth Centralized',
  mobileBillInCash: 71507,
  allowance01Name: 'red secured Jamaican',
  allowance01Amount: 77927,
  allowance03Amount: 67562,
  allowance03Remarks: 'Frozen Digitized Concrete',
  allowance04Remarks: 'Sleek panel',
  deductionHaf: 82179,
  deductionExcessCellBill: 81174,
  deductionAnnualIncomeTax: 71718,
  netSalaryPayable: 2885,
  createdAt: dayjs('2021-12-11'),
  deductionNoticePayDays: 8375,
  deductionAbsentDaysAdjustmentDays: 75493,
  deductionOther: 381,
  totalDeduction: 18058,
  salaryNumOfMonth: 60292,
};

export const sampleWithFullData: IFinalSettlement = {
  id: 255,
  dateOfResignation: dayjs('2021-12-11'),
  noticePeriod: 45925,
  lastWorkingDay: dayjs('2021-12-11'),
  dateOfRelease: dayjs('2021-12-11'),
  serviceTenure: 'Iran Maryland 24/7',
  mBasic: 94140,
  mHouseRent: 71381,
  mMedical: 17552,
  mConveyance: 78158,
  salaryPayable: 98325,
  salaryPayableRemarks: 'Soap generate',
  totalDaysForLeaveEncashment: 4834,
  totalLeaveEncashment: 17200,
  mobileBillInCash: 51954,
  allowance01Name: 'invoice',
  allowance01Amount: 13107,
  allowance01Remarks: 'Interactions incubate Brand',
  allowance02Name: 'reinvent Paradigm',
  allowance02Amount: 67672,
  allowance02Remarks: 'Dollar cross-platform',
  allowance03Name: 'Home',
  allowance03Amount: 53927,
  allowance03Remarks: 'web-enabled initiatives Generic',
  allowance04Name: 'Manager Zimbabwe Planner',
  allowance04Amount: 7807,
  allowance04Remarks: 'Austria',
  deductionNoticePay: 48920,
  deductionPf: 63341,
  deductionHaf: 44961,
  deductionExcessCellBill: 73696,
  deductionAbsentDaysAdjustment: 23604,
  totalSalaryPayable: 60997,
  deductionAnnualIncomeTax: 34420,
  netSalaryPayable: 3709,
  totalPayablePf: 8067,
  totalPayableGf: 31595,
  totalFinalSettlementAmount: 81026,
  createdAt: dayjs('2021-12-11'),
  updatedAt: dayjs('2021-12-12'),
  deductionNoticePayDays: 83829,
  deductionAbsentDaysAdjustmentDays: 34809,
  deductionOther: 54472,
  totalSalary: 14004,
  totalGrossSalary: 78902,
  totalDeduction: 5887,
  finalSettlementDate: dayjs('2021-12-11'),
  isFinalized: false,
  salaryNumOfMonth: 22053,
  remarks: 'maroon mobile Missouri',
};

export const sampleWithNewData: NewFinalSettlement = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
