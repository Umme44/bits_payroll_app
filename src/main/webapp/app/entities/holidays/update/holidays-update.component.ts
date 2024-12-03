import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { HolidaysFormGroup, HolidaysFormService } from './holidays-form.service';
import { IHolidays } from '../holidays.model';
import { HolidaysService } from '../service/holidays.service';
import { HolidayType } from 'app/entities/enumerations/holiday-type.model';

@Component({
  selector: 'jhi-holidays-update',
  templateUrl: './holidays-update.component.html',
})
export class HolidaysUpdateComponent implements OnInit {
  isSaving = false;
  holidays: IHolidays | null = null;
  holidayTypeValues = Object.keys(HolidayType);

  editForm: HolidaysFormGroup = this.holidaysFormService.createHolidaysFormGroup();

  isInvalid = false;

  constructor(
    protected holidaysService: HolidaysService,
    protected holidaysFormService: HolidaysFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ holidays }) => {
      this.holidays = holidays;
      if (holidays) {
        this.updateForm(holidays);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const holidays = this.holidaysFormService.getHolidays(this.editForm);
    if (holidays.id !== null) {
      this.subscribeToSaveResponse(this.holidaysService.update(holidays));
    } else {
      this.subscribeToSaveResponse(this.holidaysService.create(holidays as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHolidays>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(holidays: IHolidays): void {
    this.holidays = holidays;
    this.holidaysFormService.resetForm(this.editForm, holidays);
  }

  checkDate(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;

    if (startDate && endDate && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }
}
