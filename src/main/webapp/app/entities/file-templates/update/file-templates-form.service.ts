import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFileTemplates, NewFileTemplates } from '../file-templates.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFileTemplates for edit and NewFileTemplatesFormGroupInput for create.
 */
type FileTemplatesFormGroupInput = IFileTemplates | PartialWithRequiredKeyOf<NewFileTemplates>;

type FileTemplatesFormDefaults = Pick<NewFileTemplates, 'id' | 'isActive'>;

type FileTemplatesFormGroupContent = {
  id: FormControl<IFileTemplates['id'] | NewFileTemplates['id']>;
  title: FormControl<IFileTemplates['title']>;
  filePath: FormControl<IFileTemplates['filePath']>;
  file: FormControl<IFileTemplates['file']>;
  fileContentType: FormControl<IFileTemplates['fileContentType']>;
  type: FormControl<IFileTemplates['type']>;
  accessPrivilege: FormControl<IFileTemplates['accessPrivilege']>;
  isActive: FormControl<IFileTemplates['isActive']>;
};

export type FileTemplatesFormGroup = FormGroup<FileTemplatesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FileTemplatesFormService {
  createFileTemplatesFormGroup(fileTemplates: FileTemplatesFormGroupInput = { id: null }): FileTemplatesFormGroup {
    const fileTemplatesRawValue = {
      ...this.getFormDefaults(),
      ...fileTemplates,
    };
    return new FormGroup<FileTemplatesFormGroupContent>({
      id: new FormControl(
        { value: fileTemplatesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(fileTemplatesRawValue.title, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(250), CustomValidator.naturalTextValidator()],
      }),
      filePath: new FormControl(fileTemplatesRawValue.filePath),
      file: new FormControl(fileTemplatesRawValue.file, {
        validators: [Validators.required],
      }),
      fileContentType: new FormControl(fileTemplatesRawValue.fileContentType),
      type: new FormControl(fileTemplatesRawValue.type, {validators : Validators.required}),
      accessPrivilege: new FormControl(fileTemplatesRawValue.accessPrivilege, {validators : Validators.required}),
      isActive: new FormControl(fileTemplatesRawValue.isActive, {
        validators: [Validators.required],
      }),
    });
  }

  getFileTemplates(form: FileTemplatesFormGroup): IFileTemplates | NewFileTemplates {
    return form.getRawValue() as IFileTemplates | NewFileTemplates;
  }

  resetForm(form: FileTemplatesFormGroup, fileTemplates: FileTemplatesFormGroupInput): void {
    const fileTemplatesRawValue = { ...this.getFormDefaults(), ...fileTemplates };
    form.reset(
      {
        ...fileTemplatesRawValue,
        id: { value: fileTemplatesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FileTemplatesFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
