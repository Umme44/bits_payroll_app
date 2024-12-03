import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { CalenderYearFormGroup, CalenderYearFormService } from './calender-year-form.service';
import { ICalenderYear } from '../calender-year.model';
import { CalenderYearService } from '../service/calender-year.service';

@Component({
  selector: 'jhi-calender-year-update',
  templateUrl: './calender-year-update.component.html',
})
export class CalenderYearUpdateComponent implements OnInit {
  isSaving = false;
  calenderYear: ICalenderYear | null = null;

  editForm: CalenderYearFormGroup = this.calenderYearFormService.createCalenderYearFormGroup();

  constructor(
    protected calenderYearService: CalenderYearService,
    protected calenderYearFormService: CalenderYearFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ calenderYear }) => {
      this.calenderYear = calenderYear;
      if (calenderYear) {
        this.updateForm(calenderYear);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const calenderYear = this.calenderYearFormService.getCalenderYear(this.editForm);
    if (calenderYear.id !== null) {
      this.subscribeToSaveResponse(this.calenderYearService.update(calenderYear));
    } else {
      this.subscribeToSaveResponse(this.calenderYearService.create(calenderYear as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICalenderYear>>): void {
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

  protected updateForm(calenderYear: ICalenderYear): void {
    this.calenderYear = calenderYear;
    this.calenderYearFormService.resetForm(this.editForm, calenderYear);
  }
}
