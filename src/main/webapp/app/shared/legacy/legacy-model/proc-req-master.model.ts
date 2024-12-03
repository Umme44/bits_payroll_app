import { RequisitionStatus } from 'app/shared/model/enumerations/requisition-status.model';
import dayjs from 'dayjs';
import { IProcReq } from '../../../entities/proc-req/proc-req.model';
export interface IProcReqMaster {
  /*  id?: number;
  requisitionNo?: string;
  requestedDate?: dayjs;
  isCTOApprovalRequired?: boolean;
  requisitionStatus?: RequisitionStatus;
  expectedReceivedDate?: dayjs;
  reasoning?: any;
  totalApproximatePrice?: number;
  recommendationAt01?: dayjs;
  recommendationAt02?: dayjs;
  recommendationAt03?: dayjs;
  recommendationAt04?: dayjs;
  recommendationAt05?: dayjs;
  nextRecommendationOrder?: number;
  rejectedDate?: dayjs;
  rejectionReason?: any;
  closedAt?: dayjs;
  createdAt?: dayjs;
  updatedAt?: dayjs;
  departmentName?: string;
  departmentId?: number;
  requestedById?: number;
  requestedByFullName?: string;
  requestedByPIN?: string;
  requestedByOfficialContactNo?: string;
  requestedByDesignationName?: string;
  requestedByDepartmentName?: string;
  recommendedBy01FullName?: string;
  recommendedBy01Designation?: string;
  recommendedBy01Department?: string;
  recommendedBy01Id?: number;
  recommendedBy02FullName?: string;
  recommendedBy02Designation?: string;
  recommendedBy02Department?: string;
  recommendedBy02Id?: number;
  recommendedBy03FullName?: string;
  recommendedBy03Designation?: string;
  recommendedBy03Department?: string;
  recommendedBy03Id?: number;
  recommendedBy04FullName?: string;
  recommendedBy04Designation?: string;
  recommendedBy04Department?: string;
  recommendedBy04Id?: number;
  recommendedBy05FullName?: string;
  recommendedBy05Designation?: string;
  recommendedBy05Department?: string;
  recommendedBy05Id?: number;
  nextApprovalFromFullName?: string;
  nextApprovalFromId?: number;
  rejectedByFullName?: string;
  rejectedById?: number;
  closedByFullName?: string;
  closedById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  createdByLogin?: string;
  createdById?: number;
  procReqs?: IProcReq[];*/
  id: number;
  requisitionNo?: string | null;
  requestedDate?: dayjs.Dayjs | null;
  isCTOApprovalRequired?: boolean | null;
  requisitionStatus?: RequisitionStatus | null;
  expectedReceivedDate?: dayjs.Dayjs | null;
  reasoning?: string | null;
  totalApproximatePrice?: number | null;
  recommendationAt01?: dayjs.Dayjs | null;
  recommendationAt02?: dayjs.Dayjs | null;
  recommendationAt03?: dayjs.Dayjs | null;
  recommendationAt04?: dayjs.Dayjs | null;
  recommendationAt05?: dayjs.Dayjs | null;
  nextRecommendationOrder?: number | null;
  rejectedDate?: dayjs.Dayjs | null;
  rejectionReason?: string | null;
  closedAt?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;

  departmentName?: string;
  departmentId?: number;
  requestedById?: number;
  requestedByFullName?: string;
  requestedByPIN?: string;
  requestedByOfficialContactNo?: string;
  requestedByDesignationName?: string;
  requestedByDepartmentName?: string;
  recommendedBy01FullName?: string;
  recommendedBy01Designation?: string;
  recommendedBy01Department?: string;
  recommendedBy01Id?: number;
  recommendedBy02FullName?: string;
  recommendedBy02Designation?: string;
  recommendedBy02Department?: string;
  recommendedBy02Id?: number;
  recommendedBy03FullName?: string;
  recommendedBy03Designation?: string;
  recommendedBy03Department?: string;
  recommendedBy03Id?: number;
  recommendedBy04FullName?: string;
  recommendedBy04Designation?: string;
  recommendedBy04Department?: string;
  recommendedBy04Id?: number;
  recommendedBy05FullName?: string;
  recommendedBy05Designation?: string;
  recommendedBy05Department?: string;
  recommendedBy05Id?: number;
  nextApprovalFromFullName?: string;
  nextApprovalFromId?: number;
  rejectedByFullName?: string;
  rejectedById?: number;
  closedByFullName?: string;
  closedById?: number;
  updatedByLogin?: string;
  updatedById?: number;
  createdByLogin?: string;
  createdById?: number;
  procReqs?: IProcReq[];
}

export class ProcReqMaster implements IProcReqMaster {
  constructor(
    public id?: number,
    public requisitionNo?: string,
    public requestedDate?: dayjs,
    public isCTOApprovalRequired?: boolean,
    public requisitionStatus?: RequisitionStatus,
    public expectedReceivedDate?: dayjs,
    public reasoning?: any,
    public totalApproximatePrice?: number,
    public recommendationAt01?: dayjs,
    public recommendationAt02?: dayjs,
    public recommendationAt03?: dayjs,
    public recommendationAt04?: dayjs,
    public recommendationAt05?: dayjs,
    public nextRecommendationOrder?: number,
    public rejectedDate?: dayjs,
    public rejectionReason?: any,
    public closedAt?: dayjs,
    public createdAt?: dayjs,
    public updatedAt?: dayjs,
    public departmentName?: string,
    public departmentId?: number,
    public requestedByFullName?: string,
    public requestedById?: number,
    public recommendedBy01FullName?: string,
    public recommendedBy01Id?: number,
    public recommendedBy02FullName?: string,
    public recommendedBy02Id?: number,
    public recommendedBy03FullName?: string,
    public recommendedBy03Id?: number,
    public recommendedBy04FullName?: string,
    public recommendedBy04Id?: number,
    public recommendedBy05FullName?: string,
    public recommendedBy05Id?: number,
    public nextApprovalFromFullName?: string,
    public nextApprovalFromId?: number,
    public rejectedByFullName?: string,
    public rejectedById?: number,
    public closedByFullName?: string,
    public closedById?: number,
    public updatedByLogin?: string,
    public updatedById?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public procReqs?: IProcReq[]
  ) {
    //this.isCTOApprovalRequired = this.isCTOApprovalRequired || false;
  }
}
