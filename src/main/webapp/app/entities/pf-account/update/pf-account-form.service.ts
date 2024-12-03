import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPfAccount, NewPfAccount } from '../pf-account.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPfAccount for edit and NewPfAccountFormGroupInput for create.
 */
type PfAccountFormGroupInput = IPfAccount | PartialWithRequiredKeyOf<NewPfAccount>;

type PfAccountFormDefaults = Pick<NewPfAccount, 'id'>;

type PfAccountFormGroupContent = {
  id: FormControl<IPfAccount['id'] | NewPfAccount['id']>;
  pfCode: FormControl<IPfAccount['pfCode']>;
  membershipStartDate: FormControl<IPfAccount['membershipStartDate']>;
  membershipEndDate: FormControl<IPfAccount['membershipEndDate']>;
  status: FormControl<IPfAccount['status']>;
  designationName: FormControl<IPfAccount['designationName']>;
  departmentName: FormControl<IPfAccount['departmentName']>;
  unitName: FormControl<IPfAccount['unitName']>;
  accHolderName: FormControl<IPfAccount['accHolderName']>;
  pin: FormControl<IPfAccount['pin']>;
  dateOfJoining: FormControl<IPfAccount['dateOfJoining']>;
  dateOfConfirmation: FormControl<IPfAccount['dateOfConfirmation']>;
};

export type PfAccountFormGroup = FormGroup<PfAccountFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PfAccountFormService {
  createPfAccountFormGroup(pfAccount: PfAccountFormGroupInput = { id: null }): PfAccountFormGroup {
    const pfAccountRawValue = {
      ...this.getFormDefaults(),
      ...pfAccount,
    };
    return new FormGroup<PfAccountFormGroupContent>({
      id: new FormControl(
        { value: pfAccountRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      pfCode: new FormControl(pfAccountRawValue.pfCode, {
        validators: [Validators.required, Validators.minLength(4), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      membershipStartDate: new FormControl(pfAccountRawValue.membershipStartDate, { validators: [Validators.required] }),
      membershipEndDate: new FormControl(pfAccountRawValue.membershipEndDate),
      status: new FormControl(pfAccountRawValue.status),
      designationName: new FormControl(pfAccountRawValue.designationName, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      departmentName: new FormControl(pfAccountRawValue.departmentName, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      unitName: new FormControl(pfAccountRawValue.unitName, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      accHolderName: new FormControl(pfAccountRawValue.accHolderName, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      pin: new FormControl(pfAccountRawValue.pin, {
        validators: [Validators.required, Validators.minLength(4), Validators.maxLength(250), CustomValidator.numberValidator()],
      }),
      dateOfJoining: new FormControl(pfAccountRawValue.dateOfJoining, { validators: [Validators.required] }),
      dateOfConfirmation: new FormControl(pfAccountRawValue.dateOfConfirmation, { validators: [Validators.required] }),
    });
  }

  getPfAccount(form: PfAccountFormGroup): IPfAccount | NewPfAccount {
    return form.getRawValue() as IPfAccount | NewPfAccount;
  }

  resetForm(form: PfAccountFormGroup, pfAccount: PfAccountFormGroupInput): void {
    const pfAccountRawValue = { ...this.getFormDefaults(), ...pfAccount };
    form.reset(
      {
        ...pfAccountRawValue,
        id: { value: pfAccountRawValue.id, disabled: true },
      } as any // cast to workaround https://github.com/angular/angular/issues/46458 /
    );
  }

  private getFormDefaults(): PfAccountFormDefaults {
    return {
      id: null,
    };
  }
}

