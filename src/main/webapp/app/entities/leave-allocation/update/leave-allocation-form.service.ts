import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILeaveAllocation, NewLeaveAllocation } from '../leave-allocation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILeaveAllocation for edit and NewLeaveAllocationFormGroupInput for create.
 */
type LeaveAllocationFormGroupInput = ILeaveAllocation | PartialWithRequiredKeyOf<NewLeaveAllocation>;

type LeaveAllocationFormDefaults = Pick<NewLeaveAllocation, 'id'>;

type LeaveAllocationFormGroupContent = {
  id: FormControl<ILeaveAllocation['id'] | NewLeaveAllocation['id']>;
  year: FormControl<ILeaveAllocation['year']>;
  leaveType: FormControl<ILeaveAllocation['leaveType']>;
  allocatedDays: FormControl<ILeaveAllocation['allocatedDays']>;
};

export type LeaveAllocationFormGroup = FormGroup<LeaveAllocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LeaveAllocationFormService {
  createLeaveAllocationFormGroup(leaveAllocation: LeaveAllocationFormGroupInput = { id: null }): LeaveAllocationFormGroup {
    const leaveAllocationRawValue = {
      ...this.getFormDefaults(),
      ...leaveAllocation,
    };
    return new FormGroup<LeaveAllocationFormGroupContent>({
      id: new FormControl(
        { value: leaveAllocationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      year: new FormControl(leaveAllocationRawValue.year, {
        validators: [Validators.required, Validators.min(1900), Validators.max(2199)],
      }),
      leaveType: new FormControl(leaveAllocationRawValue.leaveType,{validators: [Validators.required]}),
      allocatedDays: new FormControl(leaveAllocationRawValue.allocatedDays,
        {
        validators: [Validators.required, Validators.min(0), Validators.max(365)],
      }
      ),
    });
  }

  getLeaveAllocation(form: LeaveAllocationFormGroup): ILeaveAllocation | NewLeaveAllocation {
    return form.getRawValue() as ILeaveAllocation | NewLeaveAllocation;
  }

  resetForm(form: LeaveAllocationFormGroup, leaveAllocation: LeaveAllocationFormGroupInput): void {
    const leaveAllocationRawValue = { ...this.getFormDefaults(), ...leaveAllocation };
    form.reset(
      {
        ...leaveAllocationRawValue,
        id: { value: leaveAllocationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): LeaveAllocationFormDefaults {
    return {
      id: null,
    };
  }
}
