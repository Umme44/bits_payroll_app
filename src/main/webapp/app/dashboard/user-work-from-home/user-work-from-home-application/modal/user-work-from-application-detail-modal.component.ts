import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { Status } from '../../../../shared/model/enumerations/status.model';
import { IUserWorkFromHomeApplication } from '../user-work-from-home-application.model';

@Component({
  selector: 'jhi-work-from-application-detail-modal',
  templateUrl: './user-work-from-application-detail-modal.component.html',
})
export class UserWorkFromApplicationDetailModalComponent implements OnInit, OnDestroy {
  workFromHomeApplication!: IUserWorkFromHomeApplication;

  constructor(protected activatedRoute: ActivatedRoute, protected activeModal: NgbActiveModal, private modalService: NgbModal) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {}

  previousState(): void {
    window.history.back();
  }

  closeModal(): void {
    this.activeModal.dismiss('Cross click');
  }

  getUiFriendlyStatus(status: Status): string {
    if (status === 'APPROVED') {
      return 'Approved';
    } else if (status === 'PENDING') {
      return 'Pending';
    } else {
      return 'Not Approved';
    }
  }
}
