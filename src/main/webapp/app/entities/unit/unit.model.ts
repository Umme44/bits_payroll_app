export interface IUnit {
  id: number;
  unitName?: string | null;
}

export type NewUnit = Omit<IUnit, 'id'> & { id: null };
