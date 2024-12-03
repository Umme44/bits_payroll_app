import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DesignationFormGroup, DesignationFormService } from './designation-form.service';
import { IDesignation } from '../designation.model';
import { DesignationService } from '../service/designation.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-designation-update',
  templateUrl: './designation-update.component.html',
})
export class DesignationUpdateComponent implements OnInit {
  isSaving = false;
  designation: IDesignation | null = null;

  editForm: DesignationFormGroup = this.designationFormService.createDesignationFormGroup();

  constructor(
    protected designationService: DesignationService,
    protected designationFormService: DesignationFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ designation }) => {
      this.designation = designation;
      if (designation) {
        this.updateForm(designation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const designation = this.designationFormService.getDesignation(this.editForm);
    if (designation.id !== null) {
      this.subscribeToSaveResponse(this.designationService.update(designation));
    } else {
      this.subscribeToSaveResponse(this.designationService.create(designation as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDesignation>>): void {
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
    this.showErrorForInvalidInput()
  }
  showErrorForInvalidInput(){
    Swal.fire({
      icon: 'error',
      text: 'Invalid input',
      timer: 3500,
      showConfirmButton: false,
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(designation: IDesignation): void {
    this.designation = designation;
    this.designationFormService.resetForm(this.editForm, designation);
  }
}
