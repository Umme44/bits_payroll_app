export interface IDesignation {
  id?: number;
  designationName?: string;
}

export class Designation implements IDesignation {
  constructor(public id?: number, public designationName?: string) {}
}
