import dayjs from 'dayjs/esm';

import { IWorkingExperience, NewWorkingExperience } from './working-experience.model';

export const sampleWithRequiredData: IWorkingExperience = {
  id: 36130,
};

export const sampleWithPartialData: IWorkingExperience = {
  id: 97275,
  organizationName: 'Investment orchid programming',
  dojOfLastOrganization: dayjs('2021-02-14'),
  dorOfLastOrganization: dayjs('2021-02-14'),
};

export const sampleWithFullData: IWorkingExperience = {
  id: 68759,
  lastDesignation: 'compressing',
  organizationName: 'Market Fresh Customer',
  dojOfLastOrganization: dayjs('2021-02-14'),
  dorOfLastOrganization: dayjs('2021-02-15'),
};

export const sampleWithNewData: NewWorkingExperience = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
