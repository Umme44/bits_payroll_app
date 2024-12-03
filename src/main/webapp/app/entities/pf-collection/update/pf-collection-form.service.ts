import {Injectable} from '@angular/core';
import {FormGroup, FormControl, Validators} from '@angular/forms';

import {IPfCollection, NewPfCollection} from '../pf-collection.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPfCollection for edit and NewPfCollectionFormGroupInput for create.
 */
type PfCollectionFormGroupInput = IPfCollection | PartialWithRequiredKeyOf<NewPfCollection>;

type PfCollectionFormDefaults = Pick<NewPfCollection, 'id'>;

type PfCollectionFormGroupContent = {
  id: FormControl<IPfCollection['id'] | NewPfCollection['id']>;
  employeeContribution: FormControl<IPfCollection['employeeContribution']>;
  employerContribution: FormControl<IPfCollection['employerContribution']>;
  transactionDate: FormControl<IPfCollection['transactionDate']>;
  year: FormControl<IPfCollection['year']>;
  month: FormControl<IPfCollection['month']>;
  collectionType: FormControl<IPfCollection['collectionType']>;
  employeeInterest: FormControl<IPfCollection['employeeInterest']>;
  employerInterest: FormControl<IPfCollection['employerInterest']>;
  gross: FormControl<IPfCollection['gross']>;
  basic: FormControl<IPfCollection['basic']>;
  // pfAccount: FormControl<IPfCollection['pfAccount']>;

  pfAccountId: FormControl<IPfCollection['pfAccountId']>;
};

export type PfCollectionFormGroup = FormGroup<PfCollectionFormGroupContent>;

@Injectable({providedIn: 'root'})
export class PfCollectionFormService {
  createPfCollectionFormGroup(pfCollection: PfCollectionFormGroupInput = {id: null}): PfCollectionFormGroup {
    const pfCollectionRawValue = {
      ...this.getFormDefaults(),
      ...pfCollection,
    };
    return new FormGroup<PfCollectionFormGroupContent>({
      id: new FormControl(
        {value: pfCollectionRawValue.id, disabled: true},
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      employeeContribution: new FormControl(pfCollectionRawValue.employeeContribution, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      employerContribution: new FormControl(pfCollectionRawValue.employerContribution, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      transactionDate: new FormControl(pfCollectionRawValue.transactionDate),
      year: new FormControl(pfCollectionRawValue.year, [Validators.required, Validators.min(1950), Validators.max(2099)]),
      // year: new FormControl(pfCollectionRawValue.year, [
      //   Validators.required,
      //   Validators.min(1950),
      //   Validators.max(2099)
      // ]),
      month: new FormControl(pfCollectionRawValue.month, [Validators.required]),
      collectionType: new FormControl(pfCollectionRawValue.collectionType, [Validators.required]),
      employeeInterest: new FormControl(pfCollectionRawValue.employeeInterest, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      employerInterest: new FormControl(pfCollectionRawValue.employerInterest, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      gross: new FormControl(pfCollectionRawValue.gross, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      basic: new FormControl(pfCollectionRawValue.basic, {
        validators: [Validators.min(0), Validators.max(10000000)],
      }),
      // pfAccount: new FormControl(pfCollectionRawValue.pfAccount),
      pfAccountId: new FormControl(pfCollectionRawValue.pfAccountId),
    });
  }

  getPfCollection(form: PfCollectionFormGroup): IPfCollection | NewPfCollection {
    return form.getRawValue() as IPfCollection | NewPfCollection;
  }

  resetForm(form: PfCollectionFormGroup, pfCollection: PfCollectionFormGroupInput): void {
    const pfCollectionRawValue = {...this.getFormDefaults(), ...pfCollection};
    form.reset(
      {
        ...pfCollectionRawValue,
        id: {value: pfCollectionRawValue.id, disabled: true},
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PfCollectionFormDefaults {
    return {
      id: null,
    };
  }
}
