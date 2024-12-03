import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../office-notices.test-samples';

import { OfficeNoticesFormService } from './office-notices-form.service';

describe('OfficeNotices Form Service', () => {
  let service: OfficeNoticesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OfficeNoticesFormService);
  });

  describe('Service methods', () => {
    describe('createOfficeNoticesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOfficeNoticesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
            publishForm: expect.any(Object),
            publishTo: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });

      it('passing IOfficeNotices should create a new form with FormGroup', () => {
        const formGroup = service.createOfficeNoticesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            status: expect.any(Object),
            publishForm: expect.any(Object),
            publishTo: expect.any(Object),
            createdAt: expect.any(Object),
            updatedAt: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
          })
        );
      });
    });

    describe('getOfficeNotices', () => {
      it('should return NewOfficeNotices for default OfficeNotices initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOfficeNoticesFormGroup(sampleWithNewData);

        const officeNotices = service.getOfficeNotices(formGroup) as any;

        expect(officeNotices).toMatchObject(sampleWithNewData);
      });

      it('should return NewOfficeNotices for empty OfficeNotices initial value', () => {
        const formGroup = service.createOfficeNoticesFormGroup();

        const officeNotices = service.getOfficeNotices(formGroup) as any;

        expect(officeNotices).toMatchObject({});
      });

      it('should return IOfficeNotices', () => {
        const formGroup = service.createOfficeNoticesFormGroup(sampleWithRequiredData);

        const officeNotices = service.getOfficeNotices(formGroup) as any;

        expect(officeNotices).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOfficeNotices should not enable id FormControl', () => {
        const formGroup = service.createOfficeNoticesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOfficeNotices should disable id FormControl', () => {
        const formGroup = service.createOfficeNoticesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
