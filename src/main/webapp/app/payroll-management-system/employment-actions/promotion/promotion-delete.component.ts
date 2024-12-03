import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EmploymentActionsService } from '../employment-actions.service';
import { IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './promotion-delete.component.html',
})
export class PromotionDeleteComponent {
  employmentHistory?: IEmploymentHistory;

  constructor(
    protected employmentActionsService: EmploymentActionsService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employmentActionsService.deletePromotions(id).subscribe(() => {
      this.eventManager.broadcast('employmentHistoryListModification');
      this.activeModal.close();
    });
  }
}
