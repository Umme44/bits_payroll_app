import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRecruitmentRequisitionForm } from '../../../shared/model/recruitment-requisition-form.model';
import { RecruitmentRequisitionFormService } from '../recruitment-requisition-form.service';

@Component({
  templateUrl: './recruitment-requisition-form-delete-dialog.component.html',
})
export class RecruitmentRequisitionFormDeleteDialogComponent {
  recruitmentRequisitionForm?: IRecruitmentRequisitionForm;

  constructor(
    protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recruitmentRequisitionFormService.delete(id).subscribe(() => {
      this.eventManager.broadcast('recruitmentRequisitionFormListModification');
      this.activeModal.close();
    });
  }
}
