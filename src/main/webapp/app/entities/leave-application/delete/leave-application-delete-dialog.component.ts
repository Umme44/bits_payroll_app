import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {ILeaveApplication} from "../leave-application.model";
import {LeaveApplicationService} from "../service/leave-application.service";
import {EventManager} from "../../../core/util/event-manager.service";


@Component({
  templateUrl: './leave-application-delete-dialog.component.html',
})
export class LeaveApplicationDeleteDialogComponent {
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
    this.leaveApplicationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('leaveApplicationListModification');
      this.activeModal.close();
    });
  }
}
