import dayjs from 'dayjs/esm';

export interface IEmployeeDocument {
  id: number;
  pin?: string | null;
  fileName?: string | null;
  filePath?: string | null;
  fileExtension?: string | null;
  hasEmployeeVisibility?: boolean | null;
  remarks?: string | null,
  createdBy?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedBy?: string | null;
  updatedAt?: dayjs.Dayjs | null;

  file?: any;
}

export type NewEmployeeDocument = Omit<IEmployeeDocument, 'id'> & { id: null };
