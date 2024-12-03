import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserFeedback } from '../user-feedback.model';
import { UserFeedbackService } from '../service/user-feedback.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './user-feedback-delete-dialog.component.html',
})
export class UserFeedbackDeleteDialogComponent {
  userFeedback?: IUserFeedback;

  constructor(protected userFeedbackService: UserFeedbackService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userFeedbackService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
