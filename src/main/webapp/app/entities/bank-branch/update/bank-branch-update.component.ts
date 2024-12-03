import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { BankBranchFormGroup, BankBranchFormService } from './bank-branch-form.service';
import { IBankBranch } from '../bank-branch.model';
import { BankBranchService } from '../service/bank-branch.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-bank-branch-update',
  templateUrl: './bank-branch-update.component.html',
})
export class BankBranchUpdateComponent implements OnInit {
  isSaving = false;
  bankBranch: IBankBranch | null = null;

  editForm: BankBranchFormGroup = this.bankBranchFormService.createBankBranchFormGroup();

  constructor(
    protected bankBranchService: BankBranchService,
    protected bankBranchFormService: BankBranchFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankBranch }) => {
      this.bankBranch = bankBranch;
      if (bankBranch) {
        this.updateForm(bankBranch);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankBranch = this.bankBranchFormService.getBankBranch(this.editForm);
    if (bankBranch.id !== null) {
      this.subscribeToSaveResponse(this.bankBranchService.update(bankBranch));
    } else {
      this.subscribeToSaveResponse(this.bankBranchService.create(bankBranch as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankBranch>>): void {
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
    this.showErrorForInvalidInput()
  }

  showErrorForInvalidInput(){
    Swal.fire({
      icon: 'error',
      text: 'Invalid input',
      timer: 3500,
      showConfirmButton: false,
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bankBranch: IBankBranch): void {
    this.bankBranch = bankBranch;
    this.bankBranchFormService.resetForm(this.editForm, bankBranch);
  }
}
