import dayjs from 'dayjs/esm';
import { MaritalStatus } from '../../shared/model/enumerations/marital-status.model';

export interface IUserEditAccount {
  id?: number;
  fullName?: string;
  maritalStatus?: MaritalStatus;
  dateOfMarriage?: dayjs.Dayjs;
  spouseName?: string;
  presentAddress?: string;
  permanentAddress?: string;
  personalContactNo?: string;
  whatsappId?: string;
  personalEmail?: string;
  skypeId?: string;
  emergencyContactPersonName?: string;
  emergencyContactPersonRelationshipWithEmployee?: string;
  emergencyContactPersonContactNumber?: string;
}

export class UserEditAccount implements IUserEditAccount {
  constructor(
    id?: number,
    public fullName?: string,
    public maritalStatus?: MaritalStatus,
    public dateOfMarriage?: dayjs.Dayjs,
    public spouseName?: string,
    public presentAddress?: string,
    public permanentAddress?: string,
    public personalContactNo?: string,
    public whatsappId?: string,
    public personalEmail?: string,
    public skypeId?: string,
    public emergencyContactPersonName?: string,
    public emergencyContactPersonRelationshipWithEmployee?: string,
    public emergencyContactPersonContactNumber?: string
  ) {}
}
