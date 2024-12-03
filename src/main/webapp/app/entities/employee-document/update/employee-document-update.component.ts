import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EmployeeDocumentFormService, EmployeeDocumentFormGroup } from './employee-document-form.service';
import { IEmployeeDocument, NewEmployeeDocument } from '../employee-document.model';
import { EmployeeDocumentService } from '../service/employee-document.service';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-employee-document-update',
  templateUrl: './employee-document-update.component.html',
})
export class EmployeeDocumentUpdateComponent implements OnInit {

  employeeDocument: IEmployeeDocument | null = null;

  isSaving = false;
  isFileSelected = false;
  inValidFile = false;
  inValidFileErrorMsg = '';

  // file
  selectedFileToUpload?: File;

  editForm: EmployeeDocumentFormGroup = this.employeeDocumentFormService.createEmployeeDocumentFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected employeeDocumentService: EmployeeDocumentService,
    protected employeeDocumentFormService: EmployeeDocumentFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeDocument }) => {
      this.employeeDocument = employeeDocument;
      if (employeeDocument) {
        this.updateForm(employeeDocument);
        this.updateFileInputValidation();
      }
    });
  }

  updateFileInputValidation(): void {
    // remove file validation in update mode
    if (this.editForm.get('id')!.value) {
      this.editForm.get('file')!.clearValidators();
      this.editForm.get('file')!.updateValueAndValidity();
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employeeDocument = this.employeeDocumentFormService.getEmployeeDocument(this.editForm);

    if (employeeDocument.id !== null) {
      if(this.selectedFileToUpload){
        this.subscribeToSaveResponse(this.employeeDocumentService.updateWithFile(this.editForm.get(['pin']).value , this.selectedFileToUpload, employeeDocument));
      }
      else{
        this.subscribeToSaveResponse(this.employeeDocumentService.updateWithoutFile(this.editForm.get(['pin']).value , employeeDocument));
      }
    } else {
      this.subscribeToSaveResponse(this.employeeDocumentService.createWithFile(this.editForm.get(['pin']).value , this.selectedFileToUpload!, employeeDocument as NewEmployeeDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeDocument>>): void {
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

  protected updateForm(employeeDocument: IEmployeeDocument): void {
    this.employeeDocument = employeeDocument;
    this.employeeDocumentFormService.resetForm(this.editForm, employeeDocument);
  }


  setFileData(event: any, field: string, isImage: boolean): void {
    this.isFileSelected = true;
    if (event.target.files != null && event.target.files.length > 0) {
      this.inValidFileErrorMsg = '';
      this.inValidFile = false;
      this.selectedFileToUpload = event.target.files[0];
      const sizeInKB = Number(this.selectedFileToUpload!.size / 1024);
      if (sizeInKB > 15360) {
        this.inValidFile = true;
        this.selectedFileToUpload = undefined;
        this.inValidFileErrorMsg = 'Max allowed file size is 15MB';
        this.editForm.patchValue({
          file: undefined,
        });
        return;
      } else {
        this.editForm.get('file')!.touched;
        this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: FileLoadError) => {
          this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key }));
        });
      }
    }
  }
}
