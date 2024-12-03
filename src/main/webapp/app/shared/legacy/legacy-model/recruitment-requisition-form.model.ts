import dayjs from 'dayjs/esm';
import { EmployeeCategory } from 'app/shared/model/enumerations/employee-category.model';
import { RequisitionResourceType } from 'app/shared/model/enumerations/requisition-resource-type.model';
import { RequisitionStatus } from 'app/shared/model/enumerations/requisition-status.model';
import { RecruitmentNature } from '../../../entities/enumerations/recruitment-nature.model';

export interface IRecruitmentRequisitionForm {
  id?: number;
  expectedJoiningDate?: dayjs.Dayjs;
  project?: string;
  numberOfVacancies?: number;
  employmentType?: EmployeeCategory;
  resourceType?: RequisitionResourceType;
  rrfNumber?: string;
  preferredEducationType?: string;
  dateOfRequisition?: dayjs.Dayjs;
  requestedDate?: dayjs.Dayjs;
  requesterId?: number;
  requesterFullName?: string;
  requesterDesignationName?: string;
  requesterPin?: string;
  recommendationDate01?: dayjs.Dayjs;
  recommendationDate02?: dayjs.Dayjs;
  recommendationDate03?: dayjs.Dayjs;
  recommendationDate04?: dayjs.Dayjs;
  recommendationDate05?: dayjs.Dayjs;
  requisitionStatus?: RequisitionStatus;
  rejectedAt?: dayjs.Dayjs;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  preferredSkillType?: string;
  recruitmentNature?: RecruitmentNature;
  specialRequirement?: string;
  functionalDesignationId?: number;
  functionalDesignationName?: string;
  bandId?: number;
  bandName?: string;
  departmentId?: number;
  departmentName?: string;
  unitId?: number;
  unitName?: string;
  recommendedBy01Id?: number;
  recommendedBy01FullName?: string;
  recommendedBy02Id?: number;
  recommendedBy02FullName?: string;
  recommendedBy03Id?: number;
  recommendedBy03FullName?: string;
  recommendedBy04Id?: number;
  recommendedBy04FullName?: string;
  rejectedById?: number;
  isChecked?: boolean;
  createdByLogin?: string;
  createdById?: number;
  updatedByLogin?: string;
  updatedById?: number;

  isDeleted?: boolean;
  deletedByLogin?: string;
  deletedById?: number;
  recommendedBy05Id?: number;
  recommendedBy05FullName?: string;
  totalOnboard?: number;
  employeeToBeReplacedId?: number | undefined;
  employeeToBeReplacedFullName?: string | undefined;
  employeeToBeReplacedPin?: string | undefined;
}

export class RecruitmentRequisitionForm implements IRecruitmentRequisitionForm {
  constructor(
    public id?: number,
    public expectedJoiningDate?: dayjs.Dayjs,
    public project?: string,
    public numberOfVacancies?: number,
    public employmentType?: EmployeeCategory,
    public resourceType?: RequisitionResourceType,
    public rrfNumber?: string,
    public preferredEducationType?: string,
    public dateOfRequisition?: dayjs.Dayjs,
    public requestedDate?: dayjs.Dayjs,
    public recommendationDate01?: dayjs.Dayjs,
    public recommendationDate02?: dayjs.Dayjs,
    public recommendationDate03?: dayjs.Dayjs,
    public recommendationDate04?: dayjs.Dayjs,
    public requisitionStatus?: RequisitionStatus,
    public rejectedAt?: dayjs.Dayjs,
    public createdAt?: dayjs.Dayjs,
    public updatedAt?: dayjs.Dayjs,
    public preferredSkillType?: string,
    public recruitmentNature?: RecruitmentNature,
    public specialRequirement?: string,
    public recommendationDate05?: dayjs.Dayjs,
    public functionalDesignationId?: number,
    public bandId?: number,
    public departmentId?: number,
    public unitId?: number,
    public recommendedBy01Id?: number,
    public recommendedBy02Id?: number,
    public recommendedBy03Id?: number,
    public recommendedBy04Id?: number,
    public requesterId?: number,
    public rejectedById?: number,
    public createdByLogin?: string,
    public createdById?: number,
    public updatedByLogin?: string,
    public updatedById?: number,
    public isChecked?: boolean,

    public isDeleted?: boolean,
    public deletedByLogin?: string,
    public deletedById?: number,
    public totalOnboard?: number,
    public recommendedBy05Id?: number,
    public employeeToBeReplacedId?: number,
    public employeeToBeReplacedFullName?: string,
    public employeeToBeReplacedPin?: string
  ) {}
}
