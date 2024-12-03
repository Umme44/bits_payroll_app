import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EmploymentActionsService } from '../employment-actions.service';
import { IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './increment-delete.component.html',
})
export class IncrementDeleteComponent {
  employmentHistory?: IEmploymentHistory;

  constructor(
    protected employmentHistoryService: EmploymentActionsService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employmentHistoryService.deleteIncrement(id).subscribe(() => {
      this.eventManager.broadcast('employmentHistoryListModification');
      this.activeModal.close();
    });
  }
}
