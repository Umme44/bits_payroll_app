import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {Validators} from '@angular/forms';
import {InsuranceClaimFormGroup, InsuranceClaimFormService} from './insurance-claim-form.service';
import {IInsuranceClaim} from '../insurance-claim.model';
import {InsuranceClaimService} from '../service/insurance-claim.service';
import {IInsuranceRegistration} from 'app/entities/insurance-registration/insurance-registration.model';
import {InsuranceRegistrationService} from 'app/entities/insurance-registration/service/insurance-registration.service';
import {UserService} from 'app/entities/user/user.service';
import {ClaimStatus} from 'app/entities/enumerations/claim-status.model';
import {swalOnRequestError, swalOnSavedSuccess} from "../../../shared/swal-common/swal-common";
import {InsuranceStatus} from "../../enumerations/insurance-status.model";

@Component({
  selector: 'jhi-insurance-claim-update',
  templateUrl: './insurance-claim-update.component.html',
})
export class InsuranceClaimUpdateComponent implements OnInit {
  isSaving = false;
  insuranceClaim: IInsuranceClaim | null = null;
  claimStatusValues = Object.keys(ClaimStatus);

  // insuranceRegistrationsSharedCollection: IInsuranceRegistration[] = [];
  // usersSharedCollection: IUser[] = [];

  insuranceRegistration!: IInsuranceRegistration;
  isInsuranceCardIdInvalid = false;
  isInsuranceCardStatusInvalid = false;
  isSettledAmountInvalid = false;

  settlementDateDp: any;
  paymentDateDp: any;
  regretDateDp: any;
  createdAtDp: any;
  updatedAtDp: any;


  editForm: InsuranceClaimFormGroup = this.insuranceClaimFormService.createInsuranceClaimFormGroup();

  constructor(
    protected insuranceClaimService: InsuranceClaimService,
    protected insuranceClaimFormService: InsuranceClaimFormService,
    protected insuranceRegistrationService: InsuranceRegistrationService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private router: Router
  ) {
  }

  // compareInsuranceRegistration = (o1: IInsuranceRegistration | null, o2: IInsuranceRegistration | null): boolean =>
  //   this.insuranceRegistrationService.compareInsuranceRegistration(o1, o2);

  // compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({insuranceClaim}) => {
      this.insuranceClaim = insuranceClaim;
      if (insuranceClaim) {
        this.updateForm(insuranceClaim);
      } else {
        this.disableInputFields();
      }
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const insuranceClaim = this.insuranceClaimFormService.getInsuranceClaim(this.editForm);
    if(insuranceClaim.claimStatus===ClaimStatus.REGRETTED){
      insuranceClaim.settlementDate=null;
      insuranceClaim.paymentDate=null;
    }else{
      insuranceClaim.regretDate=null;
    }
    if (insuranceClaim.id !== null) {
      this.subscribeToSaveResponse(this.insuranceClaimService.update(insuranceClaim));
    } else {
      this.subscribeToSaveResponse(this.insuranceClaimService.create(insuranceClaim as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInsuranceClaim>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    swalOnSavedSuccess();
    this.isSaving = false;
    this.router.navigate(['/insurance-claim']);
  }

  protected onSaveError(): void {
    swalOnRequestError();
    this.isSaving = false;
  }

  protected updateForm(insuranceClaim: IInsuranceClaim): void {
    this.insuranceClaim = insuranceClaim;
    this.insuranceClaimFormService.resetForm(this.editForm, insuranceClaim);

    // this.insuranceRegistrationsSharedCollection =
    //   this.insuranceRegistrationService.addInsuranceRegistrationToCollectionIfMissing<IInsuranceRegistration>(
    //     this.insuranceRegistrationsSharedCollection,
    //     insuranceClaim.insuranceRegistration
    //   );
    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   insuranceClaim.createdBy,
    //   insuranceClaim.updatedBy
    // );
  }

  protected loadRelationshipsOptions(): void {
    // this.insuranceRegistrationService
    //   .query()
    //   .pipe(map((res: HttpResponse<IInsuranceRegistration[]>) => res.body ?? []))
    //   .pipe(
    //     map((insuranceRegistrations: IInsuranceRegistration[]) =>
    //       this.insuranceRegistrationService.addInsuranceRegistrationToCollectionIfMissing<IInsuranceRegistration>(
    //         insuranceRegistrations,
    //         this.insuranceClaim?.insuranceRegistration
    //       )
    //     )
    //   )
    //   .subscribe(
    //     (insuranceRegistrations: IInsuranceRegistration[]) => (this.insuranceRegistrationsSharedCollection = insuranceRegistrations)
    //   );
    //
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(users, this.insuranceClaim?.createdBy, this.insuranceClaim?.updatedBy)
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  trackById(index: number, item: IInsuranceRegistration): any {
    return item.id;
  }

  onChangeInsuranceId(): void {
    this.isInsuranceCardIdInvalid = false;
    this.isInsuranceCardStatusInvalid = false;
    const cardId = this.editForm.get(['insuranceCardId'])!.value;

    if (cardId !== null && cardId !== undefined && cardId.trim() !== '') {
      this.insuranceRegistrationService.findInsuranceRegistrationByInsuranceCardId(cardId).subscribe(res => {
        if (res.body!.length > 0) {
          this.insuranceRegistration = res.body![0];
          if (this.insuranceRegistration.insuranceStatus !== InsuranceStatus.APPROVED) {
            this.isInsuranceCardStatusInvalid = true;
          }
          this.editForm.get(['insuranceRegistrationId'])!.setValue(this.insuranceRegistration.id);
          this.enableInputFields();
        } else {
          this.isInsuranceCardIdInvalid = true;
          this.disableInputFields();
        }
      });
    }
  }

  disableInputFields(): void {
    this.editForm.get(['settlementDate'])!.disable();
    this.editForm.get(['settledAmount'])!.disable();
    this.editForm.get(['paymentDate'])!.disable();
    this.editForm.get(['regretDate'])!.disable();
    this.editForm.get(['regretReason'])!.disable();
    this.editForm.get(['claimedAmount'])!.disable();
    this.editForm.get(['claimStatus'])!.disable();
  }

  enableInputFields(): void {
    this.editForm.get(['settlementDate'])!.enable();
    this.editForm.get(['settledAmount'])!.enable();
    this.editForm.get(['paymentDate'])!.enable();
    this.editForm.get(['regretDate'])!.enable();
    this.editForm.get(['regretReason'])!.enable();
    this.editForm.get(['claimedAmount'])!.enable();
    this.editForm.get(['claimStatus'])!.enable();
  }

  onChangeClaimStatus(): void {
    this.isSettledAmountInvalid = false;

    const status = this.editForm.get(['claimStatus'])!.value;
    this.resetFormAndClearValueAndValidators();

    if (status === ClaimStatus.REGRETTED) {
      this.editForm.get(['claimedAmount'])!.setValidators(Validators.required);
      this.editForm.get(['regretDate'])!.setValidators(Validators.required);
      this.editForm.get(['regretReason'])!.setValidators(Validators.required);
    } else {
      this.editForm.get(['settlementDate'])!.setValidators(Validators.required);
      this.editForm.get(['settledAmount'])!.setValidators(Validators.required);
      this.editForm.get(['paymentDate'])!.setValidators(Validators.required);
      this.editForm.get(['claimedAmount'])!.setValidators(Validators.required);
    }
    this.editForm.get(['settlementDate'])!.updateValueAndValidity();
    this.editForm.get(['settledAmount'])!.updateValueAndValidity();
    this.editForm.get(['paymentDate'])!.updateValueAndValidity();
    this.editForm.get(['regretDate'])!.updateValueAndValidity();
    this.editForm.get(['regretReason'])!.updateValueAndValidity();
    this.editForm.get(['claimedAmount'])!.updateValueAndValidity();
  }

  resetFormAndClearValueAndValidators(): void {
    this.editForm.get(['settlementDate'])!.reset();
    this.editForm.get(['settledAmount'])!.reset();
    this.editForm.get(['paymentDate'])!.reset();
    this.editForm.get(['regretDate'])!.reset();
    this.editForm.get(['regretReason'])!.reset();
    this.editForm.get(['claimedAmount'])!.reset();

    this.editForm.get(['settlementDate'])!.clearValidators();
    this.editForm.get(['settledAmount'])!.clearValidators();
    this.editForm.get(['paymentDate'])!.clearValidators();
    this.editForm.get(['regretDate'])!.clearValidators();
    this.editForm.get(['regretReason'])!.clearValidators();
    this.editForm.get(['claimedAmount'])!.clearValidators();
  }

  onChangeSettledAmount(): void {
    this.isSettledAmountInvalid = false;

    const claimedAmount = this.editForm.get(['claimedAmount'])!.value;
    const settledAmount = this.editForm.get(['settledAmount'])!.value;

    if (claimedAmount !== null && claimedAmount !== undefined && settledAmount !== null && settledAmount !== undefined) {
      if (settledAmount > claimedAmount) {
        this.isSettledAmountInvalid = true;
      } else {
        this.isSettledAmountInvalid = false;
      }
    }
  }
}
