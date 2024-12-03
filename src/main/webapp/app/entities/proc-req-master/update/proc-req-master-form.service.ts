import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProcReqMaster, NewProcReqMaster } from '../proc-req-master.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcReqMaster for edit and NewProcReqMasterFormGroupInput for create.
 */
type ProcReqMasterFormGroupInput = IProcReqMaster | PartialWithRequiredKeyOf<NewProcReqMaster>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProcReqMaster | NewProcReqMaster> = Omit<
  T,
  | 'recommendationAt01'
  | 'recommendationAt02'
  | 'recommendationAt03'
  | 'recommendationAt04'
  | 'recommendationAt05'
  | 'closedAt'
  | 'createdAt'
  | 'updatedAt'
> & {
  recommendationAt01?: string | null;
  recommendationAt02?: string | null;
  recommendationAt03?: string | null;
  recommendationAt04?: string | null;
  recommendationAt05?: string | null;
  closedAt?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
};

type ProcReqMasterFormRawValue = FormValueOf<IProcReqMaster>;

type NewProcReqMasterFormRawValue = FormValueOf<NewProcReqMaster>;

type ProcReqMasterFormDefaults = Pick<
  NewProcReqMaster,
  | 'id'
  | 'isCTOApprovalRequired'
  | 'recommendationAt01'
  | 'recommendationAt02'
  | 'recommendationAt03'
  | 'recommendationAt04'
  | 'recommendationAt05'
  | 'closedAt'
  | 'createdAt'
  | 'updatedAt'
>;

type ProcReqMasterFormGroupContent = {
  id: FormControl<ProcReqMasterFormRawValue['id'] | NewProcReqMaster['id']>;
  requisitionNo: FormControl<ProcReqMasterFormRawValue['requisitionNo']>;
  requestedDate: FormControl<ProcReqMasterFormRawValue['requestedDate']>;
  isCTOApprovalRequired: FormControl<ProcReqMasterFormRawValue['isCTOApprovalRequired']>;
  requisitionStatus: FormControl<ProcReqMasterFormRawValue['requisitionStatus']>;
  expectedReceivedDate: FormControl<ProcReqMasterFormRawValue['expectedReceivedDate']>;
  reasoning: FormControl<ProcReqMasterFormRawValue['reasoning']>;
  totalApproximatePrice: FormControl<ProcReqMasterFormRawValue['totalApproximatePrice']>;
  /* recommendationAt01: FormControl<ProcReqMasterFormRawValue['recommendationAt01']>;
  recommendationAt02: FormControl<ProcReqMasterFormRawValue['recommendationAt02']>;
  recommendationAt03: FormControl<ProcReqMasterFormRawValue['recommendationAt03']>;
  recommendationAt04: FormControl<ProcReqMasterFormRawValue['recommendationAt04']>;
  recommendationAt05: FormControl<ProcReqMasterFormRawValue['recommendationAt05']>;
  nextRecommendationOrder: FormControl<ProcReqMasterFormRawValue['nextRecommendationOrder']>;
  rejectedDate: FormControl<ProcReqMasterFormRawValue['rejectedDate']>;
  rejectionReason: FormControl<ProcReqMasterFormRawValue['rejectionReason']>;*/
  closedAt: FormControl<ProcReqMasterFormRawValue['closedAt']>;
  createdAt: FormControl<ProcReqMasterFormRawValue['createdAt']>;
  updatedAt: FormControl<ProcReqMasterFormRawValue['updatedAt']>;
  departmentId: FormControl<ProcReqMasterFormRawValue['departmentId']>;
  requestedById: FormControl<ProcReqMasterFormRawValue['requestedById']>;
  /*recommendedBy01: FormControl<ProcReqMasterFormRawValue['recommendedBy01']>;
  recommendedBy02: FormControl<ProcReqMasterFormRawValue['recommendedBy02']>;
  recommendedBy03: FormControl<ProcReqMasterFormRawValue['recommendedBy03']>;
  recommendedBy04: FormControl<ProcReqMasterFormRawValue['recommendedBy04']>;
  recommendedBy05: FormControl<ProcReqMasterFormRawValue['recommendedBy05']>;
  nextApprovalFrom: FormControl<ProcReqMasterFormRawValue['nextApprovalFrom']>;
  rejectedBy: FormControl<ProcReqMasterFormRawValue['rejectedBy']>;*/
  closedById: FormControl<ProcReqMasterFormRawValue['closedById']>;
  updatedById: FormControl<ProcReqMasterFormRawValue['updatedById']>;
  createdById: FormControl<ProcReqMasterFormRawValue['createdById']>;
};

export type ProcReqMasterFormGroup = FormGroup<ProcReqMasterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProcReqMasterFormService {
  createProcReqMasterFormGroup(procReqMaster: ProcReqMasterFormGroupInput = { id: null }): ProcReqMasterFormGroup {
    const procReqMasterRawValue = this.convertProcReqMasterToProcReqMasterRawValue({
      ...this.getFormDefaults(),
      ...procReqMaster,
    });
    return new FormGroup<ProcReqMasterFormGroupContent>({
      id: new FormControl(
        { value: procReqMasterRawValue.id, disabled: true },
        {
          nonNullable: true,
        }
      ),
      requisitionNo: new FormControl(procReqMasterRawValue.requisitionNo, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      requestedDate: new FormControl(procReqMasterRawValue.requestedDate, {
        validators: [Validators.required],
      }),
      isCTOApprovalRequired: new FormControl(procReqMasterRawValue.isCTOApprovalRequired),
      requisitionStatus: new FormControl(procReqMasterRawValue.requisitionStatus, {
        validators: [Validators.required],
      }),
      expectedReceivedDate: new FormControl(procReqMasterRawValue.expectedReceivedDate),
      reasoning: new FormControl(procReqMasterRawValue.reasoning),
      totalApproximatePrice: new FormControl(procReqMasterRawValue.totalApproximatePrice, {
        validators: [Validators.min(1)],
      }),
      /*recommendationAt01: new FormControl(procReqMasterRawValue.recommendationAt01),
      recommendationAt02: new FormControl(procReqMasterRawValue.recommendationAt02),
      recommendationAt03: new FormControl(procReqMasterRawValue.recommendationAt03),
      recommendationAt04: new FormControl(procReqMasterRawValue.recommendationAt04),
      recommendationAt05: new FormControl(procReqMasterRawValue.recommendationAt05),
      nextRecommendationOrder: new FormControl(procReqMasterRawValue.nextRecommendationOrder),
      rejectedDate: new FormControl(procReqMasterRawValue.rejectedDate),
      rejectionReason: new FormControl(procReqMasterRawValue.rejectionReason),
     */
      closedAt: new FormControl(procReqMasterRawValue.closedAt),
      createdAt: new FormControl(procReqMasterRawValue.createdAt),
      updatedAt: new FormControl(procReqMasterRawValue.updatedAt),
      departmentId: new FormControl(procReqMasterRawValue.departmentId, {
        validators: [Validators.required],
      }),
      requestedById: new FormControl(procReqMasterRawValue.requestedById, {
        validators: [Validators.required],
      }),
      /* recommendedBy01: new FormControl(procReqMasterRawValue.recommendedBy01),
      recommendedBy02: new FormControl(procReqMasterRawValue.recommendedBy02),
      recommendedBy03: new FormControl(procReqMasterRawValue.recommendedBy03),
      recommendedBy04: new FormControl(procReqMasterRawValue.recommendedBy04),
      recommendedBy05: new FormControl(procReqMasterRawValue.recommendedBy05),
      nextApprovalFrom: new FormControl(procReqMasterRawValue.nextApprovalFrom),
      rejectedBy: new FormControl(procReqMasterRawValue.rejectedBy),*/
      closedById: new FormControl(procReqMasterRawValue.closedById),
      updatedById: new FormControl(procReqMasterRawValue.updatedById),
      createdById: new FormControl(procReqMasterRawValue.createdById),
    });
  }

  getProcReqMaster(form: ProcReqMasterFormGroup): IProcReqMaster | NewProcReqMaster {
    return this.convertProcReqMasterRawValueToProcReqMaster(form.getRawValue() as ProcReqMasterFormRawValue | NewProcReqMasterFormRawValue);
  }

  resetForm(form: ProcReqMasterFormGroup, procReqMaster: ProcReqMasterFormGroupInput): void {
    const procReqMasterRawValue = this.convertProcReqMasterToProcReqMasterRawValue({ ...this.getFormDefaults(), ...procReqMaster });
    form.reset(
      {
        ...procReqMasterRawValue,
        id: { value: procReqMasterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProcReqMasterFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isCTOApprovalRequired: false,
      recommendationAt01: currentTime,
      recommendationAt02: currentTime,
      recommendationAt03: currentTime,
      recommendationAt04: currentTime,
      recommendationAt05: currentTime,
      closedAt: currentTime,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertProcReqMasterRawValueToProcReqMaster(
    rawProcReqMaster: ProcReqMasterFormRawValue | NewProcReqMasterFormRawValue
  ): IProcReqMaster | NewProcReqMaster {
    return {
      ...rawProcReqMaster,
      recommendationAt01: dayjs(rawProcReqMaster.recommendationAt01, DATE_TIME_FORMAT),
      recommendationAt02: dayjs(rawProcReqMaster.recommendationAt02, DATE_TIME_FORMAT),
      recommendationAt03: dayjs(rawProcReqMaster.recommendationAt03, DATE_TIME_FORMAT),
      recommendationAt04: dayjs(rawProcReqMaster.recommendationAt04, DATE_TIME_FORMAT),
      recommendationAt05: dayjs(rawProcReqMaster.recommendationAt05, DATE_TIME_FORMAT),
      closedAt: dayjs(rawProcReqMaster.closedAt, DATE_TIME_FORMAT),
      createdAt: dayjs(rawProcReqMaster.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawProcReqMaster.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertProcReqMasterToProcReqMasterRawValue(
    procReqMaster: IProcReqMaster | (Partial<NewProcReqMaster> & ProcReqMasterFormDefaults)
  ): ProcReqMasterFormRawValue | PartialWithRequiredKeyOf<NewProcReqMasterFormRawValue> {
    return {
      ...procReqMaster,
      recommendationAt01: procReqMaster.recommendationAt01 ? procReqMaster.recommendationAt01.format(DATE_TIME_FORMAT) : undefined,
      recommendationAt02: procReqMaster.recommendationAt02 ? procReqMaster.recommendationAt02.format(DATE_TIME_FORMAT) : undefined,
      recommendationAt03: procReqMaster.recommendationAt03 ? procReqMaster.recommendationAt03.format(DATE_TIME_FORMAT) : undefined,
      recommendationAt04: procReqMaster.recommendationAt04 ? procReqMaster.recommendationAt04.format(DATE_TIME_FORMAT) : undefined,
      recommendationAt05: procReqMaster.recommendationAt05 ? procReqMaster.recommendationAt05.format(DATE_TIME_FORMAT) : undefined,
      closedAt: procReqMaster.closedAt ? procReqMaster.closedAt.format(DATE_TIME_FORMAT) : undefined,
      createdAt: procReqMaster.createdAt ? procReqMaster.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: procReqMaster.updatedAt ? procReqMaster.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
