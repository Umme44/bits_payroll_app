import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../holidays.test-samples';

import { HolidaysFormService } from './holidays-form.service';

describe('Holidays Form Service', () => {
  let service: HolidaysFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HolidaysFormService);
  });

  describe('Service methods', () => {
    describe('createHolidaysFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHolidaysFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            holidayType: expect.any(Object),
            description: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            isMoonDependent: expect.any(Object),
          })
        );
      });

      it('passing IHolidays should create a new form with FormGroup', () => {
        const formGroup = service.createHolidaysFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            holidayType: expect.any(Object),
            description: expect.any(Object),
            startDate: expect.any(Object),
            endDate: expect.any(Object),
            isMoonDependent: expect.any(Object),
          })
        );
      });
    });

    describe('getHolidays', () => {
      it('should return NewHolidays for default Holidays initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createHolidaysFormGroup(sampleWithNewData);

        const holidays = service.getHolidays(formGroup) as any;

        expect(holidays).toMatchObject(sampleWithNewData);
      });

      it('should return NewHolidays for empty Holidays initial value', () => {
        const formGroup = service.createHolidaysFormGroup();

        const holidays = service.getHolidays(formGroup) as any;

        expect(holidays).toMatchObject({});
      });

      it('should return IHolidays', () => {
        const formGroup = service.createHolidaysFormGroup(sampleWithRequiredData);

        const holidays = service.getHolidays(formGroup) as any;

        expect(holidays).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHolidays should not enable id FormControl', () => {
        const formGroup = service.createHolidaysFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHolidays should disable id FormControl', () => {
        const formGroup = service.createHolidaysFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
