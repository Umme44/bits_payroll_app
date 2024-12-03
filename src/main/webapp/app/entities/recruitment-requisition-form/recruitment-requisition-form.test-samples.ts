import dayjs from 'dayjs/esm';

import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';
import { RequisitionResourceType } from 'app/entities/enumerations/requisition-resource-type.model';
import { RequisitionStatus } from 'app/entities/enumerations/requisition-status.model';
import { RecruitmentNature } from 'app/entities/enumerations/recruitment-nature.model';

import { IRecruitmentRequisitionForm, NewRecruitmentRequisitionForm } from './recruitment-requisition-form.model';

export const sampleWithRequiredData: IRecruitmentRequisitionForm = {
  id: 61342,
  expectedJoiningDate: dayjs('2022-02-11'),
  numberOfVacancies: 534,
  employmentType: EmployeeCategory['INTERN'],
  resourceType: RequisitionResourceType['NON_BUDGET'],
  requisitionStatus: RequisitionStatus['NOT_APPROVED'],
};

export const sampleWithPartialData: IRecruitmentRequisitionForm = {
  id: 98603,
  expectedJoiningDate: dayjs('2022-02-10'),
  project: 'model index',
  numberOfVacancies: 122,
  employmentType: EmployeeCategory['INTERN'],
  resourceType: RequisitionResourceType['BUDGET'],
  dateOfRequisition: dayjs('2022-02-11'),
  recommendationDate03: dayjs('2022-02-11'),
  recommendationDate04: dayjs('2022-02-11'),
  requisitionStatus: RequisitionStatus['CLOSED'],
  createdAt: dayjs('2022-02-11T12:57'),
  recruitmentNature: RecruitmentNature['REPLACEMENT'],
};

export const sampleWithFullData: IRecruitmentRequisitionForm = {
  id: 88628,
  expectedJoiningDate: dayjs('2022-02-10'),
  project: 'ROI RAM Berkshire',
  numberOfVacancies: 268,
  employmentType: EmployeeCategory['REGULAR_CONFIRMED_EMPLOYEE'],
  resourceType: RequisitionResourceType['NON_BUDGET'],
  rrfNumber: 'blue applications',
  preferredEducationType: 'maximize',
  dateOfRequisition: dayjs('2022-02-10'),
  requestedDate: dayjs('2022-02-10'),
  recommendationDate01: dayjs('2022-02-11'),
  recommendationDate02: dayjs('2022-02-10'),
  recommendationDate03: dayjs('2022-02-11'),
  recommendationDate04: dayjs('2022-02-10'),
  requisitionStatus: RequisitionStatus['CLOSED'],
  rejectedAt: dayjs('2022-02-11'),
  createdAt: dayjs('2022-02-10T20:28'),
  updatedAt: dayjs('2022-02-11T12:30'),
  isDeleted: true,
  totalOnboard: 8281,
  preferredSkillType: 'Configuration Maine',
  recruitmentNature: RecruitmentNature['PLANNED_ADDITION'],
  specialRequirement: 'paradigms',
  recommendationDate05: dayjs('2022-02-10'),
};

export const sampleWithNewData: NewRecruitmentRequisitionForm = {
  expectedJoiningDate: dayjs('2022-02-11'),
  numberOfVacancies: 529,
  employmentType: EmployeeCategory['CONTRACTUAL_EMPLOYEE'],
  resourceType: RequisitionResourceType['NON_BUDGET'],
  requisitionStatus: RequisitionStatus['IN_PROGRESS'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
