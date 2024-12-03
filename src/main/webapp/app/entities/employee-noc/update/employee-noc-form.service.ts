import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployeeNOC, NewEmployeeNOC } from '../employee-noc.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeNOC for edit and NewEmployeeNOCFormGroupInput for create.
 */
type EmployeeNOCFormGroupInput = IEmployeeNOC | PartialWithRequiredKeyOf<NewEmployeeNOC>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployeeNOC | NewEmployeeNOC> = Omit<T, 'createdAt' | 'updatedAt' | 'generatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  generatedAt?: string | null;
};

type EmployeeNOCFormRawValue = FormValueOf<IEmployeeNOC>;

type NewEmployeeNOCFormRawValue = FormValueOf<NewEmployeeNOC>;

type EmployeeNOCFormDefaults = Pick<NewEmployeeNOC, 'id' | 'createdAt' | 'updatedAt' | 'generatedAt' | 'isRequiredForVisa'>;

type EmployeeNOCFormGroupContent = {
  id: FormControl<EmployeeNOCFormRawValue['id'] | NewEmployeeNOC['id']>;
  passportNumber: FormControl<EmployeeNOCFormRawValue['passportNumber']>;
  leaveStartDate: FormControl<EmployeeNOCFormRawValue['leaveStartDate']>;
  leaveEndDate: FormControl<EmployeeNOCFormRawValue['leaveEndDate']>;
  countryToVisit: FormControl<EmployeeNOCFormRawValue['countryToVisit']>;
  referenceNumber: FormControl<EmployeeNOCFormRawValue['referenceNumber']>;
  issueDate: FormControl<EmployeeNOCFormRawValue['issueDate']>;
  createdAt: FormControl<EmployeeNOCFormRawValue['createdAt']>;
  updatedAt: FormControl<EmployeeNOCFormRawValue['updatedAt']>;
  generatedAt: FormControl<EmployeeNOCFormRawValue['generatedAt']>;
  reason: FormControl<EmployeeNOCFormRawValue['reason']>;
  purposeOfNOC: FormControl<EmployeeNOCFormRawValue['purposeOfNOC']>;
  certificateStatus: FormControl<EmployeeNOCFormRawValue['certificateStatus']>;
  isRequiredForVisa: FormControl<EmployeeNOCFormRawValue['isRequiredForVisa']>;
  employee: FormControl<EmployeeNOCFormRawValue['employee']>;
  signatoryPerson: FormControl<EmployeeNOCFormRawValue['signatoryPerson']>;
  createdBy: FormControl<EmployeeNOCFormRawValue['createdBy']>;
  updatedBy: FormControl<EmployeeNOCFormRawValue['updatedBy']>;
  generatedBy: FormControl<EmployeeNOCFormRawValue['generatedBy']>;
};

export type EmployeeNOCFormGroup = FormGroup<EmployeeNOCFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeNOCFormService {
  createEmployeeNOCFormGroup(employeeNOC: EmployeeNOCFormGroupInput = { id: null }): EmployeeNOCFormGroup {
    const employeeNOCRawValue = this.convertEmployeeNOCToEmployeeNOCRawValue({
      ...this.getFormDefaults(),
      ...employeeNOC,
    });
    return new FormGroup<EmployeeNOCFormGroupContent>({
      id: new FormControl(
        { value: employeeNOCRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      passportNumber: new FormControl(employeeNOCRawValue.passportNumber, {
        validators: [Validators.required],
      }),
      leaveStartDate: new FormControl(employeeNOCRawValue.leaveStartDate, {
        validators: [Validators.required],
      }),
      leaveEndDate: new FormControl(employeeNOCRawValue.leaveEndDate, {
        validators: [Validators.required],
      }),
      countryToVisit: new FormControl(employeeNOCRawValue.countryToVisit, {
        validators: [Validators.required],
      }),
      referenceNumber: new FormControl(employeeNOCRawValue.referenceNumber),
      issueDate: new FormControl(employeeNOCRawValue.issueDate),
      createdAt: new FormControl(employeeNOCRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(employeeNOCRawValue.updatedAt),
      generatedAt: new FormControl(employeeNOCRawValue.generatedAt),
      reason: new FormControl(employeeNOCRawValue.reason),
      purposeOfNOC: new FormControl(employeeNOCRawValue.purposeOfNOC, {
        validators: [Validators.required],
      }),
      certificateStatus: new FormControl(employeeNOCRawValue.certificateStatus, {
        validators: [Validators.required],
      }),
      isRequiredForVisa: new FormControl(employeeNOCRawValue.isRequiredForVisa),
      employee: new FormControl(employeeNOCRawValue.employee, {
        validators: [Validators.required],
      }),
      signatoryPerson: new FormControl(employeeNOCRawValue.signatoryPerson),
      createdBy: new FormControl(employeeNOCRawValue.createdBy, {
        validators: [Validators.required],
      }),
      updatedBy: new FormControl(employeeNOCRawValue.updatedBy),
      generatedBy: new FormControl(employeeNOCRawValue.generatedBy),
    });
  }

  getEmployeeNOC(form: EmployeeNOCFormGroup): IEmployeeNOC | NewEmployeeNOC {
    return this.convertEmployeeNOCRawValueToEmployeeNOC(form.getRawValue() as EmployeeNOCFormRawValue | NewEmployeeNOCFormRawValue);
  }

  resetForm(form: EmployeeNOCFormGroup, employeeNOC: EmployeeNOCFormGroupInput): void {
    const employeeNOCRawValue = this.convertEmployeeNOCToEmployeeNOCRawValue({ ...this.getFormDefaults(), ...employeeNOC });
    form.reset(
      {
        ...employeeNOCRawValue,
        id: { value: employeeNOCRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeNOCFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
      generatedAt: currentTime,
      isRequiredForVisa: false,
    };
  }

  private convertEmployeeNOCRawValueToEmployeeNOC(
    rawEmployeeNOC: EmployeeNOCFormRawValue | NewEmployeeNOCFormRawValue
  ): IEmployeeNOC | NewEmployeeNOC {
    return {
      ...rawEmployeeNOC,
      createdAt: dayjs(rawEmployeeNOC.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawEmployeeNOC.updatedAt, DATE_TIME_FORMAT),
      generatedAt: dayjs(rawEmployeeNOC.generatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeeNOCToEmployeeNOCRawValue(
    employeeNOC: IEmployeeNOC | (Partial<NewEmployeeNOC> & EmployeeNOCFormDefaults)
  ): EmployeeNOCFormRawValue | PartialWithRequiredKeyOf<NewEmployeeNOCFormRawValue> {
    return {
      ...employeeNOC,
      createdAt: employeeNOC.createdAt ? employeeNOC.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: employeeNOC.updatedAt ? employeeNOC.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      generatedAt: employeeNOC.generatedAt ? employeeNOC.generatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
