import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IArrearSalaryMaster, NewArrearSalaryMaster } from '../arrear-salary-master.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArrearSalaryMaster for edit and NewArrearSalaryMasterFormGroupInput for create.
 */
type ArrearSalaryMasterFormGroupInput = IArrearSalaryMaster | PartialWithRequiredKeyOf<NewArrearSalaryMaster>;

type ArrearSalaryMasterFormDefaults = Pick<NewArrearSalaryMaster, 'id' | 'isLocked' | 'isDeleted'>;

type ArrearSalaryMasterFormGroupContent = {
  id: FormControl<IArrearSalaryMaster['id'] | NewArrearSalaryMaster['id']>;
  title: FormControl<IArrearSalaryMaster['title']>;
  isLocked: FormControl<IArrearSalaryMaster['isLocked']>;
  isDeleted: FormControl<IArrearSalaryMaster['isDeleted']>;
};

export type ArrearSalaryMasterFormGroup = FormGroup<ArrearSalaryMasterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArrearSalaryMasterFormService {
  createArrearSalaryMasterFormGroup(arrearSalaryMaster: ArrearSalaryMasterFormGroupInput = { id: null }): ArrearSalaryMasterFormGroup {
    const arrearSalaryMasterRawValue = {
      ...this.getFormDefaults(),
      ...arrearSalaryMaster,
    };
    return new FormGroup<ArrearSalaryMasterFormGroupContent>({
      id: new FormControl(
        { value: arrearSalaryMasterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(arrearSalaryMasterRawValue.title, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(250)],
      }),
      isLocked: new FormControl(arrearSalaryMasterRawValue.isLocked, {
        validators: [Validators.required],
      }),
      isDeleted: new FormControl(arrearSalaryMasterRawValue.isDeleted, {
        validators: [Validators.required],
      }),
    });
  }

  getArrearSalaryMaster(form: ArrearSalaryMasterFormGroup): IArrearSalaryMaster | NewArrearSalaryMaster {
    return form.getRawValue() as IArrearSalaryMaster | NewArrearSalaryMaster;
  }

  resetForm(form: ArrearSalaryMasterFormGroup, arrearSalaryMaster: ArrearSalaryMasterFormGroupInput): void {
    const arrearSalaryMasterRawValue = { ...this.getFormDefaults(), ...arrearSalaryMaster };
    form.reset(
      {
        ...arrearSalaryMasterRawValue,
        id: { value: arrearSalaryMasterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ArrearSalaryMasterFormDefaults {
    return {
      id: null,
      isLocked: false,
      isDeleted: false,
    };
  }
}
