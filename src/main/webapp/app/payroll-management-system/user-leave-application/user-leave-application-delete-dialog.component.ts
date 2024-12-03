import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ILeaveApplication } from '../../shared/legacy/legacy-model/leave-application.model';
import { LeaveApplicationService } from '../../shared/legacy/legacy-service/leave-application.service';
import { EventManager } from '../../core/util/event-manager.service';

@Component({
  templateUrl: './user-leave-application-delete-dialog.component.html',
})
export class UserLeaveApplicationDeleteDialogComponent {
  leaveApplication?: ILeaveApplication;

  constructor(
    protected leaveApplicationService: LeaveApplicationService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.leaveApplicationService.deleteUserLeaveApplication(id).subscribe(() => {
      this.eventManager.broadcast('leaveApplicationListModification');
      this.activeModal.close();
    });
  }
}
