import { IUnit, NewUnit } from './unit.model';

export const sampleWithRequiredData: IUnit = {
  id: 80394,
  unitName: 'Grass-roots portal',
};

export const sampleWithPartialData: IUnit = {
  id: 73016,
  unitName: 'Synergized Generic compressing',
};

export const sampleWithFullData: IUnit = {
  id: 16378,
  unitName: 'infomediaries',
};

export const sampleWithNewData: NewUnit = {
  unitName: 'JSON copying human-resource',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
