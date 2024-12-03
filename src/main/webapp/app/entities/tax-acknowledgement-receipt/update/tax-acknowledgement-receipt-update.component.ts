import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { TaxAcknowledgementReceiptFormService, TaxAcknowledgementReceiptFormGroup } from './tax-acknowledgement-receipt-form.service';
import { TaxAcknowledgementReceiptService } from '../service/tax-acknowledgement-receipt.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { AitConfigService } from 'app/entities/ait-config/service/ait-config.service';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { UserService } from 'app/entities/user/user.service';
import { AcknowledgementStatus } from 'app/entities/enumerations/acknowledgement-status.model';
import {IEmployeeMinimal} from "../../../shared/model/employee-minimal.model";
import {IIncomeTaxDropDownMenu} from "../../../shared/model/drop-down-income-tax.model";
import {EmployeeMinimalListType} from "../../../shared/model/enumerations/employee-minimal-list-type.model";
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {
  ITaxAcknowledgementReceipt
} from "../../../payroll-management-system/user-tax-acknowledgement-receipt/tax-acknowledgement-receipt.model";
import {
  swalClose,
  swalOnLoading,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalSuccessWithMessage
} from "../../../shared/swal-common/swal-common";
import {
  IncomeTaxStatementService
} from "../../../payroll-management-system/income-tax-statement/income-tax-statement.service";
import {IEmployee} from "../../employee-custom/employee-custom.model";
import {DateValidationService} from "../../../shared/service/date-validation.service";


@Component({
  selector: 'jhi-tax-acknowledgement-receipt-update',
  templateUrl: './tax-acknowledgement-receipt-update.component.html',
  styleUrls: ['./tax-acknowledgement-receipt-update.component.scss'],
})
export class TaxAcknowledgementReceiptUpdateComponent implements OnInit {
  isSaving = false;
  taxAcknowledgementReceipt: ITaxAcknowledgementReceipt | null = null;
  acknowledgementStatusValues = Object.keys(AcknowledgementStatus);

  // aitConfigsSharedCollection: IAitConfig[] = [];
  // employeesSharedCollection: IEmployee[] = [];
  // usersSharedCollection: IUser[] = [];

  employees: IEmployeeMinimal[] = [];
  aitConfigsYear!: IIncomeTaxDropDownMenu[];
  employee?: IEmployee;
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

  employeeSelectListType = EmployeeMinimalListType.ALL;

  selectedEmployee: IEmployee | null = null;

  editForm: TaxAcknowledgementReceiptFormGroup = this.taxAcknowledgementReceiptFormService.createTaxAcknowledgementReceiptFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected taxAcknowledgementReceiptService: TaxAcknowledgementReceiptService,
    protected taxAcknowledgementReceiptFormService: TaxAcknowledgementReceiptFormService,
    protected aitConfigService: AitConfigService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected dateValidationService: DateValidationService,
    private incomeTaxStatementService: IncomeTaxStatementService
  ) {
    this.maxDate = dateValidationService.getDOBMaxDate();
  }

  // compareAitConfig = (o1: IAitConfig | null, o2: IAitConfig | null): boolean => this.aitConfigService.compareAitConfig(o1, o2);
  //
  // compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);
  //
  // compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxAcknowledgementReceipt }) => {
      this.getAllAitConfigYears();

      if (taxAcknowledgementReceipt) {
        this.updateForm(taxAcknowledgementReceipt);
        this.taxAcknowledgementId = taxAcknowledgementReceipt.id!;
        this.showAssessmentYear();
        this.editForm.controls.filePath.clearValidators();
        this.uploadFileMissing = false;

        if (taxAcknowledgementReceipt.tinNumber !== null && taxAcknowledgementReceipt.tinNumber !== undefined) {
          this.isTinMissing = false;
        }

        if (taxAcknowledgementReceipt.taxesZone !== null && taxAcknowledgementReceipt.taxesZone !== undefined) {
          this.isTaxZoneMissing = false;
          this.editForm.controls.taxesZone.setValue(taxAcknowledgementReceipt.taxesZone);
        }

        if (taxAcknowledgementReceipt.taxesCircle !== null && taxAcknowledgementReceipt.taxesCircle !== undefined) {
          this.isTaxCircleMissing = false;
          this.editForm.controls.taxesCircle.setValue(taxAcknowledgementReceipt.taxesCircle);
        }

        this.editForm.controls.employeeId.setValue(taxAcknowledgementReceipt.employeeId);
        this.getCurrentEmployeeInfo(taxAcknowledgementReceipt.employeeId);
      }
    });
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
    const taxAcknowledgementReceipt = this.taxAcknowledgementReceiptFormService.getTaxAcknowledgementReceipt(this.editForm);
    if (taxAcknowledgementReceipt.id !== undefined && taxAcknowledgementReceipt.id !== null) {
      if (this.uploadFile !== undefined && this.uploadFile !== null) {
        this.subscribeToSaveResponse(
          this.taxAcknowledgementReceiptService.updateWithFile(this.uploadFile, taxAcknowledgementReceipt)
        );
      } else {
        this.subscribeToSaveResponse(this.taxAcknowledgementReceiptService.update(taxAcknowledgementReceipt));
      }
    } else {
      if (this.uploadFile !== undefined && this.uploadFile !== null) {
        this.subscribeToSaveResponse(
          this.taxAcknowledgementReceiptService.createTaxAcknowledgement(this.uploadFile, taxAcknowledgementReceipt)
        );
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
    if (this.taxAcknowledgementId !== null && this.taxAcknowledgementId !== undefined) {
      swalSuccessWithMessage('Updated!');
    } else {
      swalSuccessWithMessage('Received!');
    }

    setTimeout(() => {
      this.previousState();
    }, 1000);
  }

  protected onSaveError(err: any): void {
    swalOnRequestErrorWithBackEndErrorTitle(err.error.title, 3000);
    this.isSaving = false;
  }

  protected updateForm(taxAcknowledgementReceipt: ITaxAcknowledgementReceipt): void {
    this.taxAcknowledgementReceipt = taxAcknowledgementReceipt;
    this.taxAcknowledgementReceiptFormService.resetForm(this.editForm, taxAcknowledgementReceipt);

    // this.aitConfigsSharedCollection = this.aitConfigService.addAitConfigToCollectionIfMissing<IAitConfig>(
    //   this.aitConfigsSharedCollection,
    //   taxAcknowledgementReceipt.fiscalYear
    // );
    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   taxAcknowledgementReceipt.employee
    // );
    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   taxAcknowledgementReceipt.receivedBy,
    //   taxAcknowledgementReceipt.createdBy,
    //   taxAcknowledgementReceipt.updatedBy
    // );
  }

  protected loadRelationshipsOptions(): void {
    // this.aitConfigService
    //   .query()
    //   .pipe(map((res: HttpResponse<IAitConfig[]>) => res.body ?? []))
    //   .pipe(
    //     map((aitConfigs: IAitConfig[]) =>
    //       this.aitConfigService.addAitConfigToCollectionIfMissing<IAitConfig>(aitConfigs, this.taxAcknowledgementReceipt?.fiscalYear)
    //     )
    //   )
    //   .subscribe((aitConfigs: IAitConfig[]) => (this.aitConfigsSharedCollection = aitConfigs));
    //
    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.taxAcknowledgementReceipt?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
    //
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(
    //         users,
    //         this.taxAcknowledgementReceipt?.receivedBy,
    //         this.taxAcknowledgementReceipt?.createdBy,
    //         this.taxAcknowledgementReceipt?.updatedBy
    //       )
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  trackById(index: number, item: any): any {
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

  getAllAitConfigYears(): void {
    this.incomeTaxStatementService.getAllAitConfigYears().subscribe(data => (this.aitConfigsYear = data.body!));
  }

  getCurrentEmployeeInfo(employeeId: number): void {
    this.taxAcknowledgementReceiptService.getCurrentEmployeeInfo(employeeId).subscribe((res: HttpResponse<IEmployee>) => {
      this.employee = res.body!;
      if (this.employee !== undefined && this.employee !== null) {
        this.populateEditFormWithEmployeeInfo(this.employee);
      }
    });
  }

  populateEditFormWithEmployeeInfo(employee: IEmployee): void {
    this.editForm.controls.pin.setValue(employee.pin);
    this.editForm.controls.name.setValue(employee.fullName);
    this.editForm.controls.designation.setValue(employee.designationName);

    this.editForm.controls.tinNumber.setValue(employee.tinNumber);
    this.editForm.controls.taxesZone.setValue(employee.taxesZone);
    this.editForm.controls.taxesCircle.setValue(employee.taxesCircle);

    this.editForm.controls.pin.disable();
    this.editForm.controls.name.disable();
    this.editForm.controls.designation.disable();
    this.editForm.controls.assessmentYear.disable();

    if (employee.tinNumber === undefined || employee.tinNumber === null || employee.tinNumber === '') {
      this.isTinMissing = true;
      this.editForm.controls.tinNumber.enable();
    } else if (employee.taxesCircle === undefined || employee.taxesCircle === null || employee.taxesCircle === '') {
      this.isTaxCircleMissing = true;
      this.isTaxZoneMissing = false;
      this.isTinMissing = false;
      this.editForm.controls.tinNumber.setValue(employee.tinNumber);
      this.editForm.controls.tinNumber.disable();
    } else if (employee.taxesZone === undefined || employee.taxesZone === null || employee.taxesZone === '') {
      this.isTaxZoneMissing = true;
      this.isTaxCircleMissing = false;
      this.isTinMissing = false;
      this.editForm.controls.tinNumber.setValue(employee.tinNumber);
      this.editForm.controls.tinNumber.disable();
      this.editForm.controls.taxesCircle.setValue(employee.taxesCircle);
    } else {
      this.editForm.controls.tinNumber.setValue(employee.tinNumber);
      this.editForm.controls.taxesCircle.setValue(employee.taxesCircle);
      this.editForm.controls.taxesZone.setValue(employee.taxesZone);
      this.editForm.controls.tinNumber.disable();
      this.isTinMissing = false;
      this.isTaxZoneMissing = false;
      this.isTaxCircleMissing = false;
    }
  }

  changeEmployee(employee: any): void {
    if (employee) {
      this.selectedEmployee = employee;
      this.editForm.get('employeeId')!.setValue(employee.id);
      this.getCurrentEmployeeInfo(employee.id);
    } else {
      this.editForm.get('employeeId')!.reset();
    }
  }

  exportReport(id: number): void {
    swalOnLoading('Preparing for download...');
    this.taxAcknowledgementReceiptService.downloadTaxAcknowledgement(id).subscribe(
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
