import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PfLoanRepaymentFormService, PfLoanRepaymentFormGroup } from './pf-loan-repayment-form.service';
import { IPfLoanRepayment } from '../pf-loan-repayment.model';
import { PfLoanRepaymentService } from '../service/pf-loan-repayment.service';
import { IPfLoan } from 'app/entities/pf-loan/pf-loan.model';
import { PfLoanService } from 'app/entities/pf-loan/service/pf-loan.service';
import { PfRepaymentStatus } from 'app/entities/enumerations/pf-repayment-status.model';

@Component({
  selector: 'jhi-pf-loan-repayment-update',
  templateUrl: './pf-loan-repayment-update.component.html',
})
export class PfLoanRepaymentUpdateComponent implements OnInit {
  isSaving = false;
  pfLoanRepayment: IPfLoanRepayment | null = null;
  pfRepaymentStatusValues = Object.keys(PfRepaymentStatus);

  pfloans: IPfLoan[] = [];

  editForm: PfLoanRepaymentFormGroup = this.pfLoanRepaymentFormService.createPfLoanRepaymentFormGroup();

  constructor(
    protected pfLoanRepaymentService: PfLoanRepaymentService,
    protected pfLoanRepaymentFormService: PfLoanRepaymentFormService,
    protected pfLoanService: PfLoanService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePfLoan = (o1: IPfLoan | null, o2: IPfLoan | null): boolean => this.pfLoanService.comparePfLoan(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfLoanRepayment }) => {
      this.pfLoanRepayment = pfLoanRepayment;
      if (pfLoanRepayment) {
        this.updateForm(pfLoanRepayment);
      }
      this.pfLoanService.query().subscribe((res: HttpResponse<IPfLoan[]>) => (this.pfloans = res.body || []));
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pfLoanRepayment = this.pfLoanRepaymentFormService.getPfLoanRepayment(this.editForm);
    if (pfLoanRepayment.id !== null) {
      this.subscribeToSaveResponse(this.pfLoanRepaymentService.update(pfLoanRepayment));
    } else {
      this.subscribeToSaveResponse(this.pfLoanRepaymentService.create(pfLoanRepayment as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfLoanRepayment>>): void {
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

  protected updateForm(pfLoanRepayment: IPfLoanRepayment): void {
    this.pfLoanRepayment = pfLoanRepayment;
    this.pfLoanRepaymentFormService.resetForm(this.editForm, pfLoanRepayment);
  }
  trackById(index: number, item: IPfLoan): any {
    return item.id;
  }
}
