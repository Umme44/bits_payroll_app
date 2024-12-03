import dayjs from 'dayjs/esm';

import { RequisitionStatus } from 'app/entities/enumerations/requisition-status.model';

import { IProcReqMaster, NewProcReqMaster } from './proc-req-master.model';

export const sampleWithRequiredData: IProcReqMaster = {
  id: 51463,
  requisitionNo: 'Identity',
  requestedDate: dayjs('2023-01-18'),
  requisitionStatus: RequisitionStatus['PARTIALLY_CLOSED'],
  createdAt: dayjs('2023-01-17T10:06'),
};

export const sampleWithPartialData: IProcReqMaster = {
  id: 4416,
  requisitionNo: 'violet Turnpike',
  requestedDate: dayjs('2023-01-17'),
  isCTOApprovalRequired: true,
  requisitionStatus: RequisitionStatus['NOT_APPROVED'],
  expectedReceivedDate: dayjs('2023-01-18'),
  reasoning: '../fake-data/blob/hipster.txt',
  totalApproximatePrice: 16134,
  recommendationAt02: dayjs('2023-01-17T10:21'),
  recommendationAt04: dayjs('2023-01-17T12:19'),
  recommendationAt05: dayjs('2023-01-17T10:01'),
  rejectedDate: dayjs('2023-01-18'),
  closedAt: dayjs('2023-01-18T03:38'),
  createdAt: dayjs('2023-01-17T09:41'),
  updatedAt: dayjs('2023-01-17T16:10'),
};

export const sampleWithFullData: IProcReqMaster = {
  id: 37386,
  requisitionNo: 'hack Burkina Analyst',
  requestedDate: dayjs('2023-01-17'),
  isCTOApprovalRequired: false,
  requisitionStatus: RequisitionStatus['PENDING'],
  expectedReceivedDate: dayjs('2023-01-17'),
  reasoning: '../fake-data/blob/hipster.txt',
  totalApproximatePrice: 97744,
  recommendationAt01: dayjs('2023-01-17T15:45'),
  recommendationAt02: dayjs('2023-01-18T02:26'),
  recommendationAt03: dayjs('2023-01-17T07:25'),
  recommendationAt04: dayjs('2023-01-17T08:54'),
  recommendationAt05: dayjs('2023-01-18T05:58'),
  nextRecommendationOrder: 91927,
  rejectedDate: dayjs('2023-01-18'),
  rejectionReason: '../fake-data/blob/hipster.txt',
  closedAt: dayjs('2023-01-18T06:42'),
  createdAt: dayjs('2023-01-18T06:22'),
  updatedAt: dayjs('2023-01-17T15:55'),
};

export const sampleWithNewData: NewProcReqMaster = {
  requisitionNo: 'deliver Suriname',
  requestedDate: dayjs('2023-01-17'),
  requisitionStatus: RequisitionStatus['PARTIALLY_CLOSED'],
  createdAt: dayjs('2023-01-17T08:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
