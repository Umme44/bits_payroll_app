import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PfLoanApplicationFormService } from './pf-loan-application-form.service';
import { EventManager } from '../../core/util/event-manager.service';
import { IPfLoanApplication } from '../../shared/legacy/legacy-model/pf-loan-application.model';

@Component({
  templateUrl: './pf-loan-application-form-delete-dialog.component.html',
})
export class PfLoanApplicationFormDeleteDialogComponent {
  pfLoanApplication?: IPfLoanApplication;

  constructor(
    protected pfLoanApplicationFormService: PfLoanApplicationFormService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pfLoanApplicationFormService.delete(id).subscribe(() => {
      this.eventManager.broadcast('pfLoanApplicationListModification');
      this.activeModal.close();
    });
  }
}
