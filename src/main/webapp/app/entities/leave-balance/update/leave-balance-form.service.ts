import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILeaveBalance, NewLeaveBalance } from '../leave-balance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILeaveBalance for edit and NewLeaveBalanceFormGroupInput for create.
 */
type LeaveBalanceFormGroupInput = ILeaveBalance | PartialWithRequiredKeyOf<NewLeaveBalance>;

type LeaveBalanceFormDefaults = Pick<NewLeaveBalance, 'id'>;

type LeaveBalanceFormGroupContent = {
  id: FormControl<ILeaveBalance['id'] | NewLeaveBalance['id']>;
  leaveType: FormControl<ILeaveBalance['leaveType']>;
  openingBalance: FormControl<ILeaveBalance['openingBalance']>;
  closingBalance: FormControl<ILeaveBalance['closingBalance']>;
  consumedDuringYear: FormControl<ILeaveBalance['consumedDuringYear']>;
  year: FormControl<ILeaveBalance['year']>;
  amount: FormControl<ILeaveBalance['amount']>;
  leaveAmountType: FormControl<ILeaveBalance['leaveAmountType']>;
  employeeId: FormControl<ILeaveBalance['employeeId']>
  /*employee: FormControl<ILeaveBalance['employee']>;*/
};

export type LeaveBalanceFormGroup = FormGroup<LeaveBalanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LeaveBalanceFormService {
  createLeaveBalanceFormGroup(leaveBalance: LeaveBalanceFormGroupInput = { id: null }): LeaveBalanceFormGroup {
    const leaveBalanceRawValue = {
      ...this.getFormDefaults(),
      ...leaveBalance,
    };
    return new FormGroup<LeaveBalanceFormGroupContent>({
      id: new FormControl(
        { value: leaveBalanceRawValue.id, disabled: true },
        {nonNullable: true, validators: [Validators.required],}
      ),
      leaveType: new FormControl(leaveBalanceRawValue.leaveType,
        [Validators.required]),
      openingBalance: new FormControl(leaveBalanceRawValue.openingBalance),
      /*closingBalance: new FormControl(leaveBalanceRawValue.closingBalance),*/
     /* consumedDuringYear: new FormControl(leaveBalanceRawValue.consumedDuringYear),*/
      closingBalance: new FormControl(0),
      consumedDuringYear:new FormControl(0),
      year: new FormControl(leaveBalanceRawValue.year,
        [Validators.required, Validators.min(1900), Validators.max(2199)]
        ),
      amount: new FormControl(leaveBalanceRawValue.amount,
        [Validators.required, Validators.min(0), Validators.max(366)]),
      leaveAmountType: new FormControl(leaveBalanceRawValue.leaveAmountType, [Validators.required]),
      /*employee: new FormControl(leaveBalanceRawValue.employee),*/
      employeeId: new FormControl(leaveBalanceRawValue.employeeId,  [Validators.required]),
    });
  }

  getLeaveBalance(form: LeaveBalanceFormGroup): ILeaveBalance | NewLeaveBalance {
    return form.getRawValue() as ILeaveBalance | NewLeaveBalance;
  }

  resetForm(form: LeaveBalanceFormGroup, leaveBalance: LeaveBalanceFormGroupInput): void {
    const leaveBalanceRawValue = { ...this.getFormDefaults(), ...leaveBalance };
    form.reset(
      {
        ...leaveBalanceRawValue,
        id: { value: leaveBalanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LeaveBalanceFormDefaults {
    return {
      id: null,
    };
  }
}
