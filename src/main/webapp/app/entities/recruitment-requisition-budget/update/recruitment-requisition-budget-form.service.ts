import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRecruitmentRequisitionBudget, NewRecruitmentRequisitionBudget } from '../recruitment-requisition-budget.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecruitmentRequisitionBudget for edit and NewRecruitmentRequisitionBudgetFormGroupInput for create.
 */
type RecruitmentRequisitionBudgetFormGroupInput = IRecruitmentRequisitionBudget | PartialWithRequiredKeyOf<NewRecruitmentRequisitionBudget>;

type RecruitmentRequisitionBudgetFormDefaults = Pick<NewRecruitmentRequisitionBudget, 'id'>;

type RecruitmentRequisitionBudgetFormGroupContent = {
  id: FormControl<IRecruitmentRequisitionBudget['id'] | NewRecruitmentRequisitionBudget['id']>;
  year: FormControl<IRecruitmentRequisitionBudget['year']>;
  budget: FormControl<IRecruitmentRequisitionBudget['budget']>;
  remainingBudget: FormControl<IRecruitmentRequisitionBudget['remainingBudget']>;
  remainingManpower: FormControl<IRecruitmentRequisitionBudget['remainingManpower']>;
  employeeId: FormControl<IRecruitmentRequisitionBudget['employeeId']>;
  departmentId: FormControl<IRecruitmentRequisitionBudget['departmentId']>;
};

export type RecruitmentRequisitionBudgetFormGroup = FormGroup<RecruitmentRequisitionBudgetFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecruitmentRequisitionBudgetFormService {
  createRecruitmentRequisitionBudgetFormGroup(
    recruitmentRequisitionBudget: RecruitmentRequisitionBudgetFormGroupInput = { id: null }
  ): RecruitmentRequisitionBudgetFormGroup {
    const recruitmentRequisitionBudgetRawValue = {
      ...this.getFormDefaults(),
      ...recruitmentRequisitionBudget,
    };
    return new FormGroup<RecruitmentRequisitionBudgetFormGroupContent>({
      id: new FormControl(
        { value: recruitmentRequisitionBudgetRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      year: new FormControl(recruitmentRequisitionBudgetRawValue.year, {
        validators: [Validators.required, Validators.min(1970), Validators.max(2070)],
      }),
      budget: new FormControl(recruitmentRequisitionBudgetRawValue.budget, {
        validators: [Validators.required],
      }),
      remainingBudget: new FormControl(recruitmentRequisitionBudgetRawValue.remainingBudget, {
        validators: [Validators.required],
      }),
      remainingManpower: new FormControl(recruitmentRequisitionBudgetRawValue.remainingManpower, {
        validators: [Validators.required],
      }),
      employeeId: new FormControl(recruitmentRequisitionBudgetRawValue.employeeId),
      departmentId: new FormControl(recruitmentRequisitionBudgetRawValue.departmentId),
    });
  }

  getRecruitmentRequisitionBudget(
    form: RecruitmentRequisitionBudgetFormGroup
  ): IRecruitmentRequisitionBudget | NewRecruitmentRequisitionBudget {
    return form.getRawValue() as IRecruitmentRequisitionBudget | NewRecruitmentRequisitionBudget;
  }

  resetForm(form: RecruitmentRequisitionBudgetFormGroup, recruitmentRequisitionBudget: RecruitmentRequisitionBudgetFormGroupInput): void {
    const recruitmentRequisitionBudgetRawValue = { ...this.getFormDefaults(), ...recruitmentRequisitionBudget };
    form.reset(
      {
        ...recruitmentRequisitionBudgetRawValue,
        id: { value: recruitmentRequisitionBudgetRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RecruitmentRequisitionBudgetFormDefaults {
    return {
      id: null,
    };
  }
}
