export interface IDeductionType {
  id: number;
  name?: string | null;
}

export type NewDeductionType = Omit<IDeductionType, 'id'> & { id: null };
