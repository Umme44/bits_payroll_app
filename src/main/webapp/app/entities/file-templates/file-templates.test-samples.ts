import { FileTemplatesType } from 'app/entities/enumerations/file-templates-type.model';
import { FileAccessPrevilage } from 'app/entities/enumerations/file-access-previlage.model';

import { IFileTemplates, NewFileTemplates } from './file-templates.model';

export const sampleWithRequiredData: IFileTemplates = {
  id: 34873,
  title: 'EXE',
  isActive: false,
};

export const sampleWithPartialData: IFileTemplates = {
  id: 97509,
  title: 'robust matrices',
  type: FileTemplatesType['TEMPLATES'],
  accessPrivilege: FileAccessPrevilage['MANAGEMENT'],
  isActive: true,
};

export const sampleWithFullData: IFileTemplates = {
  id: 18518,
  title: 'installation',
  filePath: '../fake-data/blob/hipster.txt',
  type: FileTemplatesType['FORMS'],
  accessPrivilege: FileAccessPrevilage['MANAGEMENT'],
  isActive: true,
};

export const sampleWithNewData: NewFileTemplates = {
  title: 'Response Market',
  isActive: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
