import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AlertError } from '../../../shared/alert/alert-error.model';
import { IProcReq } from '../proc-req.model';
import { IItemInformation } from '../../item-information/item-information.model';
import { EventManager, EventWithContent } from '../../../core/util/event-manager.service';
import { DataUtils, FileLoadError } from '../../../core/util/data-util.service';
import { ProcReqFormService } from './proc-req-form.service';

@Component({
  selector: 'jhi-proc-user-item-update',
  templateUrl: './proc-req-item-update-dialog.component.html',
})
export class ProcReqItemUpdateDialogComponent implements OnInit {
  unitOfMeasurement = 'unit';

  inValidFile = false;
  inValidFileErrorMsg = '';
  enableUploadOption = true;
  selectedReferenceFile: File | undefined;

  procReqItemForEdit!: IProcReq;

  itemInformation: IItemInformation[] = [];

  procReqList: IProcReq[] = [];
  selectedItemIdList: number[] = [];

  isSaving = false;

  editForm = this.procReqFormService.createProcReqFormGroup();

  isItemAlreadySelected = false;

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected procReqFormService: ProcReqFormService,
    protected activeModal: NgbActiveModal
  ) {}

  ngOnInit(): void {
    this.procReqList.forEach(proc => {
      this.selectedItemIdList.push(proc.itemInformationId!);
    });

    if (this.procReqItemForEdit) {
      this.updateForm(this.procReqItemForEdit);
    }

    this.editForm.get('itemInformationId')!.valueChanges.subscribe(itemVal => {
      if (itemVal !== undefined && itemVal !== null) {
        this.unitOfMeasurement = this.itemInformation.find((item: IItemInformation) => item.id === itemVal)?.unitOfMeasurementName!;

        if (this.selectedItemIdList.includes(itemVal)) {
          if (this.procReqItemForEdit !== undefined && this.procReqItemForEdit.itemInformationId === itemVal) {
            this.isItemAlreadySelected = false;
          } else {
            this.isItemAlreadySelected = true;
          }
        } else {
          this.isItemAlreadySelected = false;
        }
      }
    });
  }

  save(): void {
    this.isSaving = true;
    const procReq = this.procReqFormService.getProcReq(this.editForm);

    const item = this.getItemByItemId();
    procReq.itemInformationCode = item.code;
    procReq.itemInformationName = item.name;
    procReq.itemInformationSpecification = item.specification;

    this.activeModal.close(procReq);
  }

  private getItemByItemId(): IItemInformation {
    return this.itemInformation.find((item: IItemInformation) => item.id === this.editForm.get(['itemInformationId'])!.value)!;
  }

  protected updateForm(procReq: IProcReq): void {
    this.procReqItemForEdit = procReq;
    this.procReqFormService.resetForm(this.editForm, procReq);
  }

  trackById(index: number, item: any): any {
    return item.id;
  }

  dismiss(): void {
    this.activeModal.dismiss();
  }

  onChangeReferenceFileUpload($event: Event): void {
    const files = ($event.target as HTMLInputElement).files;
    if (files !== null && files.length > 0) {
      this.inValidFileErrorMsg = '';
      this.inValidFile = false;
      this.enableUploadOption = true;

      const file = files[0];

      if (file.type !== 'image/jpeg' && file.type !== 'application/pdf') {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'Select (PDF/JPG) only';
        return;
      }
      const sizeInKB = Number(file.size / 1024);
      if (sizeInKB > 5120) {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'Max allowed file size is 5MB';
        return;
      }
      this.enableUploadOption = false;
      this.selectedReferenceFile = file;
    }
  }

  clickOnReUpload(): void {
    this.selectedReferenceFile = undefined;
    this.enableUploadOption = true;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.onChangeReferenceFileUpload(event);
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: FileLoadError) => {
      this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key }));
    });
  }
}
