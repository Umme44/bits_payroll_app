import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import dayjs from 'dayjs/esm';

import { IAllowanceName } from '../../../shared/model/allowance-name.model';
import { swalOnDeleteConfirmation, swalOnDeleteSuccess, swalOnRequestError, swalOnSavedSuccess } from 'app/shared/swal-common/swal-common';
import { IdentityNumberValidationService, IIdentityNumberValidation } from 'app/shared/service/identity-number-validation.service';
import { IDesignation } from '../../../shared/legacy/legacy-model/designation.model';
import { IDepartment } from '../../../shared/legacy/legacy-model/department.model';
import { INationality } from '../../nationality/nationality.model';
import { IBankBranch } from '../../bank-branch/bank-branch.model';
import { IBand } from '../../../shared/legacy/legacy-model/band.model';
import { IUnit } from '../../../shared/legacy/legacy-model/unit.model';
import { IEmployeePin } from '../../employee-pin/employee-pin.model';
import { ConfigService } from '../../../shared/legacy/legacy-service/config.service';
import { EmployeeCustomService } from '../service/employee-custom.service';
import { DesignationService } from '../../../shared/legacy/legacy-service/designation.service';
import { DepartmentService } from '../../../shared/legacy/legacy-service/department.service';
import { NationalityService } from '../../nationality/service/nationality.service';
import { BankBranchService } from '../../bank-branch/service/bank-branch.service';
import { BandService } from '../../../shared/legacy/legacy-service/band.service';
import { UnitService } from '../../../shared/legacy/legacy-service/unit.service';
import { EmployeePinService } from '../../employee-pin/service/employee-pin.service';
import { EmployeeCustomFormService, EmployeeFormGroup } from './employee-custom-form.service';
import { IEmployee } from '../employee-custom.model';
import { EmployeeCategory } from '../../../shared/model/enumerations/employee-category.model';
import {
  EmployeeDocumentFormGroup,
  EmployeeDocumentFormService,
} from 'app/entities/employee-document/update/employee-document-form.service';
import { IEmployeeDocument, NewEmployeeDocument } from 'app/entities/employee-document/employee-document.model';
import {
  EntityArrayResponseType as EmployeeDocumentEntityArrayResponseType,
  EmployeeDocumentService,
} from 'app/entities/employee-document/service/employee-document.service';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { IconProp } from '@fortawesome/fontawesome-svg-core';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import Swal from 'sweetalert2';

type SelectableEntity = IDesignation | IDepartment | IEmployee | INationality | IBankBranch | IBand | IUnit;

@Component({
  selector: 'jhi-employee-custom-update',
  templateUrl: './employee-custom-update.component.html',
  styleUrls: ['./employee-custom-update.component.scss'],
})
export class EmployeeCustomUpdateComponent implements OnInit {
  isInvalid = false;
  startRange?: dayjs.Dayjs;
  endRange?: dayjs.Dayjs;
  durationInDays: Number = 0;
  durationWithoutCalc: Number = 0;
  isDateInvalid = false;
  isSaving = false;
  isNidInvalid = false;
  timediff: any;
  inTime: any;
  outTime: any;
  hour: any;
  durationinhourmin: any;
  minute: any;
  designations: IDesignation[] = [];
  departments: IDepartment[] = [];
  employees: IEmployee[] = [];
  nationalities: INationality[] = [];
  bankbranches: IBankBranch[] = [];
  bands: IBand[] = [];
  units: IUnit[] = [];

  selectedEmployeeCategory!: EmployeeCategory;
  employeePin!: string;
  selectedEmployeePin!: IEmployeePin;
  onBoardingEmployee = false;

  allowanceName: IAllowanceName | null = null;

  dateOfBirthDp: any;
  dateOfMarriageDp: any;
  dateOfJoiningDp: any;
  dateOfConfirmationDp: any;
  probationPeriodExtendedToDp: any;
  passportIssuedDateDp: any;
  passportExpiryDateDp: any;
  probationPeriodEndDateDp: any;
  contractPeriodExtendedToDp: any;
  contractPeriodEndDateDp: any;
  allowance01EffectiveFromDp: any;
  allowance01EffectiveToDp: any;
  allowance02EffectiveFromDp: any;
  allowance02EffectiveToDp: any;
  allowance03EffectiveFromDp: any;
  allowance03EffectiveToDp: any;
  allowance04EffectiveFromDp: any;
  allowance04EffectiveToDp: any;
  allowance05EffectiveFromDp: any;
  allowance05EffectiveToDp: any;
  allowance06EffectiveFromDp: any;
  allowance06EffectiveToDp: any;
  lastWorkingDayDp: any;
  active = 1;
  isEditForm = false;

  nationalIDNumberValidation: IIdentityNumberValidation;
  // editForm = this.fb.group({
  //   id: [],
  //   referenceId: [null, [Validators.maxLength(250)]],
  //   pin: [null, [Validators.required, Validators.maxLength(4)]],
  //   picture: [],
  //   fullName: [null, [Validators.required, Validators.minLength(3), Validators.maxLength(250)]],
  //   surName: [null, [Validators.minLength(0), Validators.maxLength(250)]],
  //   nationalIdNo: [null, [Validators.minLength(10), Validators.maxLength(17)]],
  //   dateOfBirth: [null],
  //   placeOfBirth: [null, [Validators.maxLength(250)]],
  //   fatherName: [null, [Validators.maxLength(250)]],
  //   motherName: [null, [Validators.maxLength(250)]],
  //   bloodGroup: [],
  //   presentAddress: [null, [Validators.minLength(0), Validators.maxLength(250)]],
  //   permanentAddress: [null, [Validators.minLength(0), Validators.maxLength(250)]],
  //   personalContactNo: [null, [Validators.pattern('^(?:\\+88|88)?(01[3-9]\\d{8})$')]],
  //   personalEmail: [null, [Validators.pattern('^[a-zA-Z0-9.!#$%&amp;&#39;*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$')]],
  //   religion: [null, [Validators.required]],
  //   maritalStatus: [null],
  //   dateOfMarriage: [],
  //   spouseName: [null, [Validators.maxLength(250)]],
  //   officialEmail: [null, [Validators.pattern('^[a-zA-Z0-9.!#$%&amp;&#39;*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$')]],
  //   officialContactNo: [null, [Validators.pattern('^(?:\\+88|88)?(01[3-9]\\d{8})$')]],
  //   officePhoneExtension: [null, [Validators.pattern('^[0-9]*$')]],
  //   whatsappId: [],
  //   skypeId: [],
  //   emergencyContactPersonName: [null, [Validators.minLength(3), Validators.maxLength(250)]],
  //   emergencyContactPersonRelationshipWithEmployee: [null, [Validators.minLength(3), Validators.maxLength(250)]],
  //   emergencyContactPersonContactNumber: [null, [Validators.pattern('^(?:\\+88|88)?(01[3-9]\\d{8})$')]],
  //   mainGrossSalary: [null, [Validators.required, Validators.max(9999999), Validators.min(0)]],
  //   employeeCategory: [null, [Validators.required]],
  //   location: [null, [Validators.minLength(3), Validators.maxLength(250)]],
  //   dateOfJoining: [null, [Validators.required]],
  //   dateOfConfirmation: [],
  //   isProbationaryPeriodExtended: [],
  //   probationPeriodExtendedTo: [],
  //   payType: [],
  //   disbursementMethod: [],
  //   bankName: [null, [Validators.maxLength(250)]],
  //   bankAccountNo: [null, [Validators.maxLength(250)]],
  //   mobileCelling: [null, [Validators.min(0)]],
  //   bkashNumber: [null, [Validators.maxLength(250), Validators.pattern('^(?:\\+88|88)?(01[3-9]\\d{8})$')]],
  //   cardType: [],
  //   cardNumber: [null, [Validators.maxLength(250)]],
  //   tinNumber: [null, [Validators.minLength(12), Validators.maxLength(12), Validators.pattern('^[a-zA-Z0-9]*$')]],
  //   taxesCircle: [null, [Validators.maxLength(250)]],
  //   taxesZone: [null, [Validators.maxLength(250)]],
  //   passportNo: [null, [Validators.minLength(9), Validators.maxLength(9), Validators.pattern('^[a-zA-Z0-9]*$')]],
  //   passportPlaceOfIssue: [null, [Validators.maxLength(250)]],
  //   passportIssuedDate: [],
  //   passportExpiryDate: [],
  //   gender: [null, [Validators.required]],
  //   welfareFundDeduction: [null, [Validators.min(0)]],
  //   employmentStatus: [],
  //   hasDisabledChild: [],
  //   isFirstTimeAitGiver: [],
  //   isSalaryHold: [],
  //   isFestivalBonusHold: [],
  //   isPhysicallyDisabled: [],
  //   isFreedomFighter: [],
  //   hasOverTime: [],
  //   noticePeriodInDays: [],
  //   probationPeriodEndDate: [],
  //   contractPeriodExtendedTo: [],
  //   contractPeriodEndDate: [],
  //   cardType02: [],
  //   cardNumber02: [],
  //   cardType03: [],
  //   cardNumber03: [],
  //   allowance01: [null, [Validators.min(0), Validators.max(10000000)]],
  //   allowance01EffectiveFrom: [],
  //   allowance01EffectiveTo: [],
  //   allowance02: [null, [Validators.min(0), Validators.max(10000000)]],
  //   allowance02EffectiveFrom: [],
  //   allowance02EffectiveTo: [],
  //   allowance03: [null, [Validators.min(0), Validators.max(10000000)]],
  //   allowance03EffectiveFrom: [],
  //   allowance03EffectiveTo: [],
  //   allowance04: [null, [Validators.min(0), Validators.max(10000000)]],
  //   allowance04EffectiveFrom: [],
  //   allowance04EffectiveTo: [],
  //   allowance05: [null, [Validators.min(0), Validators.max(10000000)]],
  //   allowance05EffectiveFrom: [],
  //   allowance05EffectiveTo: [],
  //   allowance06: [null, [Validators.min(0), Validators.max(10000000)]],
  //   allowance06EffectiveFrom: [],
  //   allowance06EffectiveTo: [],
  //   isTaxPaidByOrganisation: [],
  //   isAllowedToGiveOnlineAttendance: [],
  //   canRaiseRrfOnBehalf: [],
  //   createdBy: [],
  //   createdAt: [],
  //   updatedBy: [],
  //   updatedAt: [],
  //   isFixedTermContract: [],
  //   canManageTaxAcknowledgementReceipt: [],
  //   designationId: [null, [Validators.required]],
  //   departmentId: [null, [Validators.required]],
  //   reportingToId: [],
  //   nationalityId: [],
  //   bankBranchId: [],
  //   bandId: [null, [Validators.required]],
  //   unitId: [null, [Validators.required]],
  //   userId: [],
  //   isContructualPeriodExtended: [],
  //   isEligibleForAutomatedAttendance: [],
  // });

  editForm: EmployeeFormGroup = this.employeeFormService.createEmployeeFormGroup();

  editDocumentsForm: EmployeeDocumentFormGroup = this.employeeDocumentFormService.createEmployeeDocumentFormGroup();

  isSaving_editDocumentForm = false;

  isFileSelected = false;
  inValidFile = false;
  inValidFileErrorMsg = '';

  employeeDocument?: IEmployeeDocument[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;

  @ViewChild('takeInput') takeInput: ElementRef;

  totalTab = 6;

  trackId = (_index: number, item: IEmployeeDocument): number => this.employeeDocumentService.getEmployeeDocumentIdentifier(item);

  // file
  selectedFileToUpload?: File;
  employee?: IEmployee;

  constructor(
    protected configService: ConfigService,
    protected employeeService: EmployeeCustomService,
    protected designationService: DesignationService,
    protected departmentService: DepartmentService,
    protected nationalityService: NationalityService,
    protected bankBranchService: BankBranchService,
    protected bandService: BandService,
    protected unitService: UnitService,
    protected employeePinService: EmployeePinService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected employeeFormService: EmployeeCustomFormService,
    protected identityNumberValidationService: IdentityNumberValidationService,
    private fb: FormBuilder,
    private employeeDocumentFormService: EmployeeDocumentFormService,
    private employeeDocumentService: EmployeeDocumentService,
    private parseLinks: ParseLinks,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager
  ) {
    this.nationalIDNumberValidation = { isValid: true, validationMsg: '' };
  }

  ngOnInit(): void {
    this.configService.getAllowanceName().subscribe(res => {
      this.allowanceName = res.body;
    });

    this.activatedRoute.data.subscribe(({ employee }) => {
      this.employee = employee;
      if (employee !== null) {
        // const today = dayjs().startOf('day');
        // employee.createdAt = today;
        this.updateForm(employee);
        this.editForm.markAllAsTouched();
        this.isEditForm = true;
        this.editForm.get('rrfNumber')?.disable();
        this.editForm.get('lastWorkingDay')?.disable();
        this.editForm.get('contractPeriodEndDate')?.disable();
        this.editForm.get('contractPeriodExtendedTo')?.disable();
        //this.nationalIdValidation();
        this.simpleNidValidation();
        this.editDocumentsForm.get(['pin']).setValue(this.employee.pin);
        this.totalTab = 7;
      }

      if (history.state?.pin) {
        this.getEmployeePin();
        this.employee = employee;
      }

      this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));

      this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));

      this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => {
        this.employees = res.body!;
        this.employees = this.employees.map(item => {
          return {
            id: item.id,
            fullName: item.pin + ' - ' + item.fullName,
          };
        });
      });

      this.nationalityService.query().subscribe((res: HttpResponse<INationality[]>) => (this.nationalities = res.body || []));

      this.bankBranchService.query().subscribe((res: HttpResponse<IBankBranch[]>) => (this.bankbranches = res.body || []));

      this.bandService.query().subscribe((res: HttpResponse<IBand[]>) => (this.bands = res.body || []));

      this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));

      this.load();
    });
  }

  getEmployeePin(): void {
    if (history.state?.pin) {
      this.onBoardingEmployee = true;
      this.employeePin = history.state.pin;
      this.loadPinInformation(this.employeePin);
    } else {
      this.onBoardingEmployee = false;
    }
  }

  loadPinInformation(pin: string): void {
    this.employeePinService.findBtPin(pin).subscribe(res => {
      this.selectedEmployeePin = res.body!;
      this.populateEmployeePinInformation();
    });
  }

  populateEmployeePinInformation(): void {
    this.editForm.get(['pin'])!.setValue(this.selectedEmployeePin.pin);
    this.editForm.get(['pin'])!.disable();

    this.editForm.get(['employeeCategory'])!.setValue(this.selectedEmployeePin.employeeCategory);

    this.editForm.get(['fullName'])!.setValue(this.selectedEmployeePin.fullName);
    this.editForm.get(['departmentId'])!.setValue(this.selectedEmployeePin.departmentId);
    this.editForm.get(['designationId'])!.setValue(this.selectedEmployeePin.designationId);
    this.editForm.get(['unitId'])!.setValue(this.selectedEmployeePin.unitId);
    this.editForm.get(['rrfNumber'])!.setValue(this.selectedEmployeePin.rrfNumber);
  }

  updateForm(employee: IEmployee): void {
    this.employeeFormService.resetForm(this.editForm, employee);
  }

  updateEmployeeDocumentForm(employeeDocument: IEmployeeDocument): void {
    this.employeeDocumentFormService.resetForm(this.editDocumentsForm, employeeDocument);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    console.log(employee.id !== undefined, employee.id !== null);
    if (employee.id !== null) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  uploadDocument(): void {
    this.isSaving_editDocumentForm = true;
    const employeeDocument = this.employeeDocumentFormService.getEmployeeDocument(this.editDocumentsForm);

    if (employeeDocument.id !== null) {
      if (this.selectedFileToUpload) {
        this.subscribeToSaveResponseForUpload(
          this.employeeDocumentService.updateWithFile(this.employee.pin, this.selectedFileToUpload!, employeeDocument)
        );
      } else {
        this.subscribeToSaveResponseForUpload(this.employeeDocumentService.updateWithoutFile(this.employee.pin, employeeDocument));
      }
    } else {
      this.subscribeToSaveResponseForUpload(
        this.employeeDocumentService.createWithFile(this.employee.pin, this.selectedFileToUpload!, employeeDocument as NewEmployeeDocument)
      );
    }
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  loadDuration(): void {
    this.startRange = this.editForm.get(['passportIssuedDate'])!.value;
    this.endRange = this.editForm.get(['passportExpiryDate'])!.value;
    if (this.editForm.get(['passportIssuedDate'])!.value !== null && this.editForm.get(['passportExpiryDate'])!.value !== null) {
      if (this.startRange !== undefined && this.endRange !== undefined) {
        this.durationWithoutCalc = -this.startRange.diff(this.endRange, 'days') + 1;
        if (Number(this.durationWithoutCalc) < 1) {
          this.isDateInvalid = true;
          return;
        } else {
          this.isDateInvalid = false;
          return;
        }
      }
    }
  }

  nextTab(): void {
    if (this.active !== this.totalTab) this.active = this.active + 1;
  }

  previousTab(): void {
    if (this.active > 1) {
      this.active = (this.active - 1) % this.totalTab;
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
    result.subscribe(
      res => {
        this.updateForm(res.body!);
        this.onSaveSuccess();
      },
      () => this.onSaveError()
    );
  }

  protected subscribeToSaveResponseForUpload(result: Observable<HttpResponse<IEmployeeDocument>>): void {
    result.subscribe({
      next: res => {
        console.log(res);
        this.isSaving_editDocumentForm = false;
        swalOnSavedSuccess();
        this.load();
        this.updateEmployeeDocumentForm(res.body!);
        this.clearForm();
      },
      error: res => {
        this.isSaving_editDocumentForm = false;
        console.log(res);
        Swal.fire({
          text: res.error,
          icon: 'error',
          timer: 3500,
        });
      },
    });
  }

  protected onSaveSuccess(): void {
    swalOnSavedSuccess();
    this.isSaving = false;
  }

  protected onSaveError(): void {
    swalOnRequestError();
    this.isSaving = false;
  }

  private createFromForm(): IEmployee {
    return this.employeeFormService.getEmployee(this.editForm);
  }

  employeeCategoryWiseDateValidation(): void {
    this.selectedEmployeeCategory = this.editForm.get('employeeCategory')!.value;
    const docFormControl = this.editForm.get(['dateOfConfirmation'])!;
    const probationPeriodEndDateFormControl = this.editForm.get(['probationPeriodEndDate'])!;
    const contractPeriodEndDateFormControl = this.editForm.get(['contractPeriodEndDate'])!;

    //clear existing validators
    docFormControl.clearValidators();
    probationPeriodEndDateFormControl.clearValidators();
    contractPeriodEndDateFormControl.clearValidators();

    if (this.selectedEmployeeCategory === 'REGULAR_CONFIRMED_EMPLOYEE') {
      docFormControl.setValidators([Validators.required]);
    } else if (this.selectedEmployeeCategory === 'REGULAR_PROVISIONAL_EMPLOYEE') {
      docFormControl.setValidators([Validators.required]);
      probationPeriodEndDateFormControl.setValidators([Validators.required]);
    } else if (this.selectedEmployeeCategory === 'CONTRACTUAL_EMPLOYEE' || this.selectedEmployeeCategory === 'INTERN') {
      contractPeriodEndDateFormControl.setValidators([Validators.required]);
    }
    docFormControl.updateValueAndValidity();
    probationPeriodEndDateFormControl.updateValueAndValidity();
    contractPeriodEndDateFormControl.updateValueAndValidity();
  }

  simpleNidValidation(): void {
    const nid = this.editForm.get('nationalIdNo')!.value;
    this.nationalIDNumberValidation = { isValid: true, validationMsg: '' };
    this.isNidInvalid = false;
    if (nid && nid.toString().trim() !== '') {
      this.nationalIDNumberValidation = this.identityNumberValidationService.isValidNID(nid);
    }
  }

  nationalIdValidation(): void {
    const nidFormControl = this.editForm.get('nationalIdNo');
    const nidLength = nidFormControl!.value.length;
    nidFormControl!.setValidators([Validators.pattern('^[0-9]+$')]);
    switch (nidLength) {
      case 10:
        this.isNidInvalid = false;
        break;
      case 13:
        this.isNidInvalid = false;
        break;
      case 17:
        this.isNidInvalid = false;
        break;
      default:
        if (nidLength < 10) {
          nidFormControl!.clearValidators();
          nidFormControl!.setValidators([Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]+$')]);
        } else if (nidLength > 10 && nidLength < 13) {
          nidFormControl!.clearValidators();
          nidFormControl!.setValidators([
            Validators.required,
            Validators.minLength(13),
            Validators.maxLength(13),
            Validators.pattern('^[0-9]+$'),
          ]);
        } else if (nidLength > 13 && nidLength < 17) {
          nidFormControl!.clearValidators();
          nidFormControl!.setValidators([
            Validators.required,
            Validators.minLength(17),
            Validators.maxLength(17),
            Validators.pattern('^[0-9]+$'),
          ]);
        } else if (nidLength > 17) {
          nidFormControl!.clearValidators();
          nidFormControl!.setValidators([
            Validators.required,
            Validators.minLength(17),
            Validators.maxLength(17),
            Validators.pattern('^[0-9]+$'),
          ]);
        }
        this.isNidInvalid = true;
        break;
    }
    nidFormControl!.updateValueAndValidity();
  }

  loadPage(page: number): void {
    this.page = page;
    this.load();
  }

  load(): void {
    if (this.employee) {
      this.queryBackend(this.page).subscribe({
        next: (res: EmployeeDocumentEntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
    }
  }

  protected onResponseSuccess(response: EmployeeDocumentEntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.employeeDocument = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IEmployeeDocument[] | null): IEmployeeDocument[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
  }

  protected queryBackend(page?: number): Observable<EmployeeDocumentEntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      pin: this.employee.pin,
      page: pageToLoad - 1,
      size: this.itemsPerPage,
    };
    return this.employeeDocumentService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  deleteDocument(employeeDocument: IEmployeeDocument): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employeeDocumentService.delete(employeeDocument.id!).subscribe({
          next: res => {
            swalOnDeleteSuccess();
            this.load();
          },
          error: () => swalOnRequestError(),
        });
      }
    });
  }

  getFontAwesomeIcon(fileContentType: string): IconProp {
    if (fileContentType === 'pdf') {
      return 'file-pdf';
    } else if (fileContentType === 'doc' || fileContentType === 'docx') {
      return 'file-word';
    } else if (fileContentType === 'xlsx' || fileContentType === 'xls') {
      return 'file-excel';
    } else if (fileContentType === 'ppt' || fileContentType === 'pptx') {
      return 'file-powerpoint';
    } else {
      return 'file';
    }
  }

  export(id: number): void {
    this.employeeDocumentService.download(id).subscribe(
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
        // swalClose();
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

  clearForm(): void {
    this.editDocumentsForm.reset();
    this.editDocumentsForm.get('file')!.setValidators([Validators.required]);
    this.editDocumentsForm.get(['pin'])!.setValue(this.editForm.get('pin')!.value);
    this.takeInput!.nativeElement.value = null;
    this.editDocumentsForm.patchValue({
      file: null,
    });
  }

  reset(): void {
    this.page = 1;
    this.employeeDocument = [];
    this.load();
  }

  edit(employeeDocument: IEmployeeDocument): void {
    this.updateEmployeeDocumentForm(employeeDocument);
    this.updateFileInputValidation();
  }

  updateFileInputValidation(): void {
    // remove file validation in update mode
    if (this.editDocumentsForm.get('id')!.value) {
      this.editDocumentsForm.get('file')!.clearValidators();
      this.editDocumentsForm.get('file')!.updateValueAndValidity();
    }
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.isFileSelected = true;
    if (event.target.files != null && event.target.files.length > 0) {
      this.inValidFileErrorMsg = '';
      this.inValidFile = false;
      this.selectedFileToUpload = event.target.files[0];
      const sizeInKB = Number(this.selectedFileToUpload!.size / 1024);
      if (sizeInKB > 15360) {
        this.editDocumentsForm.patchValue({
          file: undefined,
        });
        this.inValidFile = true;
        this.selectedFileToUpload = undefined;
        this.inValidFileErrorMsg = 'Max allowed file size is 15MB';
        return;
      } else {
        this.editDocumentsForm.get('file')!.touched;
        this.dataUtils.loadFileToForm(event, this.editDocumentsForm, field, isImage).subscribe({
          next: () => {},
          error: (err: FileLoadError) => {
            this.eventManager.broadcast(
              new EventWithContent<AlertError>('bitsHrPayrollApp.error', {
                ...err,
                key: 'error.file.' + err.key,
              })
            );
          },
        });
      }
    }
  }

  getEmployeeCategory(category: string): string {
    if (category === EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE) {
      return 'Regular Provisional';
    } else if (category === EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE) {
      return 'Regular';
    } else if (category === EmployeeCategory.CONTRACTUAL_EMPLOYEE) {
      return 'By Contract';
    } else if (category === EmployeeCategory.INTERN) {
      return 'Intern';
    } else {
      return 'Regular';
    }
  }
}
