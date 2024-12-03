import dayjs from 'dayjs/esm';

export interface IOffer {
  id?: number;
  title?: string;
  description?: string;
  imagePath?: string;
  createdAt?: dayjs.Dayjs;
}

export class Offer implements IOffer {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public imagePath?: string,
    public createdAt?: dayjs.Dayjs
  ) {}
}
