import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkingExperience } from '../working-experience.model';
import { WorkingExperienceService } from '../service/working-experience.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './working-experience-delete-dialog.component.html',
})
export class WorkingExperienceDeleteDialogComponent {
  workingExperience?: IWorkingExperience;

  constructor(protected workingExperienceService: WorkingExperienceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workingExperienceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
