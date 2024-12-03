import { Component, HostListener, Input, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { PfNomineeFormService } from '../pf-nominee-form.service';
import dayjs from 'dayjs/esm';
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnLoading,
  swalOnRejectionForNomineeReport,
  swalOnRequestError,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnSavedSuccess,
  swalOnUpdatedSuccess,
} from '../../../shared/swal-common/swal-common';
import { IdentityType } from '../../../shared/model/enumerations/identity-type.model';
import { INomineeValidation } from '../../../shared/model/nominee-validation.model';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { IPfNominee, PfNominee } from '../../../shared/legacy/legacy-model/pf-nominee.model';
import { IdentityNumberValidationService, IIdentityNumberValidation } from '../../../shared/service/identity-number-validation.service';
import { NomineeValidationService } from '../../../shared/legacy/legacy-service/nominee-validation.service';
import { PfAccountService } from '../../../shared/legacy/legacy-service/pf-account.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { DateValidationService } from '../../../shared/service/date-validation.service';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { CustomValidator } from '../../../validators/custom-validator';

export const editPageRoute = '/provident-fund-nominee-form';

@Component({
  selector: 'jhi-pf-nominee-form-crud',
  templateUrl: './pf-nominee-form-crud.component.html',
})
export class PfNomineeFormCrudComponent implements OnInit {
  isSaving = false;
  nomineeAge = 0;
  nomineeDOB!: any;

  pfNomineePhoto?: File;
  anyFileSelected!: boolean;
  isValidFileType!: boolean;
  isValidFileSize!: boolean;
  urlForPfNomineeImg!: string;

  totalConsumedPercentage = 0;

  selectedIdentityType: string | null = null;
  selectedGuardianIdentityType: string | null = null;
  showGuardianInputFields = false;

  @Input()
  hideJhiHeader = false;

  editForm = this.fb.group({
    id: [],
    nominationDate: [],
    fullName: [null, [Validators.required, Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    presentAddress: [null, [Validators.required, Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    permanentAddress: [null, [Validators.required, Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    relationship: [null, [Validators.required, Validators.maxLength(250), CustomValidator.naturalTextValidator()]],
    dateOfBirth: [null, [Validators.required]],
    age: [null, [Validators.required, Validators.max(122)]],
    sharePercentage: [null, [Validators.required, Validators.min(1), Validators.max(100)]],

    identityType: [null, [Validators.required]],
    documentName: [null],
    idNumber: [null, [Validators.required]],

    photo: [],
    nomineePhoto: [], //for reset purpose, when saved
    guardianName: [],
    guardianFatherOrSpouseName: [],
    guardianDateOfBirth: [],
    guardianPresentAddress: [],
    guardianPermanentAddress: [],
    guardianIdentityType: [null],
    guardianDocumentName: ['', [CustomValidator.naturalTextValidator()]],
    guardianIdNumber: [null],
    guardianProofOfIdentityOfLegalGuardian: [],
    guardianRelationshipWithNominee: [],

    pfWitnessId: [],
    pfAccountId: [],
  });
  inValidNomineeAge = false;
  invalidGuardianAge = false;
  changeNomineeImage = true;
  pfNominees: IPfNominee[] = [];

  nomineeIDNumberValidation: IIdentityNumberValidation;
  guardianIDNumberValidation: IIdentityNumberValidation;

  dobMaxDate: NgbDateStruct;

  constructor(
    public pfNomineeFormService: PfNomineeFormService,
    public nomineeValidationService: NomineeValidationService,
    public identityNumberValidationService: IdentityNumberValidationService,
    protected pfAccountService: PfAccountService,
    protected employeeService: EmployeeService,
    protected dateValidationService: DateValidationService,
    protected router: Router,
    private fb: FormBuilder,
    protected applicationConfigService: ApplicationConfigService
  ) {
    this.anyFileSelected = false;

    this.nomineeIDNumberValidation = { isValid: true, validationMsg: '' };
    this.guardianIDNumberValidation = { isValid: true, validationMsg: '' };

    this.dobMaxDate = dateValidationService.getDOBMaxDate();
  }

  ngOnInit(): void {
    this.loadPfNomineeList();
    this.loadTotalConsumedSharePercentage();
  }

  ///      List of Nominee    ///

  loadTotalConsumedSharePercentage(): void {
    this.pfNomineeFormService.getTotalConsumedSharePercentageOfCurrentUser().subscribe(res => {
      this.totalConsumedPercentage = res;
    });
  }

  loadPfNomineeList(): void {
    this.pfNomineeFormService.queryForList().subscribe(res => {
      this.pfNominees = res.body!;
    });
  }

  trackId(index: number, item: IPfNominee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  ///     Nominee Form Logic and Validation   ///

  isValidIdentityType(identityType: IdentityType): boolean {
    return (
      identityType === IdentityType.NID ||
      identityType === IdentityType.BIRTH_REGISTRATION ||
      identityType === IdentityType.PASSPORT ||
      identityType === IdentityType.OTHER
    );
  }

  onIdentityChange(): void {
    this.selectedIdentityType = this.editForm.get(['identityType'])!.value;
    this.editForm.get(['idNumber'])!.reset();
    this.editForm.get(['documentName'])!.reset();
    this.updateValidationOnNomineeIdentityType();
  }

  calculateAge(): void {
    if (dayjs(this.editForm.get('dateOfBirth')!.value).isAfter(dayjs())) {
      this.inValidNomineeAge = true;
      return;
    } else {
      this.inValidNomineeAge = false;
    }

    const date = this.editForm.get('dateOfBirth')!.value;
    const identityType = this.editForm.get('identityType')!.value;
    const documentName = this.editForm.get('documentName')!.value;
    const idNumber = this.editForm.get('idNumber')!.value;

    if (date !== null && date !== undefined) {
      const birthDate = new Date(this.editForm.get('dateOfBirth')!.value);
      const todayDate = new Date();

      let years = todayDate.getFullYear() - birthDate.getFullYear();

      if (
        todayDate.getMonth() < birthDate.getMonth() ||
        (todayDate.getMonth() === birthDate.getMonth() && todayDate.getDate() < birthDate.getDate())
      ) {
        years--;
      }
      this.nomineeAge = years;
      this.editForm.get('age')!.setValue(this.nomineeAge as any);

      if (this.nomineeAge < 18 && this.nomineeAge >= 0) {
        this.showGuardianInputFields = true;
      } else {
        this.showGuardianInputFields = false;
      }
      this.updateValidationForGuardian();

      if (identityType === IdentityType.NID && this.nomineeAge < 18) {
        this.editForm.get('identityType')!.reset();
        this.editForm.get('idNumber')!.reset();
      }
    } else {
      this.showGuardianInputFields = false;
      this.nomineeAge = 0;
    }
  }

  shouldShowNomineeIdNumberField(): boolean {
    const nomineeIdentityType = this.editForm.get('identityType')!.value;
    return this.isValidIdentityType(nomineeIdentityType);
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

  updateValidationOnNomineeIdentityType(): void {
    const identityType = this.editForm.get(['identityType'])!.value;

    if (identityType === IdentityType.OTHER) {
      this.editForm.get('documentName')!.setValidators([Validators.required, Validators.maxLength(200)]);
    } else {
      this.editForm.get('documentName')!.clearValidators();
    }
    this.editForm.get('documentName')!.updateValueAndValidity();
  }

  clearForm(): void {
    this.editForm.reset();
    this.loadTotalConsumedSharePercentage();
    // if (this.nomineeAge < 18 && this.nomineeAge >= 0) {
    //   this.showGuardianInputFields = true;
    // } else {
    //   this.showGuardianInputFields = false;
    // }
    this.showGuardianInputFields = false;
    this.anyFileSelected = false;
  }

  ///      Update            ///

  updateForm(pfNominee: IPfNominee): void {
    this.editForm.patchValue({
      id: pfNominee.id,
      nominationDate: pfNominee.nominationDate,
      fullName: pfNominee.fullName as any,
      presentAddress: pfNominee.presentAddress as any,
      permanentAddress: pfNominee.permanentAddress as any,
      relationship: pfNominee.relationship as any,
      dateOfBirth: pfNominee.dateOfBirth as any,
      age: pfNominee.age as any,
      sharePercentage: pfNominee.sharePercentage as any,

      identityType: pfNominee.identityType as any,
      documentName: pfNominee.documentName,
      idNumber: pfNominee.idNumber as any,

      photo: pfNominee.photo,
      guardianName: pfNominee.guardianName,
      guardianFatherOrSpouseName: pfNominee.guardianFatherOrSpouseName,
      guardianDateOfBirth: pfNominee.guardianDateOfBirth,
      guardianPresentAddress: pfNominee.guardianPresentAddress,
      guardianPermanentAddress: pfNominee.guardianPermanentAddress,
      guardianProofOfIdentityOfLegalGuardian: pfNominee.guardianProofOfIdentityOfLegalGuardian,
      guardianIdentityType: pfNominee.guardianIdentityType,
      guardianDocumentName: pfNominee.guardianDocumentName,
      guardianIdNumber: pfNominee.guardianIdNumber,
      guardianRelationshipWithNominee: pfNominee.guardianRelationshipWithNominee,
      pfWitnessId: pfNominee.pfWitnessId,
      pfAccountId: pfNominee.pfAccountId,
    });
  }

  onClickPopulateForm(pfNominee: IPfNominee): void {
    if (pfNominee.id) {
      this.clearForm();
      this.updateForm(pfNominee);
      this.loadSavedImage(pfNominee.id);
    }
  }

  ///      Navigation        ///

  redirectToPfNomineeReport(): void {
    this.loadTotalConsumedSharePercentage();

    if (this.totalConsumedPercentage !== 100) {
      swalOnRejectionForNomineeReport();
    } else {
      this.router.navigate(['provident-fund-nominee-form/print']);
    }
  }

  previousState(): void {
    window.history.back();
  }

  ///      Save              ///

  preValidation(): void {
    const nominee = this.createFromForm();
    this.nomineeValidationService.queryForPreValidatePFNominee(nominee).subscribe(res => {
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
    swalOnLoading('Verifying & Saving..');
    this.isSaving = true;
    const pfNominee = this.createFromForm();
    if (pfNominee.id !== undefined && pfNominee.id !== null) {
      if (this.pfNomineePhoto !== undefined && this.pfNomineePhoto !== null) {
        this.subscribeToSaveResponse(this.pfNomineeFormService.update(this.pfNomineePhoto, pfNominee));
        this.pfNomineePhoto = undefined;
      } else {
        this.subscribeToSaveResponse(this.pfNomineeFormService.updateWithOutFile(pfNominee));
      }
    } else {
      if (this.pfNomineePhoto !== undefined && this.pfNomineePhoto !== null) {
        this.subscribeToSaveResponse(this.pfNomineeFormService.create(this.pfNomineePhoto, pfNominee));
        this.pfNomineePhoto = undefined;
      }
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfNominee>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      res => this.onSaveError(res)
    );
  }

  protected onSaveSuccess(): void {
    if (!this.editForm.get('id')!.value) {
      swalOnSavedSuccess();
    } else {
      swalOnUpdatedSuccess();
    }
    this.isSaving = false;
    this.loadPfNomineeList();
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

  private createFromForm(): IPfNominee {
    return {
      ...new PfNominee(),
      id: this.editForm.get(['id'])!.value,
      nominationDate: this.editForm.get(['nominationDate'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      presentAddress: this.editForm.get(['presentAddress'])!.value,
      permanentAddress: this.editForm.get(['permanentAddress'])!.value,
      relationship: this.editForm.get(['relationship'])!.value,
      dateOfBirth: this.editForm.get(['dateOfBirth'])!.value,
      age: this.editForm.get(['age'])!.value,
      sharePercentage: this.editForm.get(['sharePercentage'])!.value,

      identityType: this.editForm.get(['identityType'])!.value,
      documentName: this.editForm.get(['documentName'])!.value,
      idNumber: this.editForm.get(['idNumber'])!.value,

      photo: this.editForm.get(['photo'])!.value,
      guardianName: this.editForm.get(['guardianName'])!.value,
      guardianFatherOrSpouseName: this.editForm.get(['guardianFatherOrSpouseName'])!.value,
      guardianDateOfBirth: this.editForm.get(['guardianDateOfBirth'])!.value,
      guardianPresentAddress: this.editForm.get(['guardianPresentAddress'])!.value,
      guardianPermanentAddress: this.editForm.get(['guardianPermanentAddress'])!.value,

      guardianIdentityType: this.editForm.get(['guardianIdentityType'])!.value,
      guardianDocumentName: this.editForm.get(['guardianDocumentName'])!.value,
      guardianIdNumber: this.editForm.get(['guardianIdNumber'])!.value,

      guardianProofOfIdentityOfLegalGuardian: this.editForm.get(['guardianProofOfIdentityOfLegalGuardian'])!.value,
      guardianRelationshipWithNominee: this.editForm.get(['guardianRelationshipWithNominee'])!.value,

      pfWitnessId: this.editForm.get(['pfWitnessId'])!.value,
      pfAccountId: this.editForm.get(['pfAccountId'])!.value,
    };
  }

  ///      Delete              ///

  delete(pfNominee: any): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.pfNomineeFormService.delete(pfNominee.id).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.loadPfNomineeList();
            this.clearForm();
            this.router.navigate([editPageRoute]);
          },
          () => this.requestFailed()
        );
      }
    });
  }

  requestFailed(): void {
    swalOnRequestError();
  }

  ///      Nominee Image Logic              ///

  loadSavedImage(id: number): void {
    this.urlForPfNomineeImg = this.getPfNomineeImage(id);
    this.anyFileSelected = true;
    this.changeNomineeImage = false;
    this.isValidFileType = true;
    this.isValidFileSize = true;
    this.editForm.get('photo')!.clearValidators();
    this.editForm.get('photo')!.updateValueAndValidity();
  }

  getPfNomineeImage(pfNomineeId: number): string {
    const url = this.applicationConfigService.getEndpointFor('files/nominee-image/' + pfNomineeId);
    return url;
  }

  onChangeSelectPfNomineeImage(event: any): void {
    /* validation: [file type: {image/jpeg}, file size: 2MB,] */
    this.isValidFileSize = true;
    this.isValidFileType = true;
    if (event.target.files != null && event.target.files.length > 0) {
      const file = event.target.files[0];

      file && file.type === 'image/jpeg' ? (this.isValidFileType = true) : (this.isValidFileType = false);

      if (this.isValidFileType) {
        const sizeInKB = Number(file.size / 1024);
        sizeInKB <= 2048 ? (this.isValidFileSize = true) : (this.isValidFileSize = false);

        if (this.isValidFileSize) {
          this.pfNomineePhoto = file;
          this.anyFileSelected = true;
        }
      }
    } else {
      this.anyFileSelected = false;
    }
  }

  changeToReUpload(): void {
    this.changeNomineeImage = true;
    this.anyFileSelected = false;
  }

  ///      Guardian Logic and Validation     ///

  checkGuardianAge(): void {
    if (dayjs(this.editForm.get('guardianDateOfBirth')!.value).isAfter(dayjs())) {
      this.invalidGuardianAge = true;
    } else {
      this.invalidGuardianAge = false;
    }
  }

  updateValidationForGuardian(): void {
    const guardianNameFormControl = this.editForm.get('guardianName');
    const guardianDateOfBirthFormControl = this.editForm.get('guardianDateOfBirth');
    const guardianFatherOrSpouseNameFormControl = this.editForm.get('guardianFatherOrSpouseName');
    const guardianPresentAddressFormControl = this.editForm.get('guardianPresentAddress');
    const guardianPermanentAddressFormControl = this.editForm.get('guardianPermanentAddress');
    const guardianRelationshipFormControl = this.editForm.get('guardianRelationshipWithNominee');
    const guardianIdentityTypeFormControl = this.editForm.get('guardianIdentityType');
    const guardianIdNumberFormControl = this.editForm.get('guardianIdNumber');
    const guardianDocumentNameFormControl = this.editForm.get('guardianDocumentName');

    const fields = [
      guardianNameFormControl,
      guardianDateOfBirthFormControl,
      guardianFatherOrSpouseNameFormControl,
      guardianPresentAddressFormControl,
      guardianRelationshipFormControl,
      guardianPermanentAddressFormControl,
      guardianIdentityTypeFormControl,
      guardianDocumentNameFormControl,
      guardianIdNumberFormControl,
    ];

    if (this.nomineeAge < 18) {
      guardianNameFormControl!.setValidators([Validators.required, Validators.maxLength(250)]);
      guardianDateOfBirthFormControl!.setValidators([Validators.required]);
      guardianFatherOrSpouseNameFormControl!.setValidators([Validators.required, Validators.maxLength(250)]);
      guardianPresentAddressFormControl!.setValidators([Validators.required, Validators.maxLength(250)]);
      guardianPermanentAddressFormControl!.setValidators([Validators.required, Validators.maxLength(250)]);
      guardianRelationshipFormControl!.setValidators([Validators.required, Validators.maxLength(250)]);
      guardianIdentityTypeFormControl!.setValidators([Validators.required]);
      guardianIdNumberFormControl!.setValidators([Validators.required]);
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

  onGuardianIdentityChange(): void {
    this.selectedGuardianIdentityType = this.editForm.get(['guardianIdentityType'])!.value;
    this.editForm.get(['guardianIdNumber'])!.reset();
    this.editForm.get(['guardianDocumentName'])!.reset();
    this.updateValidationOnGuardianIdentity();
  }

  updateValidationOnGuardianIdentity(): void {
    const identityType = this.editForm.get(['guardianIdentityType'])!.value;
    if (identityType === IdentityType.OTHER) {
      this.editForm.get('guardianDocumentName')!.setValidators([Validators.required, Validators.maxLength(200)]);
    } else {
      this.editForm.get('guardianDocumentName')!.clearValidators();
    }
    this.editForm.get('guardianDocumentName')!.updateValueAndValidity();
  }

  shouldShowGuardianIdNumberField(): boolean {
    const guardianIdentityType = this.editForm.get('guardianIdentityType')!.value;
    return this.isValidIdentityType(guardianIdentityType);
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

  // Escape button reset form  ///

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    if (!this.editForm.get('id')!.value) {
      this.editForm.reset();
      this.calculateAge();
    }
  }
}
