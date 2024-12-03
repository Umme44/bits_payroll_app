import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployeeDocument, NewEmployeeDocument } from '../employee-document.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeDocument for edit and NewEmployeeDocumentFormGroupInput for create.
 */
type EmployeeDocumentFormGroupInput = IEmployeeDocument | PartialWithRequiredKeyOf<NewEmployeeDocument>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployeeDocument | NewEmployeeDocument> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type EmployeeDocumentFormRawValue = FormValueOf<IEmployeeDocument>;

type NewEmployeeDocumentFormRawValue = FormValueOf<NewEmployeeDocument>;

type EmployeeDocumentFormDefaults = Pick<NewEmployeeDocument, 'id' | 'hasEmployeeVisibility' | 'createdAt' | 'updatedAt'>;

type EmployeeDocumentFormGroupContent = {
  id: FormControl<EmployeeDocumentFormRawValue['id'] | NewEmployeeDocument['id']>;
  pin: FormControl<EmployeeDocumentFormRawValue['pin']>;
  fileName: FormControl<EmployeeDocumentFormRawValue['fileName']>;
  filePath: FormControl<EmployeeDocumentFormRawValue['filePath']>;
  hasEmployeeVisibility: FormControl<EmployeeDocumentFormRawValue['hasEmployeeVisibility']>;
  remarks: FormControl<EmployeeDocumentFormRawValue['remarks']>;
  file: FormControl<EmployeeDocumentFormRawValue['file']>;
  createdBy: FormControl<EmployeeDocumentFormRawValue['createdBy']>;
  createdAt: FormControl<EmployeeDocumentFormRawValue['createdAt']>;
  updatedBy: FormControl<EmployeeDocumentFormRawValue['updatedBy']>;
  updatedAt: FormControl<EmployeeDocumentFormRawValue['updatedAt']>;
};

export type EmployeeDocumentFormGroup = FormGroup<EmployeeDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeDocumentFormService {
  createEmployeeDocumentFormGroup(employeeDocument: EmployeeDocumentFormGroupInput = { id: null }): EmployeeDocumentFormGroup {
    const employeeDocumentRawValue = this.convertEmployeeDocumentToEmployeeDocumentRawValue({
      ...this.getFormDefaults(),
      ...employeeDocument,
    });
    return new FormGroup<EmployeeDocumentFormGroupContent>({
      id: new FormControl(
        { value: employeeDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      pin: new FormControl(employeeDocumentRawValue.pin, {
        validators: [Validators.required],
      }),
      fileName: new FormControl(employeeDocumentRawValue.fileName, {
        validators: [Validators.required, Validators.minLength(4), Validators.maxLength(100), CustomValidator.naturalTextValidator()]
      }),
      filePath: new FormControl(employeeDocumentRawValue.filePath),
      hasEmployeeVisibility: new FormControl(employeeDocumentRawValue.hasEmployeeVisibility),
      remarks: new FormControl(employeeDocumentRawValue.remarks, {
        validators: [Validators.required, Validators.minLength(1), Validators.maxLength(250), CustomValidator.naturalTextValidator()]
      }),
      file: new FormControl(employeeDocumentRawValue.file, {
        validators: [Validators.required],
      }),

      createdBy : new FormControl(employeeDocumentRawValue.createdBy),
      createdAt : new FormControl(employeeDocumentRawValue.createdAt),
      updatedBy : new FormControl(employeeDocumentRawValue.updatedBy),
      updatedAt : new FormControl(employeeDocumentRawValue.updatedAt),
    });
  }

  getEmployeeDocument(form: EmployeeDocumentFormGroup): IEmployeeDocument | NewEmployeeDocument {
    return this.convertEmployeeDocumentRawValueToEmployeeDocument(
      form.getRawValue() as EmployeeDocumentFormRawValue | NewEmployeeDocumentFormRawValue
    );
  }

  resetForm(form: EmployeeDocumentFormGroup, employeeDocument: EmployeeDocumentFormGroupInput): void {
    const employeeDocumentRawValue = this.convertEmployeeDocumentToEmployeeDocumentRawValue({
      ...this.getFormDefaults(),
      ...employeeDocument,
    });
    form.reset(
      {
        ...employeeDocumentRawValue,
        id: { value: employeeDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeDocumentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      hasEmployeeVisibility: false,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertEmployeeDocumentRawValueToEmployeeDocument(
    rawEmployeeDocument: EmployeeDocumentFormRawValue | NewEmployeeDocumentFormRawValue
  ): IEmployeeDocument | NewEmployeeDocument {
    return {
      ...rawEmployeeDocument,
      createdAt: dayjs(rawEmployeeDocument.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawEmployeeDocument.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeeDocumentToEmployeeDocumentRawValue(
    employeeDocument: IEmployeeDocument | (Partial<NewEmployeeDocument> & EmployeeDocumentFormDefaults)
  ): EmployeeDocumentFormRawValue | PartialWithRequiredKeyOf<NewEmployeeDocumentFormRawValue> {
    // return {
    //   ...employeeDocument,
    //   createdAt: employeeDocument.createdAt ? employeeDocument.createdAt.format(DATE_TIME_FORMAT) : undefined,
    //   updatedAt: employeeDocument.updatedAt ? employeeDocument.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    // };
    return {
      ...employeeDocument,
      createdAt: employeeDocument.createdAt ? employeeDocument.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: employeeDocument.updatedAt ? "2023-12-13T06:03:00.331606326Z" : undefined,
    };
  }
}
