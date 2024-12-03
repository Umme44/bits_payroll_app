import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { swalOnRequestError, swalOnSavedSuccess, swalOnUpdatedSuccess } from 'app/shared/swal-common/swal-common';
import { IEmployee } from '../../employee-custom.model';
import { EducationDetailsService } from '../../../education-details/service/education-details.service';
import { EmployeeCustomService } from '../../service/employee-custom.service';
import { IEducationDetails } from '../../../education-details/education-details.model';
import { EducationDetailsFormService } from '../../../education-details/update/education-details-form.service';

@Component({
  selector: 'jhi-education-details-update-modal',
  templateUrl: './education-details-update.modal.component.html',
})
export class EducationDetailsUpdateModalComponent implements OnInit, OnChanges {
  @Input()
  idForUpdate = -1;

  @Output()
  idForUpdateChange = new EventEmitter<number>();

  @Input()
  employeeId!: number;

  isSaving = false;
  employees: IEmployee[] = [];
  //employeeId!: number;

  editForm = this.educationDetailsFormService.createEducationDetailsFormGroup();

  constructor(
    protected educationDetailsService: EducationDetailsService,
    protected employeeService: EmployeeCustomService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private educationDetailsFormService: EducationDetailsFormService
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (this.idForUpdate !== -1) {
      this.educationDetailsService.find(this.idForUpdate).subscribe(
        res => {
          this.updateForm(res.body!);
        },
        () => {
          swalOnRequestError();
        }
      );
    }
  }

  ngOnInit(): void {}

  updateForm(educationDetails: IEducationDetails): void {
    this.educationDetailsFormService.resetForm(this.editForm, educationDetails);
  }

  clear(): void {
    this.editForm.reset();
    //change to -1 for two-way data bind and activating ngOnChange
    this.idForUpdateChange.emit(-1);
  }

  save(): void {
    this.isSaving = true;
    const educationDetails = this.educationDetailsFormService.getEducationDetails(this.editForm);
    if (educationDetails.id !== null && educationDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.educationDetailsService.update(educationDetails));
    } else {
      this.subscribeToSaveResponse(this.educationDetailsService.create(educationDetails));
    }
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEducationDetails>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    if (!this.editForm.get('id')!.value) {
      swalOnSavedSuccess();
    } else {
      swalOnUpdatedSuccess();
    }
    this.editForm.reset();
    this.isSaving = false;
    this.clear();
  }

  protected onSaveError(): void {
    swalOnRequestError();
    this.isSaving = false;
  }
}
