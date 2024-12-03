import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FestivalBonusDetailsFormGroup, FestivalBonusDetailsFormService } from './festival-bonus-details-form.service';
import { IFestivalBonusDetails } from '../festival-bonus-details.model';
import { FestivalBonusDetailsService } from '../service/festival-bonus-details.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IFestival } from 'app/entities/festival/festival.model';
import { FestivalService } from 'app/entities/festival/service/festival.service';

@Component({
  selector: 'jhi-festival-bonus-details-update',
  templateUrl: './festival-bonus-details-update.component.html',
})
export class FestivalBonusDetailsUpdateComponent implements OnInit {
  isSaving = false;
  festivalBonusDetails: IFestivalBonusDetails | null = null;

  employeesSharedCollection: IEmployee[] = [];
  festivalsSharedCollection: IFestival[] = [];

  editForm: FestivalBonusDetailsFormGroup = this.festivalBonusDetailsFormService.createFestivalBonusDetailsFormGroup();

  constructor(
    protected festivalBonusDetailsService: FestivalBonusDetailsService,
    protected festivalBonusDetailsFormService: FestivalBonusDetailsFormService,
    protected employeeService: EmployeeService,
    protected festivalService: FestivalService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareFestival = (o1: IFestival | null, o2: IFestival | null): boolean => this.festivalService.compareFestival(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ festivalBonusDetails }) => {
      this.festivalBonusDetails = festivalBonusDetails;
      if (festivalBonusDetails) {
        this.updateForm(festivalBonusDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const festivalBonusDetails = this.festivalBonusDetailsFormService.getFestivalBonusDetails(this.editForm);
    if (festivalBonusDetails.id !== null) {
      this.subscribeToSaveResponse(this.festivalBonusDetailsService.update(festivalBonusDetails));
    } else {
      this.subscribeToSaveResponse(this.festivalBonusDetailsService.create(festivalBonusDetails as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFestivalBonusDetails>>): void {
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

  protected updateForm(festivalBonusDetails: IFestivalBonusDetails): void {
    this.festivalBonusDetails = festivalBonusDetails;
    this.festivalBonusDetailsFormService.resetForm(this.editForm, festivalBonusDetails);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      festivalBonusDetails.employee
    );
    this.festivalsSharedCollection = this.festivalService.addFestivalToCollectionIfMissing<IFestival>(
      this.festivalsSharedCollection,
      festivalBonusDetails.festival
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.festivalBonusDetails?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.festivalService
      .query()
      .pipe(map((res: HttpResponse<IFestival[]>) => res.body ?? []))
      .pipe(
        map((festivals: IFestival[]) =>
          this.festivalService.addFestivalToCollectionIfMissing<IFestival>(festivals, this.festivalBonusDetails?.festival)
        )
      )
      .subscribe((festivals: IFestival[]) => (this.festivalsSharedCollection = festivals));
  }
}
