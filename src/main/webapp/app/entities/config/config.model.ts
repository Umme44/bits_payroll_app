export interface IConfig {
  id: number;
  key?: string | null;
  value?: string | null;
}

export type NewConfig = Omit<IConfig, 'id'> & { id: null };
