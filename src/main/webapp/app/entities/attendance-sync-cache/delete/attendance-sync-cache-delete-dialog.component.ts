import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttendanceSyncCache } from '../attendance-sync-cache.model';
import { AttendanceSyncCacheService } from '../service/attendance-sync-cache.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './attendance-sync-cache-delete-dialog.component.html',
})
export class AttendanceSyncCacheDeleteDialogComponent {
  attendanceSyncCache?: IAttendanceSyncCache;

  constructor(protected attendanceSyncCacheService: AttendanceSyncCacheService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attendanceSyncCacheService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
