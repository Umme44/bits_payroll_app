import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PfLoanFormService, PfLoanFormGroup } from './pf-loan-form.service';
import { IPfLoan } from '../pf-loan.model';
import { PfLoanService } from '../service/pf-loan.service';
import { IPfLoanApplication } from 'app/entities/pf-loan-application/pf-loan-application.model';
import { PfLoanApplicationService } from 'app/entities/pf-loan-application/service/pf-loan-application.service';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { PfAccountService } from 'app/entities/pf-account/service/pf-account.service';
import { PfLoanStatus } from 'app/entities/enumerations/pf-loan-status.model';

type SelectableEntity = IPfLoanApplication | IPfAccount;

@Component({
  selector: 'jhi-pf-loan-update',
  templateUrl: './pf-loan-update.component.html',
})
export class PfLoanUpdateComponent implements OnInit {
  isSaving = false;
  pfLoan: IPfLoan | null = null;
  pfLoanStatusValues = Object.keys(PfLoanStatus);

  pfloanapplications: IPfLoanApplication[] = [];
  pfaccounts: IPfAccount[] = [];

  editForm: PfLoanFormGroup = this.pfLoanFormService.createPfLoanFormGroup();

  constructor(
    protected pfLoanService: PfLoanService,
    protected pfLoanFormService: PfLoanFormService,
    protected pfLoanApplicationService: PfLoanApplicationService,
    protected pfAccountService: PfAccountService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePfLoanApplication = (o1: IPfLoanApplication | null, o2: IPfLoanApplication | null): boolean =>
    this.pfLoanApplicationService.comparePfLoanApplication(o1, o2);

  comparePfAccount = (o1: IPfAccount | null, o2: IPfAccount | null): boolean => this.pfAccountService.comparePfAccount(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfLoan }) => {
      this.pfLoan = pfLoan;
      if (pfLoan) {
        this.updateForm(pfLoan);
      }

      this.pfLoanApplicationService
        .query()
        .subscribe((res: HttpResponse<IPfLoanApplication[]>) => (this.pfloanapplications = res.body || []));

      this.pfAccountService.getAllPfAccountsList().subscribe((res: HttpResponse<IPfAccount[]>) => (this.pfaccounts = res.body || []));
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pfLoan = this.pfLoanFormService.getPfLoan(this.editForm);
    if (pfLoan.id !== null) {
      this.subscribeToSaveResponse(this.pfLoanService.update(pfLoan));
    } else {
      this.subscribeToSaveResponse(this.pfLoanService.create(pfLoan as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfLoan>>): void {
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

  protected updateForm(pfLoan: IPfLoan): void {
    this.pfLoan = pfLoan;
    this.pfLoanFormService.resetForm(this.editForm, pfLoan);
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
