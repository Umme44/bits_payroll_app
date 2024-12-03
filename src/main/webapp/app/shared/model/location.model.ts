import dayjs from 'dayjs/esm';
import { LocationType } from './enumerations/location-type.model';

export interface ILocation {
  id?: number;
  locationType?: LocationType;
  locationName?: string;
  hasParent?: boolean;
  isLastChild?: boolean;
  createdAt?: dayjs.Dayjs;
  updateAt?: dayjs.Dayjs;
  locationCode?: string;
  parentId?: number;
  parentName?: string;
  parentCode?: string;
  fullLocation?: string;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };

export class Location implements ILocation {
  constructor(
    public id: number,
    public locationType?: LocationType,
    public locationName?: string,
    public hasParent?: boolean,
    public isLastChild?: boolean,
    public createdAt?: dayjs.Dayjs,
    public updateAt?: dayjs.Dayjs,
    public locationCode?: string,
    public parentId?: number,
    public parentName?: string,
    public parentCode?: string,
    public fullLocation?: string
  ) {
    this.hasParent = this.hasParent || false;
    this.isLastChild = this.isLastChild || false;
  }
}
