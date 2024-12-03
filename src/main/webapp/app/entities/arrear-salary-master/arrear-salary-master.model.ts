export interface IArrearSalaryMaster {
  id: number;
  title?: string | null;
  isLocked?: boolean | null;
  isDeleted?: boolean | null;
}

export type NewArrearSalaryMaster = Omit<IArrearSalaryMaster, 'id'> & { id: null };
