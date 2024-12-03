import { IConfig, NewConfig } from './config.model';

export const sampleWithRequiredData: IConfig = {
  id: 13409,
  key: 'Refined Berkshire',
};

export const sampleWithPartialData: IConfig = {
  id: 84975,
  key: 'green JSON Money',
  value: 'models',
};

export const sampleWithFullData: IConfig = {
  id: 8094,
  key: 'blue strategy',
  value: 'FTP payment access',
};

export const sampleWithNewData: NewConfig = {
  key: 'Small methodology cutting-edge',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
