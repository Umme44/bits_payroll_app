import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IIndividualArrearSalary, NewIndividualArrearSalary } from '../individual-arrear-salary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIndividualArrearSalary for edit and NewIndividualArrearSalaryFormGroupInput for create.
 */
type IndividualArrearSalaryFormGroupInput = IIndividualArrearSalary | PartialWithRequiredKeyOf<NewIndividualArrearSalary>;

type IndividualArrearSalaryFormDefaults = Pick<NewIndividualArrearSalary, 'id'>;

type IndividualArrearSalaryFormGroupContent = {
  id: FormControl<IIndividualArrearSalary['id'] | NewIndividualArrearSalary['id']>;
  effectiveDate: FormControl<IIndividualArrearSalary['effectiveDate']>;
  existingBand: FormControl<IIndividualArrearSalary['existingBand']>;
  newBand: FormControl<IIndividualArrearSalary['newBand']>;
  existingGross: FormControl<IIndividualArrearSalary['existingGross']>;
  newGross: FormControl<IIndividualArrearSalary['newGross']>;
  increment: FormControl<IIndividualArrearSalary['increment']>;
  arrearSalary: FormControl<IIndividualArrearSalary['arrearSalary']>;
  arrearPfDeduction: FormControl<IIndividualArrearSalary['arrearPfDeduction']>;
  taxDeduction: FormControl<IIndividualArrearSalary['taxDeduction']>;
  netPay: FormControl<IIndividualArrearSalary['netPay']>;
  pfContribution: FormControl<IIndividualArrearSalary['pfContribution']>;
  title: FormControl<IIndividualArrearSalary['title']>;
  titleEffectiveFrom: FormControl<IIndividualArrearSalary['titleEffectiveFrom']>;
  arrearRemarks: FormControl<IIndividualArrearSalary['arrearRemarks']>;
  festivalBonus: FormControl<IIndividualArrearSalary['festivalBonus']>;
  employeeId: FormControl<IIndividualArrearSalary['employeeId']>;
};

export type IndividualArrearSalaryFormGroup = FormGroup<IndividualArrearSalaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IndividualArrearSalaryFormService {
  createIndividualArrearSalaryFormGroup(
    individualArrearSalary: IndividualArrearSalaryFormGroupInput = { id: null }
  ): IndividualArrearSalaryFormGroup {
    const individualArrearSalaryRawValue = {
      ...this.getFormDefaults(),
      ...individualArrearSalary,
    };
    return new FormGroup<IndividualArrearSalaryFormGroupContent>({
      id: new FormControl(
        { value: individualArrearSalaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      effectiveDate: new FormControl(individualArrearSalaryRawValue.effectiveDate),
      existingBand: new FormControl(individualArrearSalaryRawValue.existingBand),
      newBand: new FormControl(individualArrearSalaryRawValue.newBand),
      existingGross: new FormControl(individualArrearSalaryRawValue.existingGross),
      newGross: new FormControl(individualArrearSalaryRawValue.newGross),
      increment: new FormControl(individualArrearSalaryRawValue.increment),
      arrearSalary: new FormControl(individualArrearSalaryRawValue.arrearSalary),
      arrearPfDeduction: new FormControl(individualArrearSalaryRawValue.arrearPfDeduction),
      taxDeduction: new FormControl(individualArrearSalaryRawValue.taxDeduction),
      netPay: new FormControl(individualArrearSalaryRawValue.netPay),
      pfContribution: new FormControl(individualArrearSalaryRawValue.pfContribution),
      title: new FormControl(individualArrearSalaryRawValue.title),
      titleEffectiveFrom: new FormControl(individualArrearSalaryRawValue.titleEffectiveFrom),
      arrearRemarks: new FormControl(individualArrearSalaryRawValue.arrearRemarks),
      festivalBonus: new FormControl(individualArrearSalaryRawValue.festivalBonus),
      employeeId: new FormControl(individualArrearSalaryRawValue.employeeId),
    });
  }

  getIndividualArrearSalary(form: IndividualArrearSalaryFormGroup): IIndividualArrearSalary | NewIndividualArrearSalary {
    return form.getRawValue() as IIndividualArrearSalary | NewIndividualArrearSalary;
  }

  resetForm(form: IndividualArrearSalaryFormGroup, individualArrearSalary: IndividualArrearSalaryFormGroupInput): void {
    const individualArrearSalaryRawValue = { ...this.getFormDefaults(), ...individualArrearSalary };
    form.reset(
      {
        ...individualArrearSalaryRawValue,
        id: { value: individualArrearSalaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IndividualArrearSalaryFormDefaults {
    return {
      id: null,
    };
  }
}
