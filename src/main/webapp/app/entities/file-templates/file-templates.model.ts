import { FileTemplatesType } from 'app/entities/enumerations/file-templates-type.model';
import { FileAccessPrevilage } from 'app/entities/enumerations/file-access-previlage.model';

export interface IFileTemplates {
  id: number;
  title?: string | null;
  filePath?: string | null;
  type?: FileTemplatesType | null;
  accessPrivilege?: FileAccessPrevilage | null;
  isActive?: boolean | null;
  file?: any;
  fileContentType?: string;
}

export type NewFileTemplates = Omit<IFileTemplates, 'id'> & { id: null };
