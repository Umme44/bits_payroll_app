import dayjs from 'dayjs/esm';

import { IdentityType } from 'app/entities/enumerations/identity-type.model';

import { IPfNominee, NewPfNominee } from './pf-nominee.model';

export const sampleWithRequiredData: IPfNominee = {
  id: 17997,
  identityType: IdentityType['PASSPORT'],
  idNumber: 'Norway',
};

export const sampleWithPartialData: IPfNominee = {
  id: 77070,
  fullName: 'Investment Account Incredible',
  presentAddress: 'database Car maximized',
  permanentAddress: 'Towels',
  dateOfBirth: dayjs('2021-04-17'),
  age: 39929,
  nidNumber: 'Forest',
  isNidVerified: true,
  passportNumber: 'cross-media Agent Colon',
  brnNumber: 'Towels',
  photo: 'withdrawal Togo',
  guardianName: 'real-time Centralized',
  guardianPresentAddress: 'orchid',
  guardianProofOfIdentityOfLegalGuardian: 'migration',
  guardianNidNumber: 'Licensed Implementation',
  guardianIdNumber: 'Cotton Oklahoma Tennessee',
  isGuardianNidVerified: false,
  isApproved: false,
  identityType: IdentityType['OTHER'],
  idNumber: 'Man intangible',
};

export const sampleWithFullData: IPfNominee = {
  id: 88462,
  nominationDate: dayjs('2021-04-17'),
  fullName: 'SDD',
  presentAddress: 'parsing Dobra',
  permanentAddress: 'Future front-end',
  relationship: 'Cheese RSS partnerships',
  dateOfBirth: dayjs('2021-04-17'),
  age: 37113,
  sharePercentage: 49733,
  nidNumber: 'radical',
  isNidVerified: true,
  passportNumber: 'Dynamic paradigm',
  brnNumber: 'Garden',
  photo: 'generation',
  guardianName: 'Somali Granite Guiana',
  guardianFatherOrSpouseName: 'deposit',
  guardianDateOfBirth: dayjs('2021-04-17'),
  guardianPresentAddress: 'Alabama',
  guardianPermanentAddress: 'invoice',
  guardianProofOfIdentityOfLegalGuardian: 'Avon',
  guardianRelationshipWithNominee: 'Global Supervisor Front-line',
  guardianNidNumber: 'local Tennessee transmitting',
  guardianBrnNumber: 'Dobra Shoes Buckinghamshire',
  guardianIdNumber: 'Account Handcrafted',
  isGuardianNidVerified: false,
  isApproved: true,
  identityType: IdentityType['BIRTH_REGISTRATION'],
  idNumber: 'Tuna enhance',
  documentName: 'indigo',
  guardianIdentityType: IdentityType['BIRTH_REGISTRATION'],
  guardianDocumentName: 'Eritrea Principal',
};

export const sampleWithNewData: NewPfNominee = {
  identityType: IdentityType['OTHER'],
  idNumber: 'payment Granite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
