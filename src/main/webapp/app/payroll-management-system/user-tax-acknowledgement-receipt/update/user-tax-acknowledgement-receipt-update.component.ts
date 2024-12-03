import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

import { UserTaxAcknowledgementReceiptService } from '../service/user-tax-acknowledgement-receipt.service';
import {IAitConfig} from "../../../entities/ait-config/ait-config.model";
import {IEmployee} from "../../../entities/employee/employee.model";
import { IUser } from 'app/entities/user/user.model';
import {IEmployeeMinimal} from "../../../shared/model/employee-minimal.model";
import {IIncomeTaxDropDownMenu} from "../../../shared/model/drop-down-income-tax.model";
import {DataUtils, FileLoadError} from "../../../core/util/data-util.service";
import {EventManager, EventWithContent} from "../../../core/util/event-manager.service";
import {EmployeeService} from "../../../entities/employee/service/employee.service";
import {UserService} from "../../../entities/user/user.service";
import {DateValidationService} from "../../../shared/service/date-validation.service";
import {IncomeTaxStatementService} from "../../income-tax-statement/income-tax-statement.service";
import {ITaxAcknowledgementReceipt} from "../tax-acknowledgement-receipt.model";
import {DATE_TIME_FORMAT} from "../../../config/input.constants";
import {AlertError} from "../../../shared/alert/alert-error.model";
import {
  swalClose,
  swalOnLoading,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalSuccessWithMessage
} from "../../../shared/swal-common/swal-common";
import {
  UserTaxAcknowledgementReceiptFormService
} from "./tax-acknowledgement-receipt-form.service";

type SelectableEntity = IAitConfig | IEmployee | IUser;

@Component({
  selector: 'jhi-tax-acknowledgement-receipt-update',
  styleUrls: ['./user-tax-acknowledgement-receipt-update.component.scss'],
  templateUrl: './user-tax-acknowledgement-receipt-update.component.html',
})
export class UserTaxAcknowledgementReceiptUpdateComponent implements OnInit {
  isSaving = false;

  employees: IEmployeeMinimal[] = [];
  aitConfigsYear!: IIncomeTaxDropDownMenu[];
  employee?: IEmployeeMinimal;
  users: IUser[] = [];
  inValidFile = false;
  inValidFileErrorMsg = '';
  uploadFile?: File;
  uploadFileMissing = true;
  isTinMissing = true;
  isTaxZoneMissing = true;
  isTaxCircleMissing = true;
  assessmentYear?: IIncomeTaxDropDownMenu;
  taxAcknowledgementId?: number;
  maxDate: NgbDateStruct;

  editForm = this.userTaxAcknowledgementReceiptFormService.createTaxAcknowledgementReceiptFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected taxAcknowledgementReceiptService: UserTaxAcknowledgementReceiptService,
    protected userTaxAcknowledgementReceiptFormService: UserTaxAcknowledgementReceiptFormService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected dateValidationService: DateValidationService,
    private fb: FormBuilder,
    private incomeTaxStatementService: IncomeTaxStatementService
  ) {
    this.maxDate = dateValidationService.getDOBMaxDate();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxAcknowledgementReceipt }) => {
      this.getCurrentEmployeeInfo();
      this.getAllAitConfigYears();
      if (taxAcknowledgementReceipt) {
        this.taxAcknowledgementId = taxAcknowledgementReceipt.id!;
        this.updateForm(taxAcknowledgementReceipt);
        this.showAssessmentYear();
        this.editForm.controls.filePath.clearValidators();
        this.uploadFileMissing = false;

        if (taxAcknowledgementReceipt.tinNumber !== null && taxAcknowledgementReceipt.tinNumber !== undefined) {
          this.isTinMissing = false;
        }

        if (taxAcknowledgementReceipt.taxesZone !== null && taxAcknowledgementReceipt.taxesZone !== undefined) {
          this.isTaxZoneMissing = false;
          this.editForm.get(['taxesZone']).setValue(taxAcknowledgementReceipt.taxesZone);
        }

        if (taxAcknowledgementReceipt.taxesCircle !== null && taxAcknowledgementReceipt.taxesCircle !== undefined) {
          this.isTaxCircleMissing = false;
          this.editForm.get(['taxesCircle']).setValue(taxAcknowledgementReceipt.taxesCircle);
        }
      }
    });
  }

  updateForm(taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): void {
    this.taxAcknowledgementId = taxAcknowledgementReceipt.id;
    this.userTaxAcknowledgementReceiptFormService.resetForm(this.editForm, taxAcknowledgementReceipt);
    /*this.editForm.patchValue({
      id: taxAcknowledgementReceipt.id,
      tinNumber: taxAcknowledgementReceipt.tinNumber,
      receiptNumber: taxAcknowledgementReceipt.receiptNumber,
      taxesCircle: taxAcknowledgementReceipt.taxesCircle,
      taxesZone: taxAcknowledgementReceipt.taxesZone,
      dateOfSubmission: taxAcknowledgementReceipt.dateOfSubmission,
      filePath: taxAcknowledgementReceipt.filePath,
      acknowledgementStatus: taxAcknowledgementReceipt.acknowledgementStatus,
      receivedAt: taxAcknowledgementReceipt.receivedAt ? taxAcknowledgementReceipt.receivedAt.format(DATE_TIME_FORMAT) : null,
      createdAt: taxAcknowledgementReceipt.createdAt ? taxAcknowledgementReceipt.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: taxAcknowledgementReceipt.updatedAt ? taxAcknowledgementReceipt.updatedAt.format(DATE_TIME_FORMAT) : null,
      fiscalYearId: taxAcknowledgementReceipt.fiscalYearId,
      receivedById: taxAcknowledgementReceipt.receivedById,
      createdById: taxAcknowledgementReceipt.createdById,
      updatedById: taxAcknowledgementReceipt.updatedById,
    });*/
  }

  getAllAitConfigYears(): void {
    this.incomeTaxStatementService.getAllAitConfigYears().subscribe(data => (this.aitConfigsYear = data.body!));
  }

  getCurrentEmployeeInfo(): void {
    this.taxAcknowledgementReceiptService.getCurrentEmployeeInfo().subscribe((res: HttpResponse<IEmployeeMinimal>) => {
      this.employee = res.body!;
      if (this.employee !== undefined && this.employee !== null) {
        this.populateEditFormWithEmployeeInfo(this.employee);
      }
    });
  }

  populateEditFormWithEmployeeInfo(employee: IEmployeeMinimal): void {
    this.editForm.get(['employeeId']).setValue(employee.id);
    this.editForm.get(['pin']).setValue(employee.pin);
    this.editForm.get(['name']).setValue(employee.fullName);
    this.editForm.get(['designation']).setValue(employee.designationName);

    this.editForm.get(['tinNumber']).setValue(employee.tinNumber);
    this.editForm.get(['taxesZone']).setValue(employee.taxZone);
    this.editForm.get(['taxesCircle']).setValue(employee.taxCircle);

    this.editForm.controls.pin.disable();
    this.editForm.controls.name.disable();
    this.editForm.controls.designation.disable();
    this.editForm.controls.assessmentYear.disable();

    if (employee.tinNumber === undefined || employee.tinNumber === null || employee.tinNumber === '') {
      this.isTinMissing = true;
    } else if (employee.taxCircle === undefined || employee.taxCircle === null || employee.taxCircle === '') {
      this.isTaxCircleMissing = true;
      this.isTaxZoneMissing = false;
      this.isTinMissing = false;
      this.editForm.get(['tinNumber']).setValue(employee.tinNumber);
      this.editForm.controls.tinNumber.disable();
    } else if (employee.taxZone === undefined || employee.taxZone === null || employee.taxZone === '') {
      this.isTaxZoneMissing = true;
      this.isTaxCircleMissing = false;
      this.isTinMissing = false;
      this.editForm.get(['tinNumber']).setValue(employee.tinNumber);
      this.editForm.controls.tinNumber.disable();
      this.editForm.get(['taxesCircle']).setValue(employee.taxCircle);
      //this.editForm.controls.taxesCircle.setValue(employee.taxCircle);
    } else {
      this.editForm.get(['tinNumber']).setValue(employee.tinNumber);
      this.editForm.get(['taxesZone']).setValue(employee.taxZone);
      this.editForm.get(['taxesCircle']).setValue(employee.taxCircle);
      // this.editForm.controls.tinNumber.setValue(employee.tinNumber);
      // this.editForm.controls.taxesCircle.setValue(employee.taxCircle);
      // this.editForm.controls.taxesZone.setValue(employee.taxZone);
      this.editForm.controls.tinNumber.disable();
      this.isTinMissing = false;
      this.isTaxZoneMissing = false;
      this.isTaxCircleMissing = false;
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: FileLoadError) => {
      this.eventManager.broadcast(
        new EventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const taxAcknowledgementReceipt = this.userTaxAcknowledgementReceiptFormService.getTaxAcknowledgementReceipt(this.editForm);
    if (taxAcknowledgementReceipt.id !== undefined && taxAcknowledgementReceipt.id !== null) {
      if (this.uploadFile !== undefined && this.uploadFile !== null) {
        this.subscribeToSaveResponse(this.taxAcknowledgementReceiptService.updateWithFile(this.uploadFile, taxAcknowledgementReceipt));
      } else {
        this.subscribeToSaveResponse(this.taxAcknowledgementReceiptService.update(taxAcknowledgementReceipt));
      }
    } else {
      if (this.uploadFile !== undefined && this.uploadFile !== null) {
        this.subscribeToSaveResponse(this.taxAcknowledgementReceiptService.createCommon(this.uploadFile, taxAcknowledgementReceipt));
      }
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITaxAcknowledgementReceipt>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected onSaveSuccess(): void {
    if (this.editForm.get('id')!.value) swalSuccessWithMessage('Updated!');
    else swalSuccessWithMessage('Submitted!');

    setTimeout(() => {
      this.previousState();
    }, 1000);
  }

  protected onSaveError(err: any): void {
    swalOnRequestErrorWithBackEndErrorTitle(err.error.title, 3000);
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  onChangeUploadDocument(event: any): void {
    /* validation: [file type: {image/jpeg}, file size: 2MB,] */
    if (event.target.files != null && event.target.files.length > 0) {
      this.inValidFileErrorMsg = '';
      this.inValidFile = false;
      const file = event.target.files[0];

      if (!file || file.type !== 'application/pdf') {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'Select only pdf file';
        return;
      }
      const sizeInKB = Number(file.size / 1024);
      if (sizeInKB > 5120) {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'Max allowed file size is 5MB';
        return;
      }
      this.uploadFile = file;
      this.uploadFileMissing = false;
      this.taxAcknowledgementId = undefined;
    } else {
      if (this.editForm.get(['id'])!.value === null) {
        this.inValidFile = true;
        this.uploadFile = undefined;
        this.uploadFileMissing = true;
      }
    }
    return;
  }

  showAssessmentYear(): void {
    const fiscalId = this.editForm.get(['fiscalYearId'])!.value;
    if (fiscalId !== undefined && fiscalId !== null) {
      this.incomeTaxStatementService.getAssessmentYearByAitConfigId(fiscalId).subscribe(res => {
        this.assessmentYear = res.body!;
        this.editForm.controls.assessmentYear.setValue(this.assessmentYear.range);
        this.editForm.controls.assessmentYear.disable();
      });
    }
  }

  onChangeTaxInput(event: any): void {
    const tinNumber = this.editForm.get(['tinNumber'])!.value;
    const taxesCircle = this.editForm.get(['taxesCircle'])!.value;
    const taxesZone = this.editForm.get(['taxesZone'])!.value;

    if (tinNumber !== undefined && tinNumber !== null && tinNumber !== '') {
      this.isTinMissing = false;
    } else {
      this.isTinMissing = true;
    }

    if (taxesCircle !== undefined && taxesCircle !== null && taxesCircle !== '') {
      this.isTaxCircleMissing = false;
    } else {
      this.isTaxCircleMissing = true;
    }
    if (taxesZone !== undefined && taxesZone !== null && taxesZone !== '') {
      this.isTaxZoneMissing = false;
    } else {
      this.isTaxZoneMissing = true;
    }
  }

  exportReport(id: number): void {
    swalOnLoading('Preparing for download...');
    this.taxAcknowledgementReceiptService.downloadTaxAcknowledgementReport(id).subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should

        const fileName = this.getFileName(x.headers.get('content-disposition')!);
        const newBlob = new Blob([x.body!], { type: 'application/octet-stream' });

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
}
