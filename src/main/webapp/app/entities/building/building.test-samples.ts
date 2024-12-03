import dayjs from 'dayjs/esm';

import { IBuilding, NewBuilding } from './building.model';

export const sampleWithRequiredData: IBuilding = {
  id: 15365,
  buildingName: 'Multi-layered firmware Run',
};

export const sampleWithPartialData: IBuilding = {
  id: 28099,
  buildingName: 'leverage Refined seamless',
  remarks: 'Exclusive drive 1080p',
  updatedAt: dayjs('2022-06-06T04:16'),
};

export const sampleWithFullData: IBuilding = {
  id: 5870,
  buildingName: 'mobile',
  buildingLocation: 'Sausages challenge',
  remarks: 'Iran Soft Kansas',
  createdAt: dayjs('2022-06-05T14:22'),
  updatedAt: dayjs('2022-06-06T06:41'),
};

export const sampleWithNewData: NewBuilding = {
  buildingName: 'Industrial payment Jersey',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
