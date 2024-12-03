import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProcReq, NewProcReq } from '../proc-req.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProcReq for edit and NewProcReqFormGroupInput for create.
 */
type ProcReqFormGroupInput = IProcReq | PartialWithRequiredKeyOf<NewProcReq>;

type ProcReqFormDefaults = Pick<NewProcReq, 'id'>;

type ProcReqFormGroupContent = {
  id: FormControl<IProcReq['id'] | NewProcReq['id']>;
  quantity: FormControl<IProcReq['quantity']>;
  referenceFilePath: FormControl<IProcReq['referenceFilePath']>;
  referenceFileData: FormControl<IProcReq['referenceFileData']>;
  itemInformationCode: FormControl<IProcReq['itemInformationCode']>;
  itemInformationName: FormControl<IProcReq['itemInformationName']>;
  referenceFileDataContentType: FormControl<IProcReq['referenceFileDataContentType']>;
  itemInformationId: FormControl<IProcReq['itemInformationId']>;
  procReqMasterId: FormControl<IProcReq['procReqMasterId']>;
};

export type ProcReqFormGroup = FormGroup<ProcReqFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProcReqFormService {
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
      quantity: new FormControl(procReqRawValue.quantity, {
        validators: [Validators.required, Validators.min(1)],
      }),
      referenceFilePath: new FormControl(procReqRawValue.referenceFilePath),
      referenceFileData: new FormControl(procReqRawValue.referenceFileData),
      itemInformationCode: new FormControl(procReqRawValue.itemInformationCode),
      itemInformationName: new FormControl(procReqRawValue.itemInformationName),
      referenceFileDataContentType: new FormControl(procReqRawValue.referenceFileDataContentType),
      itemInformationId: new FormControl(procReqRawValue.itemInformationId, {
        validators: [Validators.required],
      }),
      procReqMasterId: new FormControl(procReqRawValue.procReqMasterId),
    });
  }

  getProcReq(form: ProcReqFormGroup): IProcReq | NewProcReq {
    return form.getRawValue() as IProcReq | NewProcReq;
  }

  resetForm(form: ProcReqFormGroup, procReq: ProcReqFormGroupInput): void {
    const procReqRawValue = { ...this.getFormDefaults(), ...procReq };
    form.reset({
      ...procReqRawValue,
      id: { value: procReqRawValue.id, disabled: true },
    } as any);
  }

  private getFormDefaults(): ProcReqFormDefaults {
    return {
      id: null,
    };
  }
}
