import { IProcReq, NewProcReq } from './proc-req.model';

export const sampleWithRequiredData: IProcReq = {
  id: 53832,
  quantity: 97123,
};

export const sampleWithPartialData: IProcReq = {
  id: 3595,
  quantity: 39405,
};

export const sampleWithFullData: IProcReq = {
  id: 76923,
  quantity: 64364,
  referenceFilePath: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewProcReq = {
  quantity: 12975,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
