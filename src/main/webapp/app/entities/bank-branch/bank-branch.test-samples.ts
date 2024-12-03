import { IBankBranch, NewBankBranch } from './bank-branch.model';

export const sampleWithRequiredData: IBankBranch = {
  id: 64952,
  branchName: 'Shoes hierarchy Cambridge',
};

export const sampleWithPartialData: IBankBranch = {
  id: 20414,
  branchName: 'seamless',
};

export const sampleWithFullData: IBankBranch = {
  id: 23859,
  branchName: 'Intelligent',
};

export const sampleWithNewData: NewBankBranch = {
  branchName: 'Stravenue Engineer EXE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
