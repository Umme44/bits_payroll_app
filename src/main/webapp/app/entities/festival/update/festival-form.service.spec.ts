import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../festival.test-samples';

import { FestivalFormService } from './festival-form.service';

describe('Festival Form Service', () => {
  let service: FestivalFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FestivalFormService);
  });

  describe('Service methods', () => {
    describe('createFestivalFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFestivalFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            festivalName: expect.any(Object),
            festivalDate: expect.any(Object),
            bonusDisbursementDate: expect.any(Object),
            religion: expect.any(Object),
            isProRata: expect.any(Object),
          })
        );
      });

      it('passing IFestival should create a new form with FormGroup', () => {
        const formGroup = service.createFestivalFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            festivalName: expect.any(Object),
            festivalDate: expect.any(Object),
            bonusDisbursementDate: expect.any(Object),
            religion: expect.any(Object),
            isProRata: expect.any(Object),
          })
        );
      });
    });

    describe('getFestival', () => {
      it('should return NewFestival for default Festival initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFestivalFormGroup(sampleWithNewData);

        const festival = service.getFestival(formGroup) as any;

        expect(festival).toMatchObject(sampleWithNewData);
      });

      it('should return NewFestival for empty Festival initial value', () => {
        const formGroup = service.createFestivalFormGroup();

        const festival = service.getFestival(formGroup) as any;

        expect(festival).toMatchObject({});
      });

      it('should return IFestival', () => {
        const formGroup = service.createFestivalFormGroup(sampleWithRequiredData);

        const festival = service.getFestival(formGroup) as any;

        expect(festival).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFestival should not enable id FormControl', () => {
        const formGroup = service.createFestivalFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFestival should disable id FormControl', () => {
        const formGroup = service.createFestivalFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
