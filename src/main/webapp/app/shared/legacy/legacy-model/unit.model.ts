export interface IUnit {
  id?: number;
  unitName?: string;
}

export class Unit implements IUnit {
  constructor(public id?: number, public unitName?: string) {}
}
