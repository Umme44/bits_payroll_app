import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { IProcReqMaster, NewProcReqMaster } from '../../proc-req-master/proc-req-master.model';
import dayjs from 'dayjs/esm';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcReq for edit and NewProcReqFormGroupInput for create.
 */
type ProcReqFormGroupInput = IProcReqMaster | PartialWithRequiredKeyOf<NewProcReqMaster>;

type ProcReqFormDefaults = Pick<NewProcReqMaster, 'id' | 'createdAt' | 'updatedAt' | 'requestedDate'>;

type ProcReqFormGroupContent = {
  id: FormControl<IProcReqMaster['id'] | NewProcReqMaster['id']>;
  requisitionNo: FormControl<IProcReqMaster['requisitionNo']>;
  requestedDate: FormControl<IProcReqMaster['requestedDate']>;
  isCTOApprovalRequired: FormControl<IProcReqMaster['isCTOApprovalRequired']>;
  requisitionStatus: FormControl<IProcReqMaster['requisitionStatus']>;
  expectedReceivedDate: FormControl<IProcReqMaster['expectedReceivedDate']>;
  reasoning: FormControl<IProcReqMaster['reasoning']>;
  totalApproximatePrice: FormControl<IProcReqMaster['totalApproximatePrice']>;
  closedAt: FormControl<IProcReqMaster['closedAt']>;
  createdAt: FormControl<IProcReqMaster['createdAt']>;
  updatedAt: FormControl<IProcReqMaster['updatedAt']>;
  departmentId: FormControl<IProcReqMaster['departmentId']>;
  requestedById: FormControl<IProcReqMaster['requestedById']>;
  updatedById: FormControl<IProcReqMaster['updatedById']>;
  createdById: FormControl<IProcReqMaster['createdById']>;
  closedById: FormControl<IProcReqMaster['closedById']>;
};

export type ProcReqFormGroup = FormGroup<ProcReqFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProcReqMasterFormService {
  createProcReqFormGroup(procReq: ProcReqFormGroupInput = { id: null }): ProcReqFormGroup {
    const procReqRawValue = {
      ...this.getFormDefaults(),
      ...procReq,
    };
    return new FormGroup<ProcReqFormGroupContent>({
      id: new FormControl(
        { value: procReqRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      requisitionNo: new FormControl(procReqRawValue.requisitionNo),
      requestedDate: new FormControl(procReqRawValue.requestedDate, {
        validators: [Validators.required],
      }),
      isCTOApprovalRequired: new FormControl(procReqRawValue.isCTOApprovalRequired),
      requisitionStatus: new FormControl(procReqRawValue.requisitionStatus),
      expectedReceivedDate: new FormControl(procReqRawValue.expectedReceivedDate),
      reasoning: new FormControl(procReqRawValue.reasoning, {
        validators: [Validators.required],
      }),
      totalApproximatePrice: new FormControl(procReqRawValue.totalApproximatePrice, {
        validators: [Validators.required, Validators.min(1)],
      }),
      closedAt: new FormControl(procReqRawValue.closedAt),
      createdAt: new FormControl(procReqRawValue.createdAt, {
        validators: [Validators.required, Validators.min(1)],
      }),
      updatedAt: new FormControl(procReqRawValue.updatedAt),
      departmentId: new FormControl(procReqRawValue.departmentId, {
        validators: [Validators.required, Validators.min(1)],
      }),
      requestedById: new FormControl(procReqRawValue.requestedById),
      updatedById: new FormControl(procReqRawValue.updatedById),
      createdById: new FormControl(procReqRawValue.createdById),
      closedById: new FormControl(procReqRawValue.closedById),
    });
  }

  getProcReq(form: ProcReqFormGroup): IProcReqMaster | NewProcReqMaster {
    return form.getRawValue() as IProcReqMaster | NewProcReqMaster;
  }

  resetForm(form: ProcReqFormGroup, procReq: ProcReqFormGroupInput): void {
    const procReqRawValue = { ...this.getFormDefaults(), ...procReq };
    form.reset(
      {
        ...procReqRawValue,
        id: { value: procReqRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProcReqFormDefaults {
    const today = dayjs().startOf('day');

    return {
      id: null,
      createdAt: today,
      updatedAt: today,
      requestedDate: today,
    };
  }
}
