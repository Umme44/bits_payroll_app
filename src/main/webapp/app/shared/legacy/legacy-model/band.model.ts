import dayjs from 'dayjs/esm';

export interface IBand {
  id?: number;
  bandName?: string;
  minSalary?: number;
  maxSalary?: number;
  welfareFund?: number;
  mobileCelling?: number;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;

  key?: number;
  value?: number;
}

export class Band implements IBand {
  constructor(
    public id?: number,
    public bandName?: string,
    public minSalary?: number,
    public maxSalary?: number,
    public welfareFund?: number,
    public mobileCelling?: number,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number,
    public key?: number,
    public value?: number
  ) {}
}
