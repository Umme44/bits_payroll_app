import { Component, HostListener, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';

import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnLoading,
  swalOnRejectionForNomineeReport,
  swalOnRequestError,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnSavedSuccess,
} from '../../shared/swal-common/swal-common';

import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { INominee, Nominee } from '../../shared/legacy/legacy-model/nominee.model';
import { NomineeService } from '../../shared/legacy/legacy-service/nominee.service';
import { EmployeeService } from '../../shared/legacy/legacy-service/employee.service';
import { NomineeType } from '../../shared/model/enumerations/nominee-type.model';
import { IdentityType } from '../../shared/model/enumerations/identity-type.model';
import { IIdentityNumberValidation } from '../../shared/legacy/legacy-service/identity-number-validation.service';
import { NomineeValidationService } from '../../shared/legacy/legacy-service/nominee-validation.service';
import { IdentityNumberValidationService } from '../../shared/service/identity-number-validation.service';
import { INomineeValidation } from '../../shared/legacy/legacy-model/nominee-validation.model';
import { ACCESS_GROUP_EMPLOYEE } from '../nominee-management.route';
import { DateValidationService } from '../../shared/legacy/legacy-service/date-validation.service';
import { CustomValidator } from '../../validators/custom-validator';
import { SearchModalComponent } from '../../shared/specialized-search/search-modal/search-modal.component';
import { SearchModalService } from '../../shared/specialized-search/search-modal/search-modal.service';
import { EmployeeCommonService } from '../../common/employee-address-book/employee-common.service';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Component({
  selector: 'jhi-nominee-crud-component',
  templateUrl: './nominee-crud.component.html',
})
export class NomineeCrudComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];
  dateOfBirthDp: any;
  guardianDateOfBirthDp: any;
  nominationDateDp: any;
  @Input()
  nomineeType!: NomineeType;
  identityType!: IdentityType;
  selectedIdentityType: string | null = null;
  selectedGuardianIdentityType: string | null = null;
  @Input()
  accessGroup = '';
  totalConsumedPercentage = 0;
  selectedEmployeeId!: number;
  inValidFile = false;
  inValidFileErrorMsg = '';
  isNomineeImageMissing = true;
  nomineeAge = 0;
  showGuardianInputFields = false;
  nomineeImage?: File;

  dobMaxDate: NgbDateStruct;

  editForm = this.fb.group({
    id: [],
    nomineeName: ['', [CustomValidator.naturalTextValidator()]],
    permanentAddress: ['', [CustomValidator.naturalTextValidator()]],
    presentAddress: ['', [CustomValidator.naturalTextValidator()]],
    relationshipWithEmployee: ['', [CustomValidator.naturalTextValidator()]],
    dateOfBirth: [],
    age: [],
    sharePercentage: [],

    identityType: [],
    documentName: [],
    idNumber: [],

    nidNumber: [],
    passportNumber: [],
    brNumber: [''],
    imagePath: [],
    nomineeImageFile: [],

    guardianName: ['', [CustomValidator.naturalTextValidator()]],
    guardianFatherName: ['', [CustomValidator.naturalTextValidator()]],
    guardianSpouseName: ['', [CustomValidator.naturalTextValidator()]],
    guardianDateOfBirth: [],
    guardianPresentAddress: ['', [CustomValidator.naturalTextValidator()]],
    guardianPermanentAddress: ['', [CustomValidator.naturalTextValidator()]],
    guardianRelationshipWith: ['', [CustomValidator.naturalTextValidator()]],

    guardianIdentityType: [],
    guardianDocumentName: ['', [CustomValidator.naturalTextValidator()]],
    guardianIdNumber: [],

    guardianImagePath: [],
    isLocked: [],
    nomineeType: [],
    nominationDate: [],

    employeeId: [],
    approvedById: [],
    witnessId: [],
    memberId: [],
  });
  nominees: INominee[] = [];

  urlForNomineeImg!: string;
  changeNomineeImage = true;
  invalidGuardianAge = false;

  nomineeIDNumberValidation: IIdentityNumberValidation;
  guardianIDNumberValidation: IIdentityNumberValidation;

  @Input()
  hideJhiHeader = false;

  constructor(
    public nomineeService: NomineeService,
    public nomineeValidationService: NomineeValidationService,
    public identityNumberValidationService: IdentityNumberValidationService,
    private searchModalService: SearchModalService,
    protected modalService: NgbModal,
    protected applicationConfigService: ApplicationConfigService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    private fb: FormBuilder,
    private dateValidationService: DateValidationService
  ) {
    this.nomineeIDNumberValidation = { isValid: true, validationMsg: '' };
    this.guardianIDNumberValidation = { isValid: true, validationMsg: '' };

    this.dobMaxDate = dateValidationService.getDOBMaxDate();
  }

  ngOnInit(): void {
    this.loadNomineeList();

    if (this.nomineeType) {
      this.editForm.get('nomineeType')!.setValue(this.nomineeType);
    }

    this.loadUsedSharePercentage();

    this.updateForm({ ...new Nominee() });
    this.editForm.get('identityType')!.setValue(null);
    this.editForm.get('guardianIdentityType')!.setValue(null);

    // if (nominee.identityType === IdentityType.OTHER) {
    //   this.selectedIdentityType = 'OTHER';
    //   this.updateValidationOnNomineeIdentity();
    // }
    //
    // if (nominee.guardianIdentityType === IdentityType.OTHER) {
    //   this.selectedGuardianIdentityType = 'OTHER';
    //   this.updateValidationOnGuardianIdentityType();
    // }

    // if (nominee.age) {
    //   this.nomineeAge = nominee.age;
    //   this.updateValidationForGuardian();
    // }

    this.editForm.get('age')!.disable();
  }
  ///      List of Nominee    ///

  loadNomineeList(): void {
    const nominee: INominee = {
      ...new Nominee(),
      nomineeType: this.nomineeType,
    };
    if (this.accessGroup === ACCESS_GROUP_EMPLOYEE) {
      this.nomineeService.getNomineeListCommon(nominee).subscribe(res => {
        this.nominees = res.body || [];
      });
    }
  }

  loadUsedSharePercentage(): void {
    const nominee: INominee = {
      ...new Nominee(),
      nomineeType: this.nomineeType,
    };
    if (this.accessGroup === ACCESS_GROUP_EMPLOYEE) {
      this.nomineeService.getRemainingPercentageCommon(nominee).subscribe(res => {
        this.totalConsumedPercentage = 100 - res;
      });
    }
  }

  trackNomineeId(index: number, item: INominee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  ///     Nominee Form Logic and Validation   ///

  onIdentityChange(): void {
    this.selectedIdentityType = this.editForm.get(['identityType'])!.value;
    this.editForm.get(['idNumber'])!.reset();
    this.editForm.get(['idNumber'])!.updateValueAndValidity();
    this.updateValidationOnNomineeIdentity();
  }

  onChangeIdentityNumberValidation(): void {
    const idNumber = this.editForm.get(['idNumber'])!.value;
    const identityType: IdentityType = this.editForm.get(['identityType'])!.value;
    if (idNumber && identityType) {
      if (identityType === IdentityType.NID) {
        this.nomineeIDNumberValidation = this.identityNumberValidationService.isValidNID(idNumber);
      } else if (identityType === IdentityType.PASSPORT) {
        this.nomineeIDNumberValidation = this.identityNumberValidationService.isValidPassport(idNumber);
      } else if (identityType === IdentityType.BIRTH_REGISTRATION) {
        this.nomineeIDNumberValidation = this.identityNumberValidationService.isValidBRN(idNumber);
      } else if (identityType === IdentityType.OTHER) {
        this.nomineeIDNumberValidation = this.identityNumberValidationService.otherIDValidation(idNumber);
      }
    } else {
      this.nomineeIDNumberValidation = { isValid: true, validationMsg: '' };
    }
  }

  updateValidationOnNomineeIdentity(): void {
    const identityType = this.editForm.get(['identityType'])!.value;

    if (identityType === IdentityType.OTHER) {
      this.editForm.get('documentName')!.setValidators([Validators.required, Validators.minLength(0), Validators.maxLength(255)]);
    } else {
      this.editForm.get('documentName')!.clearValidators();
    }
    this.editForm.get('documentName')!.updateValueAndValidity();
  }

  onChangeDateOfBirth($event: any): void {
    const dateOfBirth = this.editForm.get('dateOfBirth')!.value;
    if (dateOfBirth) {
      this.nomineeAge = this.nomineeService.calculateAge(new Date(dateOfBirth));
      if (this.nomineeAge < 18 && this.nomineeAge >= 0) {
        this.showGuardianInputFields = true;
      } else {
        this.showGuardianInputFields = false;
      }
      this.editForm.get('age')!.setValue(this.nomineeAge);

      const identityType = this.editForm.get('identityType')!.value;
      const idNumber = this.editForm.get('idNumber')!.value;

      if (identityType === IdentityType.NID && this.nomineeAge < 18) {
        this.editForm.get('identityType')!.reset();
        this.editForm.get('idNumber')!.reset();
      }

      this.updateValidationForGuardian();
    } else {
      this.editForm.get('age')!.setValue(0);
      this.showGuardianInputFields = false;
    }
  }

  isValidIdentityType(identityType: IdentityType): boolean {
    return (
      identityType === IdentityType.NID ||
      identityType === IdentityType.BIRTH_REGISTRATION ||
      identityType === IdentityType.PASSPORT ||
      identityType === IdentityType.OTHER
    );
  }

  shouldShowNomineeIdNumberField(): boolean {
    const nomineeIdentityType = this.editForm.get('identityType')!.value;
    return this.isValidIdentityType(nomineeIdentityType);
  }

  clearForm(): void {
    this.editForm.reset();

    this.loadUsedSharePercentage();

    this.showGuardianInputFields = false;
  }

  ///      Update            ///

  updateForm(nominee: INominee): void {
    this.editForm.patchValue({
      id: nominee.id,
      nomineeName: nominee.nomineeName,
      presentAddress: nominee.presentAddress,
      relationshipWithEmployee: nominee.relationshipWithEmployee,
      dateOfBirth: nominee.dateOfBirth,
      age: nominee.age,
      sharePercentage: nominee.sharePercentage,

      identityType: nominee.identityType,
      documentName: nominee.documentName,
      idNumber: nominee.idNumber,

      imagePath: nominee.imagePath,
      guardianName: nominee.guardianName,
      guardianFatherName: nominee.guardianFatherName,
      guardianSpouseName: nominee.guardianSpouseName,
      guardianDateOfBirth: nominee.guardianDateOfBirth,
      guardianPresentAddress: nominee.guardianPresentAddress,

      guardianIdentityType: nominee.guardianIdentityType,
      guardianDocumentName: nominee.guardianDocumentName,
      guardianIdNumber: nominee.guardianIdNumber,

      // guardianProofIdentity: nominee.guardianDocumentName,
      guardianRelationshipWith: nominee.guardianRelationshipWith,

      guardianImagePath: nominee.guardianImagePath,
      isLocked: nominee.isLocked,
      nomineeType: nominee.nomineeType,
      nominationDate: nominee.nominationDate,
      permanentAddress: nominee.permanentAddress,
      guardianPermanentAddress: nominee.guardianPermanentAddress,

      employeeId: nominee.employeeId,
      approvedById: nominee.approvedById,
      witnessId: nominee.witnessId,
      memberId: nominee.memberId,
    });
  }

  onClickPopulateForm(nominee: INominee): void {
    if (nominee.id) {
      const nomineeId = nominee.id;
      this.editForm.reset();
      this.nomineeService.findCommon(nomineeId).subscribe(res => {
        this.updateForm(res.body!);
        this.loadSavedImage(nomineeId);
      });
    }
  }

  ///      Navigation        ///

  redirectToNomineeReport(): void {
    if (this.accessGroup === ACCESS_GROUP_EMPLOYEE && this.nomineeType === NomineeType.GRATUITY_FUND) {
      this.loadUsedSharePercentage();

      if (this.totalConsumedPercentage !== 100) {
        swalOnRejectionForNomineeReport();
      } else {
        this.router.navigate(['/my-nominee/gf/report']);
      }
    } else if (this.accessGroup === ACCESS_GROUP_EMPLOYEE && this.nomineeType === NomineeType.GENERAL) {
      this.loadUsedSharePercentage();

      if (this.totalConsumedPercentage !== 100) {
        swalOnRejectionForNomineeReport();
      } else {
        this.router.navigate(['/my-nominee/general/report']);
      }
    }
  }

  previousState(): void {
    window.history.back();
  }

  onClickRedirectToViewPage(nominee: INominee): void {
    if (nominee.id) {
      if (this.nomineeType === NomineeType.GRATUITY_FUND && this.accessGroup === ACCESS_GROUP_EMPLOYEE) {
        this.router.navigate(['/my-nominee/gf/' + nominee.id + '/view']);
      } else if (this.nomineeType === NomineeType.GENERAL && this.accessGroup === ACCESS_GROUP_EMPLOYEE) {
        this.router.navigate(['/my-nominee/general/' + nominee.id + '/view']);
      }
    }
  }

  ///      Save              ///

  preValidation(): void {
    const nominee = this.createFromForm();
    this.nomineeValidationService.queryForPreValidateNominee(nominee).subscribe(res => {
      this.alertForPreValidation(res.body!);
    });
  }

  alertForPreValidation(nomineeValidation: INomineeValidation): void {
    if (nomineeValidation.doesSharePercentageExceed) {
      const userInputSharePercentage = Number(this.editForm.get('sharePercentage')!.value);
      const remaining = nomineeValidation.remainingSharePercentage!;
      if (userInputSharePercentage > remaining) {
        this.nomineeValidationService.swalValidationMsg("Total share can't be more than 100%. Remaining only " + remaining + '% Share!');
      }
    } else {
      this.save();
    }
  }

  save(): void {
    this.isSaving = true;
    swalOnLoading('Verifying & Saving..');
    const nominee = this.createFromForm();
    if (nominee.id !== undefined && nominee.id !== null) {
      if (this.nomineeImage !== null && this.nomineeImage !== undefined) {
        this.subscribeToSaveResponse(this.nomineeService.updateCommonWithFile(this.nomineeImage, nominee));
        this.nomineeImage = undefined;
      } else {
        this.subscribeToSaveResponse(this.nomineeService.updateCommon(nominee));
      }
    } else {
      if (this.nomineeImage !== null && this.nomineeImage !== undefined) {
        this.subscribeToSaveResponse(this.nomineeService.createCommon(this.nomineeImage, nominee));
        this.nomineeImage = undefined;
      }
    }
  }

  private createFromForm(): INominee {
    return {
      ...new Nominee(),
      id: this.editForm.get(['id'])!.value,
      nomineeName: this.editForm.get(['nomineeName'])!.value,
      presentAddress: this.editForm.get(['presentAddress'])!.value,
      relationshipWithEmployee: this.editForm.get(['relationshipWithEmployee'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
      age: this.editForm.get(['age'])!.value,
      sharePercentage: this.editForm.get(['sharePercentage'])!.value,

      identityType: this.editForm.get(['identityType'])!.value,
      documentName: this.editForm.get(['documentName'])!.value,
      idNumber: this.editForm.get(['idNumber'])!.value,

      imagePath: this.editForm.get(['imagePath'])!.value,
      guardianName: this.editForm.get(['guardianName'])!.value,
      guardianFatherName: this.editForm.get(['guardianFatherName'])!.value,
      guardianSpouseName: this.editForm.get(['guardianSpouseName'])!.value,
      guardianDateOfBirth: this.editForm.get(['guardianDateOfBirth'])!.value,
      guardianPresentAddress: this.editForm.get(['guardianPresentAddress'])!.value,

      guardianIdentityType: this.editForm.get(['guardianIdentityType'])!.value,
      guardianDocumentName: this.editForm.get(['guardianDocumentName'])!.value,
      guardianIdNumber: this.editForm.get(['guardianIdNumber'])!.value,

      guardianRelationshipWith: this.editForm.get(['guardianRelationshipWith'])!.value,

      guardianImagePath: this.editForm.get(['guardianImagePath'])!.value,
      isLocked: this.editForm.get(['isLocked'])!.value,
      // nomineeType: this.editForm.get(['nomineeType'])!.value,
      nomineeType: this.nomineeType,
      nominationDate: this.editForm.get(['nominationDate'])!.value,
      permanentAddress: this.editForm.get(['permanentAddress'])!.value,
      guardianPermanentAddress: this.editForm.get(['guardianPermanentAddress'])!.value,

      employeeId: this.editForm.get(['employeeId'])!.value,
      approvedById: this.editForm.get(['approvedById'])!.value,
      witnessId: this.editForm.get(['witnessId'])!.value,
      memberId: this.editForm.get(['memberId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INominee>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      res => this.onSaveError(res)
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    swalOnSavedSuccess();
    this.loadNomineeList();

    this.clearForm();
    this.showGuardianInputFields = false;
  }

  protected onSaveError(res: any): void {
    this.isSaving = false;
    swalOnRequestErrorWithBackEndErrorTitle(res.error.title);
    if (res.error.errorKey === 'nidVerificationFailed' || res.error.errorKey === 'guardianNidVerificationFailed') {
      this.clearForm();
    }
  }

  ///      Delete              ///

  delete(nominee: any): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        if (this.accessGroup === ACCESS_GROUP_EMPLOYEE) {
          this.nomineeService.deleteCommon(nominee.id).subscribe(
            () => {
              swalOnDeleteSuccess();
              this.loadNomineeList();
              this.clearForm();
            },
            () => this.requestFailed()
          );
        }
      }
    });
  }

  requestFailed(): void {
    swalOnRequestError();
  }

  ///      Nominee Image Logic              ///

  onChangeSelectNomineeImage(event: any): void {
    /* validation: [file type: {image/jpeg}, file size: 2MB,] */
    if (event.target.files != null && event.target.files.length > 0) {
      this.inValidFileErrorMsg = '';
      this.inValidFile = false;
      const file = event.target.files[0];

      if (!file || file.type !== 'image/jpeg') {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'Select JPG/JPEG image only';
        return;
      }
      const sizeInKB = Number(file.size / 1024);
      if (sizeInKB > 2048) {
        this.inValidFile = true;
        this.inValidFileErrorMsg = 'Max allowed file size is 2MB';
        return;
      }
      this.nomineeImage = file;
      this.isNomineeImageMissing = false;
    }
    return;
  }

  loadSavedImage(id: number): void {
    this.urlForNomineeImg = this.getNomineeImage(id);
    this.changeNomineeImage = false;
    this.isNomineeImageMissing = false;
    this.inValidFile = false;
  }

  getNomineeImage(nomineeId: number): string {
    const url = SERVER_API_URL + 'files/common/nominee-image/' + nomineeId;
    return url;
  }

  changeToReUpload(): void {
    this.changeNomineeImage = true;
    this.isNomineeImageMissing = true;
  }

  ///      Guardian Logic and Validation     ///

  shouldShowGuardianIdNumberField(): boolean {
    const guardianIdentityType = this.editForm.get('guardianIdentityType')!.value;
    return this.isValidIdentityType(guardianIdentityType);
  }

  onGuardianIdentityChange(): void {
    this.selectedGuardianIdentityType = this.editForm.get(['guardianIdentityType'])!.value;
    this.editForm.get(['guardianIdNumber'])!.reset();
    this.updateValidationOnGuardianIdentityType();
  }

  updateValidationOnGuardianIdentityType(): void {
    const identityType = this.editForm.get(['guardianIdentityType'])!.value;

    if (identityType === IdentityType.OTHER) {
      this.editForm
        .get('guardianDocumentName')!
        .setValidators([Validators.required, Validators.minLength(0), Validators.maxLength(255), CustomValidator.naturalTextValidator()]);
    } else {
      this.editForm.get('guardianDocumentName')!.clearValidators();
    }
    this.editForm.get('guardianDocumentName')!.updateValueAndValidity();
  }

  checkGuardianAge(): void {
    if (dayjs(this.editForm.get('guardianDateOfBirth')!.value).isAfter(dayjs())) {
      this.invalidGuardianAge = true;
    } else {
      this.invalidGuardianAge = false;
    }
  }

  onChangeGuardianIdentityNumberValidation(): void {
    const idNumber = this.editForm.get(['guardianIdNumber'])!.value;
    const identityType: IdentityType = this.editForm.get(['guardianIdentityType'])!.value;
    if (idNumber && identityType) {
      if (identityType === IdentityType.NID) {
        this.guardianIDNumberValidation = this.identityNumberValidationService.isValidNID(idNumber);
      } else if (identityType === IdentityType.PASSPORT) {
        this.guardianIDNumberValidation = this.identityNumberValidationService.isValidPassport(idNumber);
      } else if (identityType === IdentityType.BIRTH_REGISTRATION) {
        this.guardianIDNumberValidation = this.identityNumberValidationService.isValidBRN(idNumber);
      } else if (identityType === IdentityType.OTHER) {
        this.guardianIDNumberValidation = this.identityNumberValidationService.otherIDValidation(idNumber);
      }
    } else {
      this.guardianIDNumberValidation = { isValid: true, validationMsg: '' };
    }
  }

  updateValidationForGuardian(): void {
    const guardianNameFormControl = this.editForm.get('guardianName');
    const guardianFatherNameFormControl = this.editForm.get('guardianFatherName');
    const guardianSpouseNameFormControl = this.editForm.get('guardianSpouseName');
    const guardianDateOfBirthFormControl = this.editForm.get('guardianDateOfBirth');
    const guardianPresentAddressFormControl = this.editForm.get('guardianPresentAddress');
    const guardianPermanentAddressFormControl = this.editForm.get('guardianPermanentAddress');
    const guardianIdentityTypeFormControl = this.editForm.get('guardianIdentityType');
    const guardianDocumentNameFormControl = this.editForm.get('guardianDocumentName');
    const guardianIdNumberFormControl = this.editForm.get('guardianIdNumber');
    const guardianRelationshipFormControl = this.editForm.get('guardianRelationshipWith');

    const fields = [
      guardianNameFormControl,
      guardianFatherNameFormControl,
      guardianSpouseNameFormControl,
      guardianDateOfBirthFormControl,
      guardianPresentAddressFormControl,
      guardianPermanentAddressFormControl,
      guardianIdentityTypeFormControl,
      guardianDocumentNameFormControl,
      guardianIdNumberFormControl,
      guardianRelationshipFormControl,
    ];

    if (this.nomineeAge < 18) {
      guardianNameFormControl!.setValidators([Validators.required, Validators.maxLength(250), CustomValidator.naturalTextValidator()]);
      guardianDateOfBirthFormControl!.setValidators([Validators.required]);
      guardianPresentAddressFormControl!.setValidators([
        Validators.required,
        Validators.maxLength(250),
        CustomValidator.naturalTextValidator(),
      ]);
      guardianPermanentAddressFormControl!.setValidators([
        Validators.required,
        Validators.maxLength(250),
        CustomValidator.naturalTextValidator(),
      ]);
      guardianIdentityTypeFormControl!.setValidators([Validators.required]);
      guardianIdNumberFormControl!.setValidators([Validators.required]);
      guardianRelationshipFormControl!.setValidators([
        Validators.required,
        Validators.maxLength(250),
        CustomValidator.naturalTextValidator(),
      ]);
      this.onChangeGuardianIdentityNumberValidation();
    } else {
      // reset fields
      fields.forEach(x => {
        x!.clearValidators();
        x!.reset();
      });
      this.guardianIDNumberValidation = { isValid: true, validationMsg: '' };
    }

    // update fields value and validity
    fields.forEach(x => {
      x!.updateValueAndValidity();
    });
  }

  // Escape button reset form  ///

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    if (!this.editForm.get('id')!.value) {
      this.editForm.reset();
    }
  }
}
