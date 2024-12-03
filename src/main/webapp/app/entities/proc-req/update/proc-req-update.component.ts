import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import dayjs from 'dayjs/esm';
import { FormBuilder, Validators } from '@angular/forms';
import { finalize, map } from 'rxjs/operators';
import { NgbDateStruct, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProcReqMasterFormService, ProcReqFormGroup } from './proc-req-master-form.service';
import { IProcReq } from '../proc-req.model';
import { ProcReqService } from '../service/proc-req.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IItemInformation } from 'app/entities/item-information/item-information.model';
import { ItemInformationService } from 'app/entities/item-information/service/item-information.service';
import { IProcReqMaster } from 'app/entities/proc-req-master/proc-req-master.model';
import { ProcReqMasterService } from 'app/entities/proc-req-master/service/proc-req-master.service';
import { IDepartment } from '../../department/department.model';
import { IDepartmentItems } from '../../department/department-items';
import { swalConfirmationWithMessage } from '../../../shared/swal-common/swal-confirmation.common';
import {
  swalClose,
  swalForErrorWithMessage,
  swalOnLoading,
  swalOnSavedSuccess,
  swalOnUpdatedSuccess,
} from '../../../shared/swal-common/swal-common';
import { ProcReqItemUpdateDialogComponent } from './proc-req-item-update-dialog.component';
import { DefinedKeys } from '../../../config/defined-keys.constant';
import { ConfigService } from '../../config/service/config.service';

@Component({
  selector: 'jhi-proc-req-update',
  templateUrl: './proc-req-update.component.html',
})
export class ProcReqUpdateComponent implements OnInit {
  isSaving = false;
  procReq: IProcReq | null = null;

  departments: IDepartment[] = [];
  departmentItems: IDepartmentItems[] = [];
  itemInformationList: IItemInformation[] = [];
  selectedItemInformationIDList: number[] = [];
  requestedDateDp: any;

  procReqList: IProcReq[] = [];

  // only use in edit mode (store old data before changing department)
  selectedDepartmentId: any;
  tempProcReqList: IProcReq[] = [];

  tempDepartmentId!: number; // control dept change (in cancel of swal confirmation tempDepartmentId will be set to formControl['departmentId]' value

  maxTotalPrice!: number; // 15lacks

  minimumExpectedReceiveDate: NgbDateStruct;

  itemInformationsSharedCollection: IItemInformation[] = [];
  procReqMastersSharedCollection: IProcReqMaster[] = [];

  editForm: ProcReqFormGroup = this.procReqFormService.createProcReqFormGroup();

  private procReqMaster: IProcReqMaster;

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected procReqService: ProcReqService,
    protected procReqFormService: ProcReqMasterFormService,
    protected itemInformationService: ItemInformationService,
    protected procReqMasterService: ProcReqMasterService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute,
    private configService: ConfigService
  ) {
    this.minimumExpectedReceiveDate = {
      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1,
      day: new Date().getDate() + 1,
    };
  }

  compareItemInformation = (o1: IItemInformation | null, o2: IItemInformation | null): boolean =>
    this.itemInformationService.compareItemInformation(o1, o2);

  compareProcReqMaster = (o1: IProcReqMaster | null, o2: IProcReqMaster | null): boolean =>
    this.procReqMasterService.compareProcReqMaster(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ procReqMaster }) => {
      this.procReqMaster = procReqMaster;

      if (procReqMaster !== null) {
        // only for edit mode
        this.tempProcReqList = this.procReqList = procReqMaster.procReqs!;
        this.selectedDepartmentId = procReqMaster.departmentId;
        this.loadDeptWiseItems(procReqMaster.departmentId); // load items information list
      }

      this.updateForm(procReqMaster);

      //this.tempDepartmentId = procReqMaster.departmentId;

      //this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
      this.itemInformationService.getDepartmentsAndItemsMapping().subscribe((departmentItemsResponse: HttpResponse<IDepartmentItems[]>) => {
        this.departmentItems = departmentItemsResponse.body!;
      });

      this.configService.findByKeyCommon(DefinedKeys.PRF_MAX_TOTAL_APPROXIMATE_BDT_AMOUNT).subscribe(configAmountResponse => {
        this.maxTotalPrice = Number(configAmountResponse.body!.value);
        this.editForm
          .get(['totalApproximatePrice'])!
          .setValidators([Validators.required, Validators.min(1), Validators.max(this.maxTotalPrice)]);
        this.editForm.get(['totalApproximatePrice'])!.updateValueAndValidity();
      });

      this.loadRelationshipsOptions();
    });
  }

  trackId(index: number, item: IProcReq): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const procReq = this.procReqFormService.getProcReq(this.editForm);
    procReq.procReqs = this.procReqList;
    if (procReq.id !== null) {
      this.subscribeToSaveResponse(this.procReqService.update(procReq));
    } else {
      this.subscribeToSaveResponse(this.procReqService.create(procReq as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProcReqMaster>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    if (this.editForm.get('id')!.value) swalOnUpdatedSuccess();
    else swalOnSavedSuccess();
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected updateForm(procReq: IProcReqMaster): void {
    this.procReq = procReq;
    this.procReqFormService.resetForm(this.editForm, procReq);

    // this.itemInformationsSharedCollection = this.itemInformationService.addItemInformationToCollectionIfMissing<IItemInformation>(
    //   this.itemInformationsSharedCollection,
    //   procReq.itemInformation
    // );
    // this.procReqMastersSharedCollection = this.procReqMasterService.addProcReqMasterToCollectionIfMissing<IProcReqMaster>(
    //   this.procReqMastersSharedCollection,
    //   procReq.procReqMaster
    // );
  }

  protected loadRelationshipsOptions(): void {
    // this.itemInformationService
    //   .query()
    //   .pipe(map((res: HttpResponse<IItemInformation[]>) => res.body ?? []))
    //   .pipe(
    //     map((itemInformations: IItemInformation[]) =>
    //       this.itemInformationService.addItemInformationToCollectionIfMissing<IItemInformation>(
    //         itemInformations,
    //         this.procReq?.itemInformation
    //       )
    //     )
    //   )
    //   .subscribe((itemInformations: IItemInformation[]) => (this.itemInformationsSharedCollection = itemInformations));
    //
    // this.procReqMasterService
    //   .query()
    //   .pipe(map((res: HttpResponse<IProcReqMaster[]>) => res.body ?? []))
    //   .pipe(
    //     map((procReqMasters: IProcReqMaster[]) =>
    //       this.procReqMasterService.addProcReqMasterToCollectionIfMissing<IProcReqMaster>(procReqMasters, this.procReq?.procReqMaster)
    //     )
    //   )
    //   .subscribe((procReqMasters: IProcReqMaster[]) => (this.procReqMastersSharedCollection = procReqMasters));
  }

  changeDepartment(): void {
    const departmentId = this.editForm.get('departmentId')!.value;

    if (this.procReqList.length > 0) {
      swalConfirmationWithMessage('After department changing requisition list will reset. Confirm ?').then(response => {
        if (response.isConfirmed) {
          this.tempDepartmentId = departmentId;
          this.refreshProcReqList();
          if (departmentId !== undefined && departmentId !== null) {
            this.loadDeptWiseItems(departmentId);
          }
        } else {
          this.editForm.get('departmentId')!.setValue(this.tempDepartmentId);
        }
      });
    } else {
      if (departmentId !== undefined && departmentId !== null) {
        this.tempDepartmentId = departmentId;
        this.loadDeptWiseItems(departmentId);
        this.refreshProcReqList();
      }
    }
  }

  loadDeptWiseItems(departmentId: number): void {
    this.itemInformationService.getByDepartmentId(departmentId).subscribe((itemInformationResponse: HttpResponse<IItemInformation[]>) => {
      this.itemInformationList = itemInformationResponse.body!;
    });
  }

  refreshProcReqList(): void {
    const departmentId = this.editForm.get('departmentId')!.value;
    // load previous items from temporary list
    if (this.editForm.get(['id'])!.value && departmentId === this.selectedDepartmentId) {
      this.procReqList = this.tempProcReqList;
    } else {
      this.resetProcReqList();
    }
  }

  resetProcReqList(): void {
    this.procReqList = [];
  }

  addNewProcReq(): void {
    const modalRef = this.modalService.open(ProcReqItemUpdateDialogComponent, { backdrop: 'static', size: 'lg' });
    modalRef.componentInstance.selectedDepartmentId = this.editForm.get('departmentId')!.value;
    modalRef.componentInstance.itemInformation = this.itemInformationList;
    modalRef.componentInstance.procReqList = this.procReqList;
    modalRef.result.then(
      (result: IProcReq) => {
        // close with save button
        const index = this.procReqList.findIndex((_: IProcReq) => _.itemInformationId === result.itemInformationId);
        if (index !== -1) {
          swalForErrorWithMessage('Item already exits in your list. Please update the following item.');
        } else {
          this.procReqList.push(result);
        }
      },
      reason => {}
    );
  }

  downloadFile(id: number): void {
    swalOnLoading('Preparing for download...');
    this.procReqService.downloadFileCommonUser(id).subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should

        const newBlob = new Blob([x.body!], { type: 'application/octet-stream' });
        const fileName = this.getFileName(x.headers.get('content-disposition')!);

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
          (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
          return;
        }

        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        const link = document.createElement('a');
        link.href = data;
        link.download = fileName;
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        // tslint:disable-next-line:typedef
        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
      },
      err => {},
      () => {
        swalClose();
      }
    );
  }

  private getFileName(disposition: string): string {
    const utf8FilenameRegex = /filename\*=UTF-8''([\w%\-\\.]+)(?:; ?|$)/i;
    const asciiFilenameRegex = /^filename=(["']?)(.*?[^\\])\1(?:; ?|$)/i;

    let fileName = '';
    if (utf8FilenameRegex.test(disposition)) {
      fileName = decodeURIComponent(utf8FilenameRegex.exec(disposition)![1]);
    } else {
      // prevent ReDos attacks by anchoring the ascii regex to string start and
      //  slicing off everything before 'filename='
      const filenameStart = disposition.toLowerCase().indexOf('filename=');
      if (filenameStart >= 0) {
        const partialDisposition = disposition.slice(filenameStart);
        const matches = asciiFilenameRegex.exec(partialDisposition);
        if (matches != null && matches[2]) {
          fileName = matches[2];
        }
      }
    }
    return fileName;
  }

  editProcReqList(procReq: IProcReq): void {
    const modalRef = this.modalService.open(ProcReqItemUpdateDialogComponent, { backdrop: 'static', size: 'lg' });
    modalRef.componentInstance.selectedDepartmentId = this.editForm.get('departmentId')!.value;
    modalRef.componentInstance.itemInformation = this.itemInformationList;
    modalRef.componentInstance.procReqItemForEdit = procReq;
    modalRef.componentInstance.procReqList = this.procReqList;
    modalRef.componentInstance.unitOfMeasurement = procReq.unitOfMeasurementName;
    const idOfEditProcReq = procReq.id;
    modalRef.result.then(
      result => {
        const indexOfProcReq = this.procReqList.findIndex((_: IProcReq) => _.id === idOfEditProcReq);
        this.procReqList[indexOfProcReq] = result;
      },
      reason => {}
    );
  }

  removeFromProcReqList(index: number): void {
    swalConfirmationWithMessage('Delete ?').then(response => {
      if (response.isConfirmed) {
        this.procReqList.splice(index, 1);
      }
    });
  }
}
