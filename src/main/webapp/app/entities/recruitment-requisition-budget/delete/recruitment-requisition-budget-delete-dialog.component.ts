import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecruitmentRequisitionBudget } from '../recruitment-requisition-budget.model';
import { RecruitmentRequisitionBudgetService } from '../service/recruitment-requisition-budget.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './recruitment-requisition-budget-delete-dialog.component.html',
})
export class RecruitmentRequisitionBudgetDeleteDialogComponent {
  recruitmentRequisitionBudget?: IRecruitmentRequisitionBudget;

  constructor(protected recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recruitmentRequisitionBudgetService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
