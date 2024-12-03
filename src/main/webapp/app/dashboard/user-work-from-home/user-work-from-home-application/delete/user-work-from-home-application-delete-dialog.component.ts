import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { UserWorkFromHomeApplicationService } from '../service/user-work-from-home-application.service';
import { IUserWorkFromHomeApplication } from '../user-work-from-home-application.model';

@Component({
  templateUrl: './user-work-from-home-application-delete-dialog.component.html',
})
export class UserWorkFromHomeApplicationDeleteDialogComponent {
  workFromHomeApplication?: IUserWorkFromHomeApplication;

  constructor(protected workFromHomeApplicationService: UserWorkFromHomeApplicationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workFromHomeApplicationService.delete(id).subscribe(() => {
      this.activeModal.close();
    });
  }
}
