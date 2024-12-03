import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PfAccountFormService, PfAccountFormGroup } from './pf-account-form.service';
import { IPfAccount } from '../pf-account.model';
import { PfAccountService } from '../service/pf-account.service';
import { PfAccountStatus } from 'app/entities/enumerations/pf-account-status.model';
import { FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-pf-account-update',
  templateUrl: './pf-account-update.component.html',
})
export class PfAccountUpdateComponent implements OnInit {
  isDateInvalid = false;
  isConflict = false;
  isSaving = false;
  pfAccount: IPfAccount | null = null;
  pfAccountStatusValues = Object.keys(PfAccountStatus);

  editForm: PfAccountFormGroup = this.pfAccountFormService.createPfAccountFormGroup();
  // editForm: PfAccountFormGroup = this.pfAccountFormService.editForm;

  constructor(
    protected pfAccountService: PfAccountService,
    protected pfAccountFormService: PfAccountFormService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  // editForm = this.fb.group({
  //   id: [],
  //   pfCode: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(250), Validators.pattern('^[a-zA-Z0-9\\s]+$')]],
  //   membershipStartDate: [null, [Validators.required]],
  //   membershipEndDate: [],
  //   dateOfJoining: [null, [Validators.required]],
  //   dateOfConfirmation: [null, [Validators.required]],
  //   status: [null, [Validators.required]],
  //   designationName: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(250)]],
  //   departmentName: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(250)]],
  //   unitName: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(250)]],
  //   accHolderName: [
  //     null,
  //     [Validators.required, Validators.minLength(2), Validators.maxLength(250), Validators.pattern('^[a-zA-Z.\\s-]+$')],
  //   ],
  //   pin: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(250), Validators.pattern('^[a-zA-Z0-9\\s]+$')]],
  // })

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfAccount }) => {
      this.pfAccount = pfAccount;
      if (pfAccount) {
        this.updateForm(pfAccount);
        this.editForm.get('pfCode').disable();
        this.editForm.get('pin').disable();
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pfAccount = this.pfAccountFormService.getPfAccount(this.editForm);
    if (pfAccount.id !== null) {
      this.subscribeToSaveResponse(this.pfAccountService.update(pfAccount));
    } else {
      this.subscribeToSaveResponse(this.pfAccountService.create(pfAccount as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfAccount>>): void {
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

  protected updateForm(pfAccount: IPfAccount): void {
    this.pfAccount = pfAccount;
    this.pfAccountFormService.resetForm(this.editForm, pfAccount);
  }

  // memberShipDateValidation() {
  //
  //   this.pfAccountFormService.memberShipDateValidationX()
  // }
}
