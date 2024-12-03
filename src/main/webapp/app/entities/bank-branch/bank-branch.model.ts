export interface IBankBranch {
  id: number;
  branchName?: string | null;
}

export type NewBankBranch = Omit<IBankBranch, 'id'> & { id: null };
