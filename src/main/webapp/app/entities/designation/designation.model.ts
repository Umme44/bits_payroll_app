export interface IDesignation {
  id: number;
  designationName?: string | null;
}

export type NewDesignation = Omit<IDesignation, 'id'> & { id: null };
