import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrainingHistory } from '../training-history.model';
import { TrainingHistoryService } from '../service/training-history.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './training-history-delete-dialog.component.html',
})
export class TrainingHistoryDeleteDialogComponent {
  trainingHistory?: ITrainingHistory;

  constructor(protected trainingHistoryService: TrainingHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trainingHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
