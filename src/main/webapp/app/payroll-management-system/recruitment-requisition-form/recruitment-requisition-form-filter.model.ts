import { RequisitionStatus } from '../../shared/model/enumerations/requisition-status.model';

export interface IRrfFilterModel {
  page?: number;
  size?: number;
  sort?: string[];
  employeeId?: number;
  departmentId?: number;
  requisitionStatus?: RequisitionStatus;
  startDate?: string;
  endDate?: string;
}
