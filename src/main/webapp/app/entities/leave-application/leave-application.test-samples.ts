import dayjs from 'dayjs/esm';

import { LeaveType } from 'app/entities/enumerations/leave-type.model';

import { ILeaveApplication, NewLeaveApplication } from './leave-application.model';

export const sampleWithRequiredData: ILeaveApplication = {
  id: 91940,
};

export const sampleWithPartialData: ILeaveApplication = {
  id: 19260,
  description: 'Global',
  startDate: dayjs('2021-02-28'),
  isHRApproved: true,
  isRejected: true,
  isHalfDay: false,
  durationInDay: 66757,
  phoneNumberOnLeave: 'Bangladesh Gorgeous incremental',
  addressOnLeave: '../fake-data/blob/hipster.txt',
  sanctionedAt: dayjs('2021-02-28T19:07'),
};

export const sampleWithFullData: ILeaveApplication = {
  id: 18336,
  applicationDate: dayjs('2021-02-28'),
  leaveType: LeaveType['Non_Mentionable_Paternity_Leave'],
  description: 'Concrete strategy',
  startDate: dayjs('2021-03-01'),
  endDate: dayjs('2021-03-01'),
  isLineManagerApproved: false,
  isHRApproved: false,
  isRejected: true,
  rejectionComment: 'auxiliary',
  isHalfDay: true,
  durationInDay: 54809,
  phoneNumberOnLeave: 'Books',
  addressOnLeave: '../fake-data/blob/hipster.txt',
  reason: '../fake-data/blob/hipster.txt',
  sanctionedAt: dayjs('2021-03-01T03:34'),
};

export const sampleWithNewData: NewLeaveApplication = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
