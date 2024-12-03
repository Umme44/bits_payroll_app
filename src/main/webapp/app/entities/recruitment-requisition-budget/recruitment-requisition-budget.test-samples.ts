import { IRecruitmentRequisitionBudget, NewRecruitmentRequisitionBudget } from './recruitment-requisition-budget.model';

export const sampleWithRequiredData: IRecruitmentRequisitionBudget = {
  id: 62991,
  year: 1987,
  budget: 97626,
  remainingBudget: 54879,
  remainingManpower: 68997,
};

export const sampleWithPartialData: IRecruitmentRequisitionBudget = {
  id: 85207,
  year: 1987,
  budget: 75733,
  remainingBudget: 41472,
  remainingManpower: 42448,
};

export const sampleWithFullData: IRecruitmentRequisitionBudget = {
  id: 59630,
  year: 2048,
  budget: 89874,
  remainingBudget: 34069,
  remainingManpower: 65162,
};

export const sampleWithNewData: NewRecruitmentRequisitionBudget = {
  year: 2008,
  budget: 39624,
  remainingBudget: 56864,
  remainingManpower: 14298,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
