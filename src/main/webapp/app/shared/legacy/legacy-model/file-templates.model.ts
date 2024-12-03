import { FileTemplatesType } from 'app/shared/model/enumerations/file-templates-type.model';
import { FileAccessPrevilage } from 'app/shared/model/enumerations/file-access-previlage.model';

export interface IFileTemplates {
  id?: number;
  title?: string;
  filePath?: any;
  type?: FileTemplatesType;
  accessPrivilege?: FileAccessPrevilage;
  isActive?: boolean;
  file?: any;
  fileContentType?: string;
}

export class FileTemplates implements IFileTemplates {
  constructor(
    public id?: number,
    public title?: string,
    public filePath?: any,
    public type?: FileTemplatesType,
    public accessPrivilege?: FileAccessPrevilage,
    public isActive?: boolean,
    public file?: any,
    public fileContentType?: string
  ) {
    this.isActive = this.isActive || false;
  }
}
