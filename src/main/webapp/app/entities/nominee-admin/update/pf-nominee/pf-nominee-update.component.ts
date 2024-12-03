import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import dayjs from 'dayjs/esm';

import Swal from 'sweetalert2';
import { IPfAccount } from '../../../pf-account/pf-account.model';
import { IEmployee } from '../../../employee-custom/employee-custom.model';
import { IPfNominee } from '../../pf-nominee.model';
import { IdentityNumberValidationService, IIdentityNumberValidation } from '../../../../shared/service/identity-number-validation.service';
import { PfNomineeService } from '../../service/pf-nominee.service';
import { PfNomineeFormService } from '../edit-form-service/pf-nominee-form.service';
import { PfAccountService } from '../../../pf-account/service/pf-account.service';
import { EmployeeCustomService } from '../../../employee-custom/service/employee-custom.service';
import { DateValidationService } from '../../../../shared/service/date-validation.service';
import { IdentityType } from '../../../../shared/model/enumerations/identity-type.model';
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRejectionForNomineeReport,
  swalOnSavedSuccess,
  swalOnUpdatedSuccess,
} from '../../../../shared/swal-common/swal-common';

type SelectableEntity = IPfAccount | IEmployee;

@Component({
  selector: 'jhi-pf-nominee-update',
  templateUrl: './pf-nominee-update.component.html',
})
export class PfNomineeUpdateComponent implements OnInit, OnDestroy {
  isSaving = false;
  pfAccounts: IPfAccount[] = [];
  employees: IEmployee[] = [];
  pfNominees: IPfNominee[] = [];
  pfNominee!: IPfNominee;
  allowedSharePercentage = 1;
  minimumSharePercentage = 50;
  maximumSharePercentage = 100;
  anyFileSelected = false;
  urlForPfNomineeImg!: string;
  nomineeAge!: number;
  nomineeDOB!: any;
  changeNomineeImage = true;
  showGuardianInputFields = false;
  dobMaxDate: NgbDateStruct;
  headerName!: string;
  routeName!: string;
  inValidFileErrorMsg = '';
  nomineeIDNumberValidation: IIdentityNumberValidation;
  guardianIDNumberValidation: IIdentityNumberValidation;
  totalConsumedPercentage = 0;
  pfNomineePhoto?: File;
  selectedPfAccountId!: number;
  saveFileName = '';

  editForm = this.pfNomineeFormService.createPfNomineeFormGroup();

  inValidNomineeAge = false;
  invalidGuardianAge = false;
  isValidFileType!: boolean;
  isValidFileSize!: boolean;
  selectedIdentityType: string | null = null;
  selectedGuardianIdentityType: string | null = null;

  constructor(
    public pfNomineeService: PfNomineeService,
    public pfNomineeFormService: PfNomineeFormService,
    protected pfAccountService: PfAccountService,
    protected employeeService: EmployeeCustomService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected dateValidationService: DateValidationService,
    public identityNumberValidationService: IdentityNumberValidationService,
    private fb: FormBuilder
  ) {
    this.dobMaxDate = dateValidationService.getDOBMaxDate();

    this.nomineeIDNumberValidation = { isValid: true, validationMsg: '' };
    this.guardianIDNumberValidation = { isValid: true, validationMsg: '' };
  }

  ngOnDestroy(): void {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfNominee }) => {
      if (pfNominee.id !== null && pfNominee.id !== undefined) {
        this.selectedPfAccountId = pfNominee.pfAccountId;
        this.loadNomineeList(this.selectedPfAccountId);
        this.loadSavedImage(pfNominee.id);
        this.updateForm(pfNominee);
      }

      this.nomineeDOB = pfNominee.dateOfBirth;

      this.pfAccountService.getAllPfAccountsList().subscribe((res: HttpResponse<IPfAccount[]>) => {
        this.pfAccounts = res.body || [];

        /*pf account selection field with ng-select*/
        this.pfAccounts = this.pfAccounts.map(pfAccount => {
          return {
            id: pfAccount.id,
            pin: pfAccount.pin,
            name: pfAccount.accHolderName,
            designation: pfAccount.designationName,
            accHolderName: pfAccount.pin + ' - ' + pfAccount.accHolderName + ' - ' + pfAccount.designationName,
          };
        });

        this.employeeService.getAllMinimal().subscribe((response: HttpResponse<IEmployee[]>) => {
          this.employees = response.body || [];

          /*employee select drop down with ng-select*/
          this.employees = this.employees.map(item => {
            return {
              id: item.id,
              pin: item.pin,
              name: item.fullName,
              designation: item.designationName,
              fullName: item.pin + ' - ' + item.fullName + ' - ' + item.designationName,
            };
          });
        });
      });
    });
    if (this.editForm.get(['id'])!.value) {
      this.editForm.get(['pfAccountId'])!.disable();
      //this.editForm.get(['sharePercentage'])!.disable();
    }

    if (sessionStorage.getItem('midHeaderOfNomineeReport') !== null && sessionStorage.getItem('midHeaderOfNomineeReport') !== undefined) {
      this.headerName = sessionStorage.getItem('midHeaderOfNomineeReport')!;
    }

    if (sessionStorage.getItem('midRouteOfNomineeReport') !== null && sessionStorage.getItem('midRouteOfNomineeReport') !== undefined) {
      this.routeName = sessionStorage.getItem('midRouteOfNomineeReport')!;
    }
  }

  loadNomineeList(pfAccountId: number): void {
    this.pfNomineeService.getAllByPfAccountId(pfAccountId).subscribe(res => {
      this.pfNominees = res.body!;
    });
    this.pfNomineeService.getRemainingSharePercentage(this.selectedPfAccountId).subscribe(res => {
      this.totalConsumedPercentage = 100 - res;
    });
  }

  updateForm(pfNominee: IPfNominee): void {
    this.pfNomineeFormService.resetForm(this.editForm, pfNominee);
  }

  isValidIdentityType(identityType: IdentityType): boolean {
    return (
      identityType === IdentityType.NID ||
      identityType === IdentityType.BIRTH_REGISTRATION ||
      identityType === IdentityType.PASSPORT ||
      identityType === IdentityType.OTHER
    );
  }

  previousState(): void {
    window.history.back();
  }

  onClickRedirectToViewPage(nominee: IPfNominee): void {
    if (nominee.id) {
      this.router.navigate(['/pf-nominee', nominee.id, 'view']);
    }
  }

  onClickPopulateForm(nominee: IPfNominee): void {
    if (nominee.id) {
      const nomineeId = nominee.id;
      this.editForm.reset();
      this.pfNomineeService.find(nomineeId).subscribe(res => {
        this.pfNominee = res.body!;
        this.updateForm(this.pfNominee);
        this.loadSavedImage(nomineeId);
      });
    }
  }

  redirectToNomineeReport(): void {
    if (this.totalConsumedPercentage !== 100) {
      swalOnRejectionForNomineeReport();
    } else {
      this.router.navigate(['/pf-nominee/print/', this.pfNominees[0].pin]);
    }
  }

  delete(nominee: any): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.pfNomineeService.delete(nominee.id).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.clearForm();
            this.loadNomineeList(this.selectedPfAccountId);
          },
          () => this.requestFailed()
        );
      }
    });
  }

  clearForm(): void {
    this.editForm.reset();
    //this.loadUsedSharePercentage();
    this.showGuardianInputFields = false;
  }

  checkSharePercentage(): void {
    if (!this.editForm.get('id')!.value) {
      this.pfNomineeService.getRemainingSharePercentage(this.editForm.get('pfAccountId')!.value).subscribe(
        res => this.save(Number(res)),
        () => this.requestFailed()
      );
    } else {
      const pfNominee = this.createFromForm();
      this.pfNomineeService.getRemainingSharePercentageForSavedPfNominee(pfNominee).subscribe(
        res => this.save(Number(res)),
        () => this.requestFailed()
      );
    }
  }

  save(remainingSharePercentage: number): void {
    const available = remainingSharePercentage - this.editForm.get('sharePercentage')!.value;
    if (available >= 0) {
      this.isSaving = true;
      const pfNominee = this.createFromForm();
      if (pfNominee.id !== undefined && pfNominee.id !== null) {
        if (this.pfNomineePhoto !== undefined && this.pfNomineePhoto !== null) {
          this.subscribeToSaveResponse(this.pfNomineeService.update(this.pfNomineePhoto, pfNominee));
          this.pfNomineePhoto = undefined;
        } else {
          this.subscribeToSaveResponse(this.pfNomineeService.updateWithOutFile(pfNominee));
          this.pfNomineePhoto = undefined;
        }
      } else {
        if (this.pfNomineePhoto !== undefined && this.pfNomineePhoto !== null) {
          this.subscribeToSaveResponse(this.pfNomineeService.create(this.pfNomineePhoto, pfNominee));
          this.pfNomineePhoto = undefined;
        }
      }
    } else {
      Swal.fire({
        icon: 'warning',
        text: 'Remaining ' + remainingSharePercentage + '% Share!',
        timer: 1500,
        showConfirmButton: false,
      });
    }
  }

  requestFailed(): void {
    Swal.fire({
      icon: 'error',
      text: 'Something went Wrong',
      timer: 1500,
      showConfirmButton: false,
    });
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfNominee>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    if (!this.editForm.get('id')!.value) {
      swalOnSavedSuccess();
      this.loadNomineeList(this.selectedPfAccountId);
    } else {
      swalOnUpdatedSuccess();
      this.loadNomineeList(this.selectedPfAccountId);
    }
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
    this.requestFailed();
  }

  private createFromForm(): IPfNominee {
    return this.pfNomineeFormService.getPfNominee(this.editForm);
  }

  uploadPfNomineeImage(event: any): void {
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

  /*  calculateAge(): void {
    if (moment(this.editForm.get('dateOfBirth')!.value).isAfter(moment())) {
      this.inValidNomineeAge = true;
      return;
    } else {
      this.inValidNomineeAge = false;
    }
    //const birthDate = new Date(this.nomineeDOB);
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

    const guardianNameFormControl = this.editForm.get('guardianName');
    const guardianDateOfBirthFormControl = this.editForm.get('guardianDateOfBirth');
    const guardianFatherOrSpouseNameFormControl = this.editForm.get('guardianFatherOrSpouseName');
    const guardianPresentAddressFormControl = this.editForm.get('guardianPresentAddress');
    const guardianPermanentAddressFormControl = this.editForm.get('guardianPermanentAddress');
    const relationshipFormControl = this.editForm.get('guardianRelationshipWithNominee');
    const guardianNidNumberFormControl = this.editForm.get('guardianNidNumber');
    const guardianBRN = this.editForm.get('guardianBrnNumber');
    if (this.nomineeAge < 18) {
      //this.guardianInfoValidation(true);
    } else {
      guardianNameFormControl!.clearValidators();
      guardianDateOfBirthFormControl!.clearValidators();
      guardianFatherOrSpouseNameFormControl!.clearValidators();
      guardianPresentAddressFormControl!.clearValidators();
      guardianPermanentAddressFormControl!.clearValidators();
      relationshipFormControl!.clearValidators();
      guardianNidNumberFormControl!.clearValidators();
    }
    guardianNameFormControl!.updateValueAndValidity();
    guardianDateOfBirthFormControl!.updateValueAndValidity();
    guardianFatherOrSpouseNameFormControl!.updateValueAndValidity();
    guardianPresentAddressFormControl!.updateValueAndValidity();
    guardianPermanentAddressFormControl!.updateValueAndValidity();
    relationshipFormControl!.updateValueAndValidity();
    guardianNidNumberFormControl!.updateValueAndValidity();
  }*/

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
      const birthDate = dayjs(this.editForm.get('dateOfBirth')!.value);
      const todayDate = dayjs();

      let years = todayDate.year() - birthDate.year();

      if (todayDate.month() < birthDate.month() || (todayDate.month() === birthDate.month() && todayDate.date() < birthDate.date())) {
        years--;
      }
      this.nomineeAge = years;

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

  getPfNomineeImage(pfNomineeId: number): string {
    const url = SERVER_API_URL + 'files/nominee-image/' + pfNomineeId;
    return url;
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

  checkGuardianAge(): void {
    if (dayjs(this.editForm.get('guardianDateOfBirth')!.value).isAfter(dayjs())) {
      this.invalidGuardianAge = true;
    } else {
      this.invalidGuardianAge = false;
    }
  }

  onChangePfAccountId(): void {
    const pfAccountId = this.editForm.get(['pfAccountId'])!.value;
    //const sharePercentage = this.editForm.get(['sharePercentage'])!.value;
    if (pfAccountId) {
      this.pfNomineeService.getRemainingSharePercentage(pfAccountId).subscribe(res => {
        this.allowedSharePercentage = res;
        if (this.allowedSharePercentage === 50) {
          this.minimumSharePercentage = this.maximumSharePercentage = 50;
          this.editForm
            .get(['sharePercentage'])!
            .setValidators([Validators.required, Validators.min(this.minimumSharePercentage), Validators.max(this.maximumSharePercentage)]);
        } else if (this.allowedSharePercentage === 100) {
          this.minimumSharePercentage = 50;
          this.maximumSharePercentage = 100;
          this.editForm
            .get(['sharePercentage'])!
            .setValidators([Validators.required, Validators.min(this.minimumSharePercentage), Validators.max(this.minimumSharePercentage)]);
        } else if (this.allowedSharePercentage < 50) {
          this.minimumSharePercentage = this.maximumSharePercentage = 0;
          this.editForm.get(['sharePercentage'])!.setValidators([Validators.required, Validators.min(0), Validators.max(0)]);
        }
      });
    }
  }

  guardianInfoValidation(isValidate: boolean): void {
    const guardianNameFormControl = this.editForm.get('guardianName');
    const guardianDateOfBirthFormControl = this.editForm.get('guardianDateOfBirth');
    const guardianFatherOrSpouseNameFormControl = this.editForm.get('guardianFatherOrSpouseName');
    const guardianPresentAddressFormControl = this.editForm.get('guardianPresentAddress');
    const guardianPermanentAddressFormControl = this.editForm.get('guardianPermanentAddress');
    const relationshipFormControl = this.editForm.get('guardianRelationshipWithNominee');
    const guardianNidNumberFormControl = this.editForm.get('guardianNidNumber');
    const guardianBRN = this.editForm.get('guardianBrnNumber');
    if (isValidate) {
      guardianNameFormControl!.setValidators([
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(250),
        Validators.pattern('^[a-zA-Z\\s]+$'),
      ]);
      guardianDateOfBirthFormControl!.setValidators([Validators.required]);
      guardianFatherOrSpouseNameFormControl!.setValidators([
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(250),
        Validators.pattern('^[a-zA-Z\\s]+$'),
      ]);
      guardianPresentAddressFormControl!.setValidators([
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(250),
        Validators.pattern('^[a-zA-Z0-9.\\s,\\-]+$'),
      ]);
      guardianPermanentAddressFormControl!.setValidators([
        Validators.required,
        Validators.minLength(10),
        Validators.maxLength(250),
        Validators.pattern('^[a-zA-Z0-9.\\s,\\-]+$'),
      ]);
      relationshipFormControl!.setValidators([
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(250),
        Validators.pattern('^[a-zA-Z0-9.\\s,\\-]+$'),
      ]);
      guardianNidNumberFormControl!.setValidators([Validators.minLength(10), Validators.maxLength(17), Validators.pattern('^[0-9]+$')]);
      guardianBRN!.setValidators([Validators.minLength(15), Validators.maxLength(20), Validators.pattern('^[0-9]+$')]);
    } else {
      guardianNameFormControl!.clearValidators();
      guardianDateOfBirthFormControl!.clearValidators();
      guardianFatherOrSpouseNameFormControl!.clearValidators();
      guardianPresentAddressFormControl!.clearValidators();
      guardianPermanentAddressFormControl!.clearValidators();
      relationshipFormControl!.clearValidators();
      guardianNidNumberFormControl!.clearValidators();
    }
    guardianNameFormControl!.updateValueAndValidity();
    guardianDateOfBirthFormControl!.updateValueAndValidity();
    guardianFatherOrSpouseNameFormControl!.updateValueAndValidity();
    guardianPresentAddressFormControl!.updateValueAndValidity();
    guardianPermanentAddressFormControl!.updateValueAndValidity();
    relationshipFormControl!.updateValueAndValidity();
    guardianNidNumberFormControl!.updateValueAndValidity();
  }

  /*  onIdentityChange(): void {
    this.selectedIdentityType = this.editForm.get(['identityType'])!.value;
    this.updateValidationOnNomineeIdentity();
  }*/

  onIdentityChange(): void {
    this.selectedIdentityType = this.editForm.get(['identityType'])!.value;
    this.editForm.get(['idNumber'])!.reset();
    this.editForm.get(['documentName'])!.reset();
    this.updateValidationOnNomineeIdentityType();
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

  updateValidationOnNomineeIdentity(): void {
    const identityType = this.editForm.get(['identityType'])!.value;

    const isValid = this.isValidIdentityType(identityType);
    if (isValid) {
      this.editForm.get('idNumber')!.setValidators([Validators.required]);
    } else {
      this.editForm.get('idNumber')!.clearValidators();
    }

    if (identityType === IdentityType.OTHER) {
      this.editForm.get('documentName')!.setValidators([Validators.required]);
    } else {
      this.editForm.get('documentName')!.clearValidators();
    }
    this.editForm.get('documentName')!.updateValueAndValidity();
    this.editForm.get('idNumber')!.updateValueAndValidity();
  }

  onGuardianIdentityChange(): void {
    this.selectedGuardianIdentityType = this.editForm.get(['guardianIdentityType'])!.value;
    this.updateValidationOnGuardianIdentity();
  }

  updateValidationOnGuardianIdentity(): void {
    const identityType = this.editForm.get(['guardianIdentityType'])!.value;
    const isValid = this.isValidIdentityType(identityType);

    if (isValid) {
      this.editForm.get('guardianIdNumber')!.setValidators([Validators.required]);
    } else {
      this.editForm.get('guardianIdNumber')!.clearValidators();
    }

    if (identityType === IdentityType.OTHER) {
      this.editForm.get('guardianDocumentName')!.setValidators([Validators.required]);
    } else {
      this.editForm.get('guardianDocumentName')!.clearValidators();
    }
    this.editForm.get('guardianDocumentName')!.updateValueAndValidity();
    this.editForm.get('guardianIdNumber')!.updateValueAndValidity();
  }

  shouldShowNomineeIdNumberField(): boolean {
    const nomineeIdentityType = this.editForm.get('identityType')!.value;
    return this.isValidIdentityType(nomineeIdentityType);
  }

  shouldShowGuardianIdNumberField(): boolean {
    const guardianIdentityType = this.editForm.get('guardianIdentityType')!.value;
    return this.isValidIdentityType(guardianIdentityType);
  }

  onChangeSelectPfNomineeImage(event: any): void {
    /* validation: [file type: {image/jpeg}, file size: 2MB,] */
    this.isValidFileSize = true;
    this.isValidFileType = true;
    if (event.target.files != null && event.target.files.length > 0) {
      this.inValidFileErrorMsg = '';
      const file = event.target.files[0];

      if (!file || file.type !== 'image/jpeg') {
        this.isValidFileType = false;
        this.inValidFileErrorMsg = 'Select JPG/JPEG image only';
        return;
      }
      const sizeInKB = Number(file.size / 1024);
      if (sizeInKB > 2048) {
        this.isValidFileSize = false;
        this.inValidFileErrorMsg = 'Max allowed file size is 2MB';
        return;
      } else {
        this.isValidFileSize = true;
      }
      this.pfNomineePhoto = file;
      this.anyFileSelected = true;
    }
    return;
  }

  changeToReUpload(): void {
    this.changeNomineeImage = true;
    this.anyFileSelected = false;
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

  getPfAccountId(event: any): void {
    this.selectedPfAccountId = event.id;
    this.loadNomineeList(this.selectedPfAccountId);
    this.checkSharePercentage();
  }
}
