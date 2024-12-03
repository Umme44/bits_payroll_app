import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pf-arrear.test-samples';

import { PfArrearFormService } from './pf-arrear-form.service';

describe('PfArrear Form Service', () => {
  let service: PfArrearFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PfArrearFormService);
  });

  describe('Service methods', () => {
    describe('createPfArrearFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPfArrearFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            amount: expect.any(Object),
            remarks: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });

      it('passing IPfArrear should create a new form with FormGroup', () => {
        const formGroup = service.createPfArrearFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
            amount: expect.any(Object),
            remarks: expect.any(Object),
            employee: expect.any(Object),
          })
        );
      });
    });

    describe('getPfArrear', () => {
      it('should return NewPfArrear for default PfArrear initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPfArrearFormGroup(sampleWithNewData);

        const pfArrear = service.getPfArrear(formGroup) as any;

        expect(pfArrear).toMatchObject(sampleWithNewData);
      });

      it('should return NewPfArrear for empty PfArrear initial value', () => {
        const formGroup = service.createPfArrearFormGroup();

        const pfArrear = service.getPfArrear(formGroup) as any;

        expect(pfArrear).toMatchObject({});
      });

      it('should return IPfArrear', () => {
        const formGroup = service.createPfArrearFormGroup(sampleWithRequiredData);

        const pfArrear = service.getPfArrear(formGroup) as any;

        expect(pfArrear).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPfArrear should not enable id FormControl', () => {
        const formGroup = service.createPfArrearFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPfArrear should disable id FormControl', () => {
        const formGroup = service.createPfArrearFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
