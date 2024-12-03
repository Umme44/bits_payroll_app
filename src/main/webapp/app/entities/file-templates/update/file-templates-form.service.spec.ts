import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../file-templates.test-samples';

import { FileTemplatesFormService } from './file-templates-form.service';

describe('FileTemplates Form Service', () => {
  let service: FileTemplatesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FileTemplatesFormService);
  });

  describe('Service methods', () => {
    describe('createFileTemplatesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFileTemplatesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            filePath: expect.any(Object),
            type: expect.any(Object),
            accessPrivilege: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IFileTemplates should create a new form with FormGroup', () => {
        const formGroup = service.createFileTemplatesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            filePath: expect.any(Object),
            type: expect.any(Object),
            accessPrivilege: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getFileTemplates', () => {
      it('should return NewFileTemplates for default FileTemplates initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFileTemplatesFormGroup(sampleWithNewData);

        const fileTemplates = service.getFileTemplates(formGroup) as any;

        expect(fileTemplates).toMatchObject(sampleWithNewData);
      });

      it('should return NewFileTemplates for empty FileTemplates initial value', () => {
        const formGroup = service.createFileTemplatesFormGroup();

        const fileTemplates = service.getFileTemplates(formGroup) as any;

        expect(fileTemplates).toMatchObject({});
      });

      it('should return IFileTemplates', () => {
        const formGroup = service.createFileTemplatesFormGroup(sampleWithRequiredData);

        const fileTemplates = service.getFileTemplates(formGroup) as any;

        expect(fileTemplates).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFileTemplates should not enable id FormControl', () => {
        const formGroup = service.createFileTemplatesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFileTemplates should disable id FormControl', () => {
        const formGroup = service.createFileTemplatesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
