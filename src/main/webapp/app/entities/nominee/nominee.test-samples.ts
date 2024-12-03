import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';
import { NomineeType } from 'app/entities/enumerations/nominee-type.model';
import { IdentityType } from 'app/entities/enumerations/identity-type.model';

import { INominee, NewNominee } from './nominee.model';

export const sampleWithRequiredData: INominee = {
  id: 87924,
  nomineeName: 'Senior',
  presentAddress: 'heuristic vortals',
  sharePercentage: 48,
  status: Status['NOT_APPROVED'],
  permanentAddress: 'revolutionary Village Small',
};

export const sampleWithPartialData: INominee = {
  id: 46420,
  nomineeName: 'withdrawal Nepal Associate',
  presentAddress: 'B2B Steel Granite',
  dateOfBirth: dayjs('2022-04-23'),
  sharePercentage: 98,
  status: Status['APPROVED'],
  guardianName: 'Nepalese',
  guardianSpouseName: 'ADP payment markets',
  guardianDateOfBirth: dayjs('2022-04-23'),
  guardianDocumentName: 'turquoise',
  guardianRelationshipWith: 'Creek Tactics',
  isLocked: false,
  permanentAddress: 'invoice superstructure',
  guardianIdentityType: IdentityType['NID'],
};

export const sampleWithFullData: INominee = {
  id: 42758,
  nomineeName: 'Montana indigo Somoni',
  presentAddress: 'Minnesota Cotton',
  relationshipWithEmployee: 'Specialist',
  dateOfBirth: dayjs('2022-04-23'),
  age: 28928,
  sharePercentage: 45,
  imagePath: 'payment',
  status: Status['NOT_APPROVED'],
  guardianName: 'Maryland improvement',
  guardianFatherName: 'Movies',
  guardianSpouseName: 'array',
  guardianDateOfBirth: dayjs('2022-04-24'),
  guardianPresentAddress: 'composite redundant',
  guardianDocumentName: 'Borders Auto indexing',
  guardianRelationshipWith: 'SMS scalable',
  guardianImagePath: 'Sleek Plastic lavender',
  isLocked: false,
  nominationDate: dayjs('2022-04-23'),
  permanentAddress: 'Phased withdrawal bluetooth',
  guardianPermanentAddress: 'Frozen',
  nomineeType: NomineeType['GRATUITY_FUND'],
  identityType: IdentityType['PASSPORT'],
  documentName: 'virtual',
  idNumber: 'Zimbabwe Tuna Village',
  isNidVerified: true,
  guardianIdentityType: IdentityType['OTHER'],
  guardianIdNumber: 'Configuration',
  isGuardianNidVerified: true,
};

export const sampleWithNewData: NewNominee = {
  nomineeName: 'sexy Granite Gorgeous',
  presentAddress: 'red AGP orange',
  sharePercentage: 61,
  status: Status['APPROVED'],
  permanentAddress: 'Open-architected',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
