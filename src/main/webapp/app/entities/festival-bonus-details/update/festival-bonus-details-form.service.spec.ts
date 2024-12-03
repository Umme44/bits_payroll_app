import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../festival-bonus-details.test-samples';

import { FestivalBonusDetailsFormService } from './festival-bonus-details-form.service';

describe('FestivalBonusDetails Form Service', () => {
  let service: FestivalBonusDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FestivalBonusDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createFestivalBonusDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFestivalBonusDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bonusAmount: expect.any(Object),
            remarks: expect.any(Object),
            isHold: expect.any(Object),
            basic: expect.any(Object),
            gross: expect.any(Object),
            employee: expect.any(Object),
            festival: expect.any(Object),
          })
        );
      });

      it('passing IFestivalBonusDetails should create a new form with FormGroup', () => {
        const formGroup = service.createFestivalBonusDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bonusAmount: expect.any(Object),
            remarks: expect.any(Object),
            isHold: expect.any(Object),
            basic: expect.any(Object),
            gross: expect.any(Object),
            employee: expect.any(Object),
            festival: expect.any(Object),
          })
        );
      });
    });

    describe('getFestivalBonusDetails', () => {
      it('should return NewFestivalBonusDetails for default FestivalBonusDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFestivalBonusDetailsFormGroup(sampleWithNewData);

        const festivalBonusDetails = service.getFestivalBonusDetails(formGroup) as any;

        expect(festivalBonusDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewFestivalBonusDetails for empty FestivalBonusDetails initial value', () => {
        const formGroup = service.createFestivalBonusDetailsFormGroup();

        const festivalBonusDetails = service.getFestivalBonusDetails(formGroup) as any;

        expect(festivalBonusDetails).toMatchObject({});
      });

      it('should return IFestivalBonusDetails', () => {
        const formGroup = service.createFestivalBonusDetailsFormGroup(sampleWithRequiredData);

        const festivalBonusDetails = service.getFestivalBonusDetails(formGroup) as any;

        expect(festivalBonusDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFestivalBonusDetails should not enable id FormControl', () => {
        const formGroup = service.createFestivalBonusDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFestivalBonusDetails should disable id FormControl', () => {
        const formGroup = service.createFestivalBonusDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
