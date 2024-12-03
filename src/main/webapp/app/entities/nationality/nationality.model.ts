export interface INationality {
  id: number;
  nationalityName?: string | null;
}

export type NewNationality = Omit<INationality, 'id'> & { id: null };
