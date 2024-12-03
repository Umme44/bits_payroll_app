import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEmployeeStaticFile, NewEmployeeStaticFile } from '../employee-static-file.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployeeStaticFile for edit and NewEmployeeStaticFileFormGroupInput for create.
 */
type EmployeeStaticFileFormGroupInput = IEmployeeStaticFile | PartialWithRequiredKeyOf<NewEmployeeStaticFile>;

type EmployeeStaticFileFormDefaults = Pick<NewEmployeeStaticFile, 'id'>;

type EmployeeStaticFileFormGroupContent = {
  id: FormControl<IEmployeeStaticFile['id'] | NewEmployeeStaticFile['id']>;
  filePath: FormControl<IEmployeeStaticFile['filePath']>;
  employee: FormControl<IEmployeeStaticFile['employee']>;
};

export type EmployeeStaticFileFormGroup = FormGroup<EmployeeStaticFileFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeStaticFileFormService {
  createEmployeeStaticFileFormGroup(employeeStaticFile: EmployeeStaticFileFormGroupInput = { id: null }): EmployeeStaticFileFormGroup {
    const employeeStaticFileRawValue = {
      ...this.getFormDefaults(),
      ...employeeStaticFile,
    };
    return new FormGroup<EmployeeStaticFileFormGroupContent>({
      id: new FormControl(
        { value: employeeStaticFileRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      filePath: new FormControl(employeeStaticFileRawValue.filePath, {
        validators: [Validators.minLength(0), Validators.maxLength(255)],
      }),
      employee: new FormControl(employeeStaticFileRawValue.employee),
    });
  }

  getEmployeeStaticFile(form: EmployeeStaticFileFormGroup): IEmployeeStaticFile | NewEmployeeStaticFile {
    return form.getRawValue() as IEmployeeStaticFile | NewEmployeeStaticFile;
  }

  resetForm(form: EmployeeStaticFileFormGroup, employeeStaticFile: EmployeeStaticFileFormGroupInput): void {
    const employeeStaticFileRawValue = { ...this.getFormDefaults(), ...employeeStaticFile };
    form.reset(
      {
        ...employeeStaticFileRawValue,
        id: { value: employeeStaticFileRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EmployeeStaticFileFormDefaults {
    return {
      id: null,
    };
  }
}
