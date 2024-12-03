import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRecruitmentRequisitionForm, NewRecruitmentRequisitionForm } from '../recruitment-requisition-form.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecruitmentRequisitionForm for edit and NewRecruitmentRequisitionFormFormGroupInput for create.
 */
type RecruitmentRequisitionFormFormGroupInput = IRecruitmentRequisitionForm | PartialWithRequiredKeyOf<NewRecruitmentRequisitionForm>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRecruitmentRequisitionForm | NewRecruitmentRequisitionForm> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type RecruitmentRequisitionFormFormRawValue = FormValueOf<IRecruitmentRequisitionForm>;

type NewRecruitmentRequisitionFormFormRawValue = FormValueOf<NewRecruitmentRequisitionForm>;

type RecruitmentRequisitionFormFormDefaults = Pick<NewRecruitmentRequisitionForm, 'id' | 'createdAt' | 'updatedAt' | 'isDeleted'>;

type RecruitmentRequisitionFormFormGroupContent = {
  id: FormControl<RecruitmentRequisitionFormFormRawValue['id'] | NewRecruitmentRequisitionForm['id']>;
  expectedJoiningDate: FormControl<RecruitmentRequisitionFormFormRawValue['expectedJoiningDate']>;
  project: FormControl<RecruitmentRequisitionFormFormRawValue['project']>;
  numberOfVacancies: FormControl<RecruitmentRequisitionFormFormRawValue['numberOfVacancies']>;
  employmentType: FormControl<RecruitmentRequisitionFormFormRawValue['employmentType']>;
  resourceType: FormControl<RecruitmentRequisitionFormFormRawValue['resourceType']>;
  rrfNumber: FormControl<RecruitmentRequisitionFormFormRawValue['rrfNumber']>;
  preferredEducationType: FormControl<RecruitmentRequisitionFormFormRawValue['preferredEducationType']>;
  dateOfRequisition: FormControl<RecruitmentRequisitionFormFormRawValue['dateOfRequisition']>;
  requestedDate: FormControl<RecruitmentRequisitionFormFormRawValue['requestedDate']>;
  recommendationDate01: FormControl<RecruitmentRequisitionFormFormRawValue['recommendationDate01']>;
  recommendationDate02: FormControl<RecruitmentRequisitionFormFormRawValue['recommendationDate02']>;
  recommendationDate03: FormControl<RecruitmentRequisitionFormFormRawValue['recommendationDate03']>;
  recommendationDate04: FormControl<RecruitmentRequisitionFormFormRawValue['recommendationDate04']>;
  requisitionStatus: FormControl<RecruitmentRequisitionFormFormRawValue['requisitionStatus']>;
  rejectedAt: FormControl<RecruitmentRequisitionFormFormRawValue['rejectedAt']>;
  createdAt: FormControl<RecruitmentRequisitionFormFormRawValue['createdAt']>;
  updatedAt: FormControl<RecruitmentRequisitionFormFormRawValue['updatedAt']>;
  isDeleted: FormControl<RecruitmentRequisitionFormFormRawValue['isDeleted']>;
  totalOnboard: FormControl<RecruitmentRequisitionFormFormRawValue['totalOnboard']>;
  preferredSkillType: FormControl<RecruitmentRequisitionFormFormRawValue['preferredSkillType']>;
  recruitmentNature: FormControl<RecruitmentRequisitionFormFormRawValue['recruitmentNature']>;
  specialRequirement: FormControl<RecruitmentRequisitionFormFormRawValue['specialRequirement']>;
  recommendationDate05: FormControl<RecruitmentRequisitionFormFormRawValue['recommendationDate05']>;
  functionalDesignationId: FormControl<RecruitmentRequisitionFormFormRawValue['functionalDesignationId']>;
  bandId: FormControl<RecruitmentRequisitionFormFormRawValue['bandId']>;
  departmentId: FormControl<RecruitmentRequisitionFormFormRawValue['departmentId']>;
  unitId: FormControl<RecruitmentRequisitionFormFormRawValue['unitId']>;
  recommendedBy01Id: FormControl<RecruitmentRequisitionFormFormRawValue['recommendedBy01Id']>;
  recommendedBy02Id: FormControl<RecruitmentRequisitionFormFormRawValue['recommendedBy02Id']>;
  recommendedBy03Id: FormControl<RecruitmentRequisitionFormFormRawValue['recommendedBy03Id']>;
  recommendedBy04Id: FormControl<RecruitmentRequisitionFormFormRawValue['recommendedBy04Id']>;
  requesterId: FormControl<RecruitmentRequisitionFormFormRawValue['requesterId']>;
  rejectedById: FormControl<RecruitmentRequisitionFormFormRawValue['rejectedById']>;
  createdById: FormControl<RecruitmentRequisitionFormFormRawValue['createdById']>;
  updatedById: FormControl<RecruitmentRequisitionFormFormRawValue['updatedById']>;
  deletedById: FormControl<RecruitmentRequisitionFormFormRawValue['deletedById']>;
  recommendedBy05Id: FormControl<RecruitmentRequisitionFormFormRawValue['recommendedBy05Id']>;
  employeeToBeReplacedId: FormControl<RecruitmentRequisitionFormFormRawValue['employeeToBeReplacedId']>;
};

export type RecruitmentRequisitionFormFormGroup = FormGroup<RecruitmentRequisitionFormFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecruitmentRequisitionFormFormService {
  createRecruitmentRequisitionFormFormGroup(
    recruitmentRequisitionForm: RecruitmentRequisitionFormFormGroupInput = { id: null }
  ): RecruitmentRequisitionFormFormGroup {
    const recruitmentRequisitionFormRawValue = this.convertRecruitmentRequisitionFormToRecruitmentRequisitionFormRawValue({
      ...this.getFormDefaults(),
      ...recruitmentRequisitionForm,
    });
    return new FormGroup<RecruitmentRequisitionFormFormGroupContent>({
      id: new FormControl(
        { value: recruitmentRequisitionFormRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      expectedJoiningDate: new FormControl(recruitmentRequisitionFormRawValue.expectedJoiningDate, {
        validators: [Validators.required],
      }),
      project: new FormControl(recruitmentRequisitionFormRawValue.project, {
        validators: [Validators.minLength(0), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      numberOfVacancies: new FormControl(recruitmentRequisitionFormRawValue.numberOfVacancies, {
        validators: [Validators.required, Validators.min(1), Validators.max(1000)],
      }),
      employmentType: new FormControl(recruitmentRequisitionFormRawValue.employmentType, {
        validators: [Validators.required],
      }),
      resourceType: new FormControl(recruitmentRequisitionFormRawValue.resourceType, {
        validators: [Validators.required],
      }),
      rrfNumber: new FormControl(recruitmentRequisitionFormRawValue.rrfNumber),
      preferredEducationType: new FormControl(recruitmentRequisitionFormRawValue.preferredEducationType, {
        validators: [Validators.minLength(2), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      dateOfRequisition: new FormControl(recruitmentRequisitionFormRawValue.dateOfRequisition),
      requestedDate: new FormControl(recruitmentRequisitionFormRawValue.requestedDate),
      recommendationDate01: new FormControl(recruitmentRequisitionFormRawValue.recommendationDate01),
      recommendationDate02: new FormControl(recruitmentRequisitionFormRawValue.recommendationDate02),
      recommendationDate03: new FormControl(recruitmentRequisitionFormRawValue.recommendationDate03),
      recommendationDate04: new FormControl(recruitmentRequisitionFormRawValue.recommendationDate04),
      requisitionStatus: new FormControl(recruitmentRequisitionFormRawValue.requisitionStatus, {
        validators: [Validators.required],
      }),
      rejectedAt: new FormControl(recruitmentRequisitionFormRawValue.rejectedAt),
      createdAt: new FormControl(recruitmentRequisitionFormRawValue.createdAt),
      updatedAt: new FormControl(recruitmentRequisitionFormRawValue.updatedAt),
      isDeleted: new FormControl(recruitmentRequisitionFormRawValue.isDeleted),
      totalOnboard: new FormControl(recruitmentRequisitionFormRawValue.totalOnboard),
      preferredSkillType: new FormControl(recruitmentRequisitionFormRawValue.preferredSkillType, {
        validators: [Validators.minLength(2), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      recruitmentNature: new FormControl(recruitmentRequisitionFormRawValue.recruitmentNature),
      specialRequirement: new FormControl(recruitmentRequisitionFormRawValue.specialRequirement, {
        validators: [Validators.minLength(2), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      recommendationDate05: new FormControl(recruitmentRequisitionFormRawValue.recommendationDate05),
      functionalDesignationId: new FormControl(recruitmentRequisitionFormRawValue.functionalDesignationId, {
        validators: [Validators.required],
      }),
      bandId: new FormControl(recruitmentRequisitionFormRawValue.bandId, {
        validators: [Validators.required],
      }),
      departmentId: new FormControl(recruitmentRequisitionFormRawValue.departmentId),
      unitId: new FormControl(recruitmentRequisitionFormRawValue.unitId),
      recommendedBy01Id: new FormControl(recruitmentRequisitionFormRawValue.recommendedBy01Id),
      recommendedBy02Id: new FormControl(recruitmentRequisitionFormRawValue.recommendedBy02Id),
      recommendedBy03Id: new FormControl(recruitmentRequisitionFormRawValue.recommendedBy03Id),
      recommendedBy04Id: new FormControl(recruitmentRequisitionFormRawValue.recommendedBy04Id),
      requesterId: new FormControl(recruitmentRequisitionFormRawValue.requesterId),
      rejectedById: new FormControl(recruitmentRequisitionFormRawValue.rejectedById),
      createdById: new FormControl(recruitmentRequisitionFormRawValue.createdById),
      updatedById: new FormControl(recruitmentRequisitionFormRawValue.updatedById),
      deletedById: new FormControl(recruitmentRequisitionFormRawValue.deletedById),
      recommendedBy05Id: new FormControl(recruitmentRequisitionFormRawValue.recommendedBy05Id),
      employeeToBeReplacedId: new FormControl(recruitmentRequisitionFormRawValue.employeeToBeReplacedId),
    });
  }

  getRecruitmentRequisitionForm(form: RecruitmentRequisitionFormFormGroup): IRecruitmentRequisitionForm | NewRecruitmentRequisitionForm {
    return this.convertRecruitmentRequisitionFormRawValueToRecruitmentRequisitionForm(
      form.getRawValue() as RecruitmentRequisitionFormFormRawValue | NewRecruitmentRequisitionFormFormRawValue
    );
  }

  resetForm(form: RecruitmentRequisitionFormFormGroup, recruitmentRequisitionForm: RecruitmentRequisitionFormFormGroupInput): void {
    const recruitmentRequisitionFormRawValue = this.convertRecruitmentRequisitionFormToRecruitmentRequisitionFormRawValue({
      ...this.getFormDefaults(),
      ...recruitmentRequisitionForm,
    });
    form.reset(
      {
        ...recruitmentRequisitionFormRawValue,
        id: { value: recruitmentRequisitionFormRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RecruitmentRequisitionFormFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
      isDeleted: false,
    };
  }

  private convertRecruitmentRequisitionFormRawValueToRecruitmentRequisitionForm(
    rawRecruitmentRequisitionForm: RecruitmentRequisitionFormFormRawValue | NewRecruitmentRequisitionFormFormRawValue
  ): IRecruitmentRequisitionForm | NewRecruitmentRequisitionForm {
    return {
      ...rawRecruitmentRequisitionForm,
      createdAt: dayjs(rawRecruitmentRequisitionForm.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawRecruitmentRequisitionForm.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertRecruitmentRequisitionFormToRecruitmentRequisitionFormRawValue(
    recruitmentRequisitionForm:
      | IRecruitmentRequisitionForm
      | (Partial<NewRecruitmentRequisitionForm> & RecruitmentRequisitionFormFormDefaults)
  ): RecruitmentRequisitionFormFormRawValue | PartialWithRequiredKeyOf<NewRecruitmentRequisitionFormFormRawValue> {
    return {
      ...recruitmentRequisitionForm,
      createdAt: recruitmentRequisitionForm.createdAt ? recruitmentRequisitionForm.createdAt.toString() : undefined,
      updatedAt: recruitmentRequisitionForm.updatedAt ? recruitmentRequisitionForm.updatedAt.toString() : undefined,
    };
  }
}
