import { Moment } from 'moment';
import { EmployeeCategory } from 'app/shared/model/enumerations/employee-category.model';
import { RequisitionResourceType } from 'app/shared/model/enumerations/requisition-resource-type.model';
import { RequisitionStatus } from 'app/shared/model/enumerations/requisition-status.model';
import { RecruitmentNatureEnum } from 'app/shared/model/recruitment-nature-enum.model';

export interface IRecruitmentRequisitionForm {
  id?: number;
  expectedJoiningDate?: Moment;
  project?: string;
  numberOfVacancies?: number;
  employmentType?: EmployeeCategory;
  resourceType?: RequisitionResourceType;
  rrfNumber?: string;
  preferredEducationType?: string;
  dateOfRequisition?: Moment;
  requestedDate?: Moment;
  requesterId?: number;
  requesterFullName?: string;
  requesterDesignationName?: string;
  requesterPin?: string;
  recommendationDate01?: Moment;
  recommendationDate02?: Moment;
  recommendationDate03?: Moment;
  recommendationDate04?: Moment;
  recommendationDate05?: Moment;
  requisitionStatus?: RequisitionStatus;
  rejectedAt?: Moment;
  createdAt?: Moment;
  updatedAt?: Moment;
  preferredSkillType?: string;
  recruitmentNatureEnum?: RecruitmentNatureEnum;
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
    public expectedJoiningDate?: Moment,
    public project?: string,
    public numberOfVacancies?: number,
    public employmentType?: EmployeeCategory,
    public resourceType?: RequisitionResourceType,
    public rrfNumber?: string,
    public preferredEducationType?: string,
    public dateOfRequisition?: Moment,
    public requestedDate?: Moment,
    public recommendationDate01?: Moment,
    public recommendationDate02?: Moment,
    public recommendationDate03?: Moment,
    public recommendationDate04?: Moment,
    public requisitionStatus?: RequisitionStatus,
    public rejectedAt?: Moment,
    public createdAt?: Moment,
    public updatedAt?: Moment,
    public preferredSkillType?: string,
    public recruitmentNatureEnum?: RecruitmentNatureEnum,
    public specialRequirement?: string,
    public recommendationDate05?: Moment,
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
