import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecruitmentRequisitionForm } from '../recruitment-requisition-form.model';
import { RecruitmentRequisitionFormService } from '../service/recruitment-requisition-form.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './recruitment-requisition-form-delete-dialog.component.html',
})
export class RecruitmentRequisitionFormDeleteDialogComponent {
  recruitmentRequisitionForm?: IRecruitmentRequisitionForm;

  constructor(protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recruitmentRequisitionFormService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
