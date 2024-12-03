import dayjs from 'dayjs/esm';

import { PfAccountStatus } from 'app/entities/enumerations/pf-account-status.model';

import { IPfAccount, NewPfAccount } from './pf-account.model';

export const sampleWithRequiredData: IPfAccount = {
  id: 60679,
};

export const sampleWithPartialData: IPfAccount = {
  id: 65301,
  pfCode: 'Rustic Sleek',
  membershipStartDate: dayjs('2021-04-17'),
  membershipEndDate: dayjs('2021-04-17'),
  status: PfAccountStatus['INACTIVE'],
  designationName: 'interface communities Poland',
  unitName: 'XSS platforms redundant',
  pin: 'implementation payment Cotton',
  dateOfJoining: dayjs('2021-04-17'),
  dateOfConfirmation: dayjs('2021-04-17'),
};

export const sampleWithFullData: IPfAccount = {
  id: 66731,
  pfCode: '24/7 invoice',
  membershipStartDate: dayjs('2021-04-17'),
  membershipEndDate: dayjs('2021-04-17'),
  status: PfAccountStatus['CLOSED'],
  designationName: 'generating FTP Account',
  departmentName: 'bypass Missouri',
  unitName: 'Rubber Associate service-desk',
  accHolderName: 'Ohio bluetooth',
  pin: 'Coordinator Computers',
  dateOfJoining: dayjs('2021-04-17'),
  dateOfConfirmation: dayjs('2021-04-17'),
};

export const sampleWithNewData: NewPfAccount = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
