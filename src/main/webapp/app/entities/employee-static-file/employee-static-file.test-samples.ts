import { IEmployeeStaticFile, NewEmployeeStaticFile } from './employee-static-file.model';

export const sampleWithRequiredData: IEmployeeStaticFile = {
  id: 94564,
};

export const sampleWithPartialData: IEmployeeStaticFile = {
  id: 45678,
  filePath: 'GB deposit Pizza',
};

export const sampleWithFullData: IEmployeeStaticFile = {
  id: 46986,
  filePath: 'Concrete Chips Dynamic',
};

export const sampleWithNewData: NewEmployeeStaticFile = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
