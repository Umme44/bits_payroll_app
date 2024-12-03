import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EducationDetailsFormGroup, EducationDetailsFormService } from './education-details-form.service';
import { IEducationDetails } from '../education-details.model';
import { EducationDetailsService } from '../service/education-details.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { IEmployee } from '../../employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-education-details-update',
  templateUrl: './education-details-update.component.html',
})
export class EducationDetailsUpdateComponent implements OnInit {
  isSaving = false;
  educationDetails: IEducationDetails | null = null;

  editForm: EducationDetailsFormGroup = this.educationDetailsFormService.createEducationDetailsFormGroup();

  constructor(
    protected educationDetailsService: EducationDetailsService,
    protected educationDetailsFormService: EducationDetailsFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ educationDetails }) => {
      this.educationDetails = educationDetails;
      if (educationDetails) {
        this.updateForm(educationDetails);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const educationDetails = this.educationDetailsFormService.getEducationDetails(this.editForm);
    if (educationDetails.id !== null) {
      this.subscribeToSaveResponse(this.educationDetailsService.update(educationDetails));
    } else {
      this.subscribeToSaveResponse(this.educationDetailsService.create(educationDetails as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEducationDetails>>): void {
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

  protected updateForm(educationDetails: IEducationDetails): void {
    this.educationDetails = educationDetails;
    this.educationDetailsFormService.resetForm(this.editForm, educationDetails);
  }

  changeEmployee(employee: IEmployee): void {
    const deptHeadFormControl = this.editForm.get(['employeeId'])!;
    deptHeadFormControl.markAsTouched();
    if (employee) {
      deptHeadFormControl.setValue(employee.id);
    } else {
      deptHeadFormControl.setValue(null);
    }
  }
}
